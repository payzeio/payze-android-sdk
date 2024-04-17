package com.payze.paylib.network

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
class TransactionStatusResponse(
    @Json(name = "status")
    val status: TransactionStatus
)

@Keep
enum class TransactionStatus(value: String) {

    @Json(name = "Created")
    CREATED("Created"),

    @Json(name = "Success")
    SUCCESS("Success"),

    @Json(name = "Failure")
    FAILURE("Failure")
}