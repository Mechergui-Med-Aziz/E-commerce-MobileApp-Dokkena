package com.example.examapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.examapp.data.model.Product
import com.example.examapp.viewmodel.ProductViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(
    product: Product,
    viewModel: ProductViewModel,
    navController: NavController
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val cartItems = viewModel.cart.collectAsState().value

    Scaffold(
        snackbarHost = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                SnackbarHost(hostState = snackbarHostState)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text("Product details")
            }

            Spacer(Modifier.height(16.dp))

            Image(
                painter = rememberAsyncImagePainter(product.image),
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(Modifier.height(16.dp))
            Text(product.title)
            Spacer(Modifier.height(8.dp))
            Text("${product.price} $")
            Spacer(Modifier.height(8.dp))
            Text(product.description)

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val alreadyInCart =
                        cartItems.any { it.product.id == product.id }

                    if (alreadyInCart) {
                        scope.launch { snackbarHostState.showSnackbar("Product already in cart") }
                    } else {
                        viewModel.addToCart(product)
                        scope.launch { snackbarHostState.showSnackbar("Product added to cart") }
                    }
                }
            ) {
                Text("Add to Cart")
            }
        }
    }
}
