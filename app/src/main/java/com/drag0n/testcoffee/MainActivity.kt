package com.drag0n.testcoffee

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.drag0n.testcoffee.domain.model.Point
import com.drag0n.testcoffee.presentation.Authorization
import com.drag0n.testcoffee.presentation.CenteredRegistrationScreen
import com.drag0n.testcoffee.presentation.CoffeeShopList
import com.drag0n.testcoffee.presentation.LocationScreen
import com.drag0n.testcoffee.presentation.ViewModelCoffee
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


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
        val cofeeShop by model.CoffeeShopsFlow.collectAsStateWithLifecycle()
        LocationScreen()
        if (model.chekPermissionLocation()) model.chekLocation()
        else pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        model.myGeo.asLiveData().observe(this) {

        }

        NavHost(
            navController = navController, startDestination =
                if (model.isAuthorization()) "CoffeeShop"
                else "Registation"

        ) {
            composable("Registation") {
                CenteredRegistrationScreen(
                    { navController.navigate("CoffeeShop") },
                    text = "Регистрация", model
                )


            }
            composable("Authorization") {
                Authorization({ navController.navigate("CoffeeShop") }, text = "Авторизация", model)
            }

            composable("CoffeeShop") {
                model.getCoffeeShops()
                CoffeeShopList(
                    { navController.navigate("Registation") },
                    text = "Близжайшие кофейни",
                    model
                )
            }
        }

    }

    fun randomPoint(): Point {
        val moscowLatRange = 55.5..55.9
        val moscowLonRange = 37.3..37.8

        return Point(
           Random.nextDouble(moscowLatRange.start, moscowLatRange.endInclusive).toString(),
            Random.nextDouble(moscowLonRange.start, moscowLonRange.endInclusive).toString()
        )
    }


}







