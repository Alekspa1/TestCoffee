package com.drag0n.testcoffee.data.sharedPreferense

import android.content.Context
import com.drag0n.testcoffee.domain.model.User
import com.google.gson.Gson
import androidx.core.content.edit
import javax.inject.Inject


class SharedPreferenseImp @Inject constructor(context: Context) : SharedPreferenseRepository {
    private val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun saveAuthorization(user: User) {
        val json = gson.toJson(user)
        sharedPref.edit { putString("USER", json) }
    }

    override fun deleteAuthorization() {
        sharedPref.edit { remove("USER") }
    }

    override fun isAuthorization(): Boolean {
        val json = sharedPref.getString("USER", null)
         val user = gson.fromJson(json, User::class.java) ?: User("", "")
        return  (!user.login.isEmpty() && !user.password.isEmpty())
    }
}