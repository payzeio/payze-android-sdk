package com.payze.paylib.network

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class PaymentRequest(
    @Json(name = "number")
    val number: String,
    @Json(name = "cardHolder")
    val cardHolder: String,
    @Json(name = "expirationDate")
    val expirationDate: String,
    @Json(name = "securityNumber")
    val securityNumber: String,
    @Json(name = "transactionId")
    val transactionId: String,
)