package com.payze.paylib.network

data class PaymentRequest(
    val number: String,
    val cardHolder: String,
    val expirationDate: String,
    val securityNumber: String,
    val transactionId: String,
    val billingAddress: String = ""
)