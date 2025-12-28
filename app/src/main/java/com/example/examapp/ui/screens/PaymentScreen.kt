package com.example.examapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.examapp.viewmodel.ProductViewModel
import kotlinx.coroutines.delay
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: ProductViewModel
) {
    val scrollState = rememberScrollState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var paymentSuccess by remember { mutableStateOf(false) }

    val isExpiryValid = isExpiryDateValid(expiry)

    val isFormValid =
        firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                phone.length == 8 &&
                address.isNotBlank() &&
                cardNumber.length == 16 &&
                cvv.length == 3 &&
                isExpiryValid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.navigate("cart") }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Payment", style = MaterialTheme.typography.h5)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Personal Information", style = MaterialTheme.typography.subtitle1)

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )


                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = phone,

                    onValueChange = {
                        if (it.length <= 8 && it.all { c -> c.isDigit() }) phone = it
                    },
                    label = { Text("Phone number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Card Information", style = MaterialTheme.typography.subtitle1)

                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = {
                        if (it.length <= 16 && it.all { c -> c.isDigit() }) cardNumber = it
                    },
                    label = { Text("Card number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = expiry,
                    onValueChange = { expiry = formatExpiryDate(it) },
                    label = { Text("Expiry (MM/YY)") },
                    isError = expiry.isNotEmpty() && !isExpiryValid,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                if (expiry.isNotEmpty() && !isExpiryValid) {
                    Text(
                        text = "Card expired",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption
                    )
                }

                OutlinedTextField(
                    value = cvv,
                    onValueChange = {
                        if (it.length <= 3 && it.all { c -> c.isDigit() }) cvv = it
                    },
                    label = { Text("CVV") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { showDialog = true },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pay securely")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }


    if (showDialog) {
        Dialog (onDismissRequest = {}) {
            Card(
                elevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .wrapContentHeight()
                    .padding(12.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {

                    if (!paymentSuccess) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("Processing payment...")
                    } else {
                        Text("Payment successful ✅ \n Your order will be delivered in the next 24-48 hour ")
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.clearCart()
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back to Home")
                        }
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            delay(2000)
            paymentSuccess = true
        }
    }

}

/* ---------- Helpers ---------- */

private fun formatExpiryDate(input: String): String {
    val digits = input.filter { it.isDigit() }
    return when {
        digits.length <= 2 -> digits
        digits.length <= 4 -> digits.substring(0, 2) + "/" + digits.substring(2)
        else -> digits.substring(0, 2) + "/" + digits.substring(2, 4)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun isExpiryDateValid(expiry: String): Boolean {
    return try {
        if (expiry.length != 5) return false
        val formatter = DateTimeFormatter.ofPattern("MM/yy")
        val expiryDate = YearMonth.parse(expiry, formatter)
        val currentDate = YearMonth.now()
        !expiryDate.isBefore(currentDate)
    } catch (e: Exception) {
        false
    }
}
