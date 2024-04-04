package com.payze.paylib.network

import com.squareup.moshi.Json

data class PaymentResponse(
    @Json(name = "success")
    val success: Boolean?,
    @Json(name = "url")
    val url: String?,
    @Json(name = "threeDSIsPresent")
    val threeDSIsPresent: Boolean?,
    @Json(name = "timestamp")
    val timestamp: String?,
    @Json(name = "status")
    val status: Int?,
    @Json(name = "error")
    val error: String?,
    @Json(name = "path")
    val path: String?
)