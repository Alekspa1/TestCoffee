package com.drag0n.testcoffee.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.drag0n.testcoffee.domain.model.User
import com.drag0n.testcoffee.ui.theme.input
import com.drag0n.testcoffee.ui.theme.inputButton
import com.drag0n.testcoffee.ui.theme.textButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredRegistrationScreen(onNavigate: NavHostController, text: String, model: ViewModelCoffee) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            color = input,
                            fontSize = 25.sp,
                            modifier = Modifier.widthIn(max = 280.dp)
                        )
                    }
                },

                )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegistrationScreen(onNavigate, model)
        }
    }

}


@Composable
fun RegistrationScreen(onNavigate: NavHostController, model: ViewModelCoffee) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        val emailState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }
        val confirmPasswordState = remember { mutableStateOf("") }

        val isEmailValid = emailState.value.contains("@") && emailState.value.contains(".")
        val passwordsMatch = passwordState.value == confirmPasswordState.value
        val isPasswordValid = passwordState.value.isNotEmpty()

        if (emailState.value.isNotEmpty() && !isEmailValid) {
            Text("Введите корректный email", color = Color.Red)
        }

        Text(
            text = "E-mail", modifier = Modifier.padding(bottom = 4.dp),
            color = input
        )
        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = input
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Пароль", modifier = Modifier.padding(bottom = 4.dp),
            color = input
        )
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = input
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Повторите пароль",
            modifier = Modifier.padding(bottom = 4.dp),
            color = input
        )
        OutlinedTextField(
            value = confirmPasswordState.value,
            onValueChange = { confirmPasswordState.value = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = input
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            enabled = isEmailValid && passwordsMatch && isPasswordValid,
            onClick = {
                if (isEmailValid && passwordsMatch && isPasswordValid) {
                    val user = User(emailState.value, passwordState.value)
                    model.authRegister(user)
                    onNavigate.navigate("CoffeeShop")
                }
            },
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = inputButton,
                contentColor = textButton,
                disabledContainerColor = inputButton.copy(alpha = 0.5f),
                disabledContentColor = textButton.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Регистрация")
        }
        Button(
            onClick = { onNavigate.navigate("Authorization")
            },
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = inputButton,
                contentColor = textButton,
            ),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(48.dp)

        ) {
            Text("Авторизоваться")
        }
    }
}