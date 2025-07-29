package com.drag0n.testcoffee.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.drag0n.testcoffee.domain.model.CartItem
import com.drag0n.testcoffee.domain.model.MenuCoffeeShopItem
import com.drag0n.testcoffee.ui.theme.input
import com.drag0n.testcoffee.ui.theme.inputButton
import com.drag0n.testcoffee.ui.theme.price
import com.drag0n.testcoffee.ui.theme.textButton


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuCoffeeShopItem(
    menu: MenuCoffeeShopItem,
    model: ViewModelCoffee,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column {
            GlideImage(
                model = menu.imageURL,
                contentDescription = menu.name,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(Modifier.padding(8.dp)) {
                Text(
                    text = menu.name,
                    fontSize = 16.sp,
                    color = input
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${menu.price} руб.",
                        fontSize = 14.sp,
                        color = price,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (quantity > 0) {
                                    onRemove()
                                }
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Close, "Уменьшить")
                        }

                        Text(
                            text = quantity.toString(),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = { onAdd() }, // Вызываем функцию
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Add, "Добавить")
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuCoffeeShops(
    onNavigate: NavHostController,
    text: String,
    model: ViewModelCoffee
) {
    val menucofeeShop by model.menuCoffeeShopsFlow.collectAsStateWithLifecycle()
    val cartItems by model.cartItems.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.weight(1f),
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
                        IconButton(onClick = { onNavigate.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    }
                )
            },
            bottomBar = {
                Button(
                    onClick = {
                        onNavigate.currentBackStackEntry?.savedStateHandle?.set(
                            "cart_items",
                            cartItems.map { CartItem(it.key, it.value) }
                        )
                        onNavigate.navigate("checkout_screen")
                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = inputButton,
                        contentColor = textButton,
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                ) {
                    Text("Перейти к оплате")
                }
            }
        ) { innerPadding ->
            if (menucofeeShop != null) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(menucofeeShop as List<Any?>) { menu ->
                        val item = menu as MenuCoffeeShopItem
                        val quantity = cartItems[item] ?: 0

                        MenuCoffeeShopItem(
                            menu = item,
                            model = model,
                            quantity = quantity,
                            onAdd = { model.addToCart(item) },
                            onRemove = { model.removeFromCart(item) }
                        )
                    }
                }
            }
        }
    }
}







