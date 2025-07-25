package com.drag0n.testcoffee.presentation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.drag0n.testcoffee.R


@Composable
fun LocationScreen() {
    val model: ViewModelCoffee = hiltViewModel()
    val showDialog by model.showLocationDialog.collectAsStateWithLifecycle()
    if (showDialog) {
        LocationSettingsDialog(
            onDismiss = { model.hideLocationDialog() },
            onConfirm = { model.openLocationSettings() }
        )
    }
}

@Composable
fun LocationSettingsDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_location),
                contentDescription = null
            )
        },
        title = {
            Text("Проверка местоположения")
        },
        text = {
            Text("Местоположение выключено, хотите включить?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    Log.d("MyLog", "да")
                }
            ) {
                Text("Да")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    Toast.makeText(
                        context,
                        "Тогда вы не сможете получать информацию о кофейнях",
                        Toast.LENGTH_SHORT
                    ).show()
                    onDismiss()
                }
            ) {
                Text("Нет")
            }
        }
    )
}

