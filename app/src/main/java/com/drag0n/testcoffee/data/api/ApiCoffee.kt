package com.drag0n.testcoffee.data.api

import com.drag0n.testcoffee.domain.model.CoffeeShopList
import com.drag0n.testcoffee.domain.model.MenuCoffeeShop
import com.drag0n.testcoffee.domain.model.User
import com.drag0n.testcoffee.domain.model.UserRegisterOrAuthorization
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiCoffee {

    @POST("auth/register")
    suspend fun authRegister(
        @Body authDto: User
    ): Response<UserRegisterOrAuthorization>

    @POST("auth/login")
    suspend fun authLogin(
        @Body authDto: User
    ): Response<UserRegisterOrAuthorization>


    @GET("locations")
    suspend fun getCoffeeShops(
        @Header ("Authorization") token: String,
    ): Response<CoffeeShopList>

    @GET("/location/{id}/menu")
    suspend fun getMenuCoffeeShops(
        @Header ("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<MenuCoffeeShop>


}