package com.drag0n.testcoffee.data.api

import com.drag0n.testcoffee.domain.model.CoffeeShopList
import com.drag0n.testcoffee.domain.model.User
import com.drag0n.testcoffee.domain.model.UserRegisterOrAuthorization
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiCoffee {

    @POST("auth/register")
    suspend fun authRegister(
        @Body authDto: User
    ): Response<UserRegisterOrAuthorization>

    @POST("auth/login")
    suspend fun authLogin(
        @Body authDto: User
    ): Response<UserRegisterOrAuthorization>


    @GET("location")
    suspend fun getCoffeeShops(): Response<CoffeeShopList>


}