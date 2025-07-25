package com.drag0n.testcoffee.domain.model

data class UserRegisterOrAuthorization(
    val token: String,
    val tokenLifeTime: String
)