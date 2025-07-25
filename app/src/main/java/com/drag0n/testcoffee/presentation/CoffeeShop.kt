package com.drag0n.testcoffee.presentation

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.drag0n.testcoffee.domain.model.CoffeeShopItem
import com.drag0n.testcoffee.ui.theme.cardCoffee
import com.drag0n.testcoffee.ui.theme.input


@Composable
fun CoffeeShopItem(shop: CoffeeShopItem, model: ViewModelCoffee) {
    val currentLocation by model.myGeo.collectAsStateWithLifecycle()
    val distanceText = remember(currentLocation) {
        when {
            currentLocation == null -> "Определение вашего местоположения..."
            else -> {
                val distance = distanceBetween(
                    currentLocation!!.latitude.toDouble(),
                    currentLocation!!.longitude.toDouble(),
                    shop.point.latitude.toDouble(),
                    shop.point.longitude.toDouble()
                )
                "${formatDistance(distance)} от вас"
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = cardColors(cardCoffee)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = shop.id,
                fontSize = 30.sp,
                color = input,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = distanceText,
                fontSize = 14.sp,
                color = input
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeShopList(
    shops: List<CoffeeShopItem>,
    onNavigate: () -> Unit,
    text: String,
    model: ViewModelCoffee
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.widthIn(max = 280.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { model.deleteUser()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(shops) { shop ->
                CoffeeShopItem(shop, model)
            }
        }
    }
}

fun distanceBetween(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
    val results = FloatArray(1)
    Location.distanceBetween(lat1, lon1, lat2, lon2, results)
    return results[0].toInt()
}

fun formatDistance(distanceMeters: Int): String {
    return if (distanceMeters >= 1000) {
        val distanceKm = distanceMeters / 1000f
        "%.1f км".format(distanceKm)
    } else {
        "$distanceMeters м"
    }
}
