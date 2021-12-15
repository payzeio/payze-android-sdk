package com.payze.paylib.network

data class PaymentResponse(
    val success: Boolean?,
    val url: String?,
    val threeDSIsPresent: Boolean?,
    val timestamp: String?,
    val status: Int?,
    val error: String?,
    val path: String?
)