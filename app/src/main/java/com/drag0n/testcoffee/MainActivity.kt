package com.drag0n.testcoffee

import android.Manifest
import android.os.Bundle
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
import com.drag0n.testcoffee.domain.model.CoffeeShopItem
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
        LocationScreen()
        if (model.chekPermissionLocation()) model.chekLocation()
        else pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        model.myGeo.asLiveData().observe(this) {

        }
        val sampleShops = listOf(
            CoffeeShopItem("BEDOEV COFFEE", "1 KM or Bac", randomPoint()),
            CoffeeShopItem("Coffee Like", "2 KM or Bac", randomPoint()),
            CoffeeShopItem("EMBDI Coffee and Snacks", "1 KM or Bac", randomPoint()),
            CoffeeShopItem("Kodobe ecrb", "300 M or Bac", randomPoint()),
            CoffeeShopItem("BEDOEV COFFEE 2", "3 KM or Bac", randomPoint())
        )
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
                    sampleShops,
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







