package com.payze.paylib

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import com.payze.paylib.model.CardInfo
import com.payze.paylib.network.ApiService
import com.payze.paylib.network.TransactionStatus
import com.payze.paylib.network.sendRequest

class Payze(private val context: Context) {

    private var transactionID: String? = null
    private var onSuccess: (() -> Unit)? = null
    private var onError: ((code: Int?, error: String?) -> Unit)? = null
    private val activity get() = context as AppCompatActivity

    init {
        ApiService.initialize()
    }

    fun pay(
        cardInfo: CardInfo,
        transactionID: String,
        billingAddress: String = "",
        onSuccess: () -> Unit,
        onError: (code: Int?, error: String?) -> Unit
    ) {
        pay(
            cardInfo.number,
            cardInfo.cardHolder,
            cardInfo.expirationDate,
            cardInfo.securityNumber,
            transactionID,
            billingAddress,
            onSuccess,
            onError
        )
    }

    fun pay(
        number: String,
        cardHolder: String,
        expirationDate: String,
        securityNumber: String,
        transactionID: String,
        billingAddress: String = "",
        onSuccess: () -> Unit,
        onError: (code: Int?, error: String?) -> Unit
    ) {
        this.transactionID = transactionID
        this.onSuccess = onSuccess
        this.onError = onError

        ApiService.get().sendPaymentData(
            number,
            cardHolder,
            expirationDate,
            securityNumber,
            transactionID,
            billingAddress
        ).sendRequest(
            successWithData = { response ->
                response?.let {
                    when {
                        it.success == true && it.threeDSIsPresent == true && !it.url.isNullOrBlank() ->
                            openWebView(it.url)
                        it.success == true && it.threeDSIsPresent != true ->
                            checkTransactionStatus(transactionID)
                        else ->
                            handleError(KEY_UNKNOWN, context.getString(R.string.unknown_error))
                    }
                }
            },
            onConnectionFailure = {
                handleError(KEY_NO_CONNECTION, it.message)
            },
            responseFailure = { code, error ->
                handleError(code, error)
            },
            failure = {
                handleError(KEY_REQUEST_FAIL, it.message)
            }
        )

    }

    private fun checkTransactionStatus(
        transactionID: String?
    ) {
        if (transactionID == null) return

        ApiService.get().getTransactionStatus(transactionID).sendRequest(
            successWithData = {
                when (it?.status) {
                    TransactionStatus.SUCCESS -> onSuccess?.invoke()
                    else -> handleError(KEY_STATUS_NOT_SUCCESS, "status: ${it?.status}")
                }
            },
            onConnectionFailure = {
                handleError(KEY_NO_CONNECTION, it.message)
            },
            responseFailure = { code, error ->
                handleError(code, error)
            },
            failure = {
                handleError(KEY_REQUEST_FAIL, it.message)
            }
        )
    }

    private fun handleError(
        code: Int? = null,
        error: String? = null
    ) {
        onError?.invoke(code, error)
    }

    private fun openWebView(url: String) {
        savedScreenOrientation = activity.requestedOrientation
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        val dialog = WebViewDialog.instance(url)
        WebViewDialog.listener = object : WebViewDialog.DialogCallBack {
            override fun onDismiss(success: Boolean) {
                savedScreenOrientation?.let {
                    activity.requestedOrientation = it
                }
                if (success)
                    checkTransactionStatus(transactionID)
                else
                    handleError(KEY_DISMISS_2FA, context.getString(R.string.reject_auth))
            }
        }
        dialog.show(activity.supportFragmentManager, DIALOG_TAG)
    }

    companion object {
        const val TAG = "PayzeLib"
        const val DIALOG_TAG = "webViewDialog"

        var savedScreenOrientation: Int? = null

        const val KEY_NO_CONNECTION = 1001
        const val KEY_REQUEST_FAIL = 1002
        const val KEY_DISMISS_2FA = 1003
        const val KEY_STATUS_NOT_SUCCESS = 1004
        const val KEY_UNKNOWN = 1005
    }
}