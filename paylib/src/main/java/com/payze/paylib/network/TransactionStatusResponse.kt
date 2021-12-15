package com.payze.paylib.network

import com.squareup.moshi.Json

class TransactionStatusResponse(
    val status: TransactionStatus
)

enum class TransactionStatus(value: String) {

    @Json(name = "Created")
    CREATED("Created"),

    @Json(name = "Success")
    SUCCESS("Success"),

    @Json(name = "Failure")
    FAILURE("Failure")
}