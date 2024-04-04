package com.payze.payapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.payze.payapp.databinding.ActivityMainBinding
import com.payze.paylib.Payze
import com.payze.paylib.model.CardInfo

class MainActivity : AppCompatActivity() {

    // should have one
    private val transactionID = "CFCBDAFB7F2D41EBA2E34E3EA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val payze = Payze(this)

        with(binding) {
            payBtn.setOnClickListener {
                payze.pay(
                    readCardData(),
                    transactionID,
                    onSuccess = ::onSuccess,
                    onError = ::onError
                )
            }
        }
    }

    private fun onSuccess() {
        Toast.makeText(this, getString(R.string.success_msg), Toast.LENGTH_LONG).show()
    }

    private fun onError(code: Int?, error: String?) {
        Toast.makeText(this, getString(R.string.error_msg, code, error), Toast.LENGTH_LONG).show()
    }

    private fun ActivityMainBinding.readCardData(): CardInfo {
        return CardInfo(
            number = cardNumber.text.toString(),
            cardHolder = cardHolder.text.toString(),
            expirationDate = cardExp.text.toString(),
            securityNumber = cardCvv.text.toString(),
        )
    }

}