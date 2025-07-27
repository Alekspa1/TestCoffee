package com.drag0n.testcoffee.data.sharedPreferense

import com.drag0n.testcoffee.domain.model.User

interface SharedPreferenseRepository {
    fun saveAuthorization(user: User)
    fun deleteAuthorization()
    fun isAuthorization(): Boolean
    fun savetoken(token: String)
    fun gettoken() : String
}