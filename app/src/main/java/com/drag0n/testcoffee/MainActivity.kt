package com.drag0n.testcoffee

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.drag0n.testcoffee.presentation.Authorization
import com.drag0n.testcoffee.presentation.CenteredRegistrationScreen
import com.drag0n.testcoffee.presentation.CheckoutScreen
import com.drag0n.testcoffee.presentation.CoffeeShopList
import com.drag0n.testcoffee.presentation.LocationScreen
import com.drag0n.testcoffee.presentation.MenuCoffeeShops
import com.drag0n.testcoffee.presentation.ViewModelCoffee
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var pLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        setContent {
            Navigation()
        }
    }


    @Composable
    fun Navigation() {
        val model: ViewModelCoffee = hiltViewModel()
        val navController = rememberNavController()

        LocationScreen()
        if (model.chekPermissionLocation()) model.chekLocation()
        else pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        model.myGeo.asLiveData().observe(this) {

        }

        NavHost(
            navController = navController, startDestination = "Registation"

        ) {
            composable("Registation") {
                CenteredRegistrationScreen(
                     navController ,
                    text = "Регистрация", model
                )


            }
            composable("Authorization") {
                Authorization( navController, text = "Авторизация", model)
            }
            composable("CoffeeShop") {
                CoffeeShopList(
                    navController,
                    text = "Близжайшие кофейни",
                    model
                )
            }
            composable("MenuCoffeeShops") {
                MenuCoffeeShops(
                     navController ,
                    text = "Меню",
                    model
                )
            }

            composable("checkout_screen") {
                CheckoutScreen(navController,"Ваш заказ")
            }
        }

    }



}







