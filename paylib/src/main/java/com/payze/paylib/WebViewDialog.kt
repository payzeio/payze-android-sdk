package com.payze.paylib

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.payze.paylib.databinding.WebViewPageBinding

class WebViewDialog : DialogFragment() {

    private val urlAddress by lazy {
        arguments?.getString(KEY_URL)
    }
    private var _binding: WebViewPageBinding? = null
    private val binding get() = _binding!!
    var isSuccessful = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullscreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WebViewPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webView.stopLoading()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configWenView()
        if (urlAddress != null)
            binding.webView.loadUrl(urlAddress!!)
        else
            dismiss()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configWenView() {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    if (catchRedirect(request?.url.toString())) return true
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }
        }
    }

    private fun catchRedirect(url: String?): Boolean {

        val keyUrlPart = KEY_WORD

        if (url?.contains(keyUrlPart, true) == true) {
            closeWithSuccess()
        }
        return false
    }

    private fun closeWithSuccess() {
        isSuccessful = true
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener?.onDismiss(isSuccessful)
        super.onDismiss(dialog)
    }

    interface DialogCallBack {
        fun onDismiss(success: Boolean)
    }

    companion object {
        const val KEY_URL = "URL_KEY"
        const val KEY_WORD = "payze"

        var listener: DialogCallBack? = null

        fun instance(url: String): WebViewDialog {
            return WebViewDialog().apply {
                arguments = bundleOf(KEY_URL to url)
            }
        }
    }
}