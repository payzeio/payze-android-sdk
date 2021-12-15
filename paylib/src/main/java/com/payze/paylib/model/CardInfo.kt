package com.payze.paylib.model

data class CardInfo(
    val number: String,
    val cardHolder: String,
    val expirationDate: String,
    val securityNumber: String
)
