package com.example.examapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.examapp.data.model.Product
import com.example.examapp.viewmodel.ProductViewModel
import com.example.examapp.R

@Composable
fun CartScreen(
    viewModel: ProductViewModel,
    navController: NavController
) {
    val cartItems = viewModel.cart.collectAsState().value
    val totalPrice = cartItems.sumOf {
        it.product.price * it.quantity
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "My Cart",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )
            }

            if (cartItems.isNotEmpty()) {
                IconButton(onClick = { viewModel.clearCart() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear cart"
                    )
                }
            }
        }

        if (cartItems.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your cart is empty,\nlet's go shopping 🛍️",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(onClick = { navController.navigate("home") }) {
                    Text("Go to Home")
                }
            }
        } else {

            Column(modifier = Modifier.fillMaxSize()) {

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(cartItems) { item ->
                        CartItem(
                            product = item.product,
                            quantity = item.quantity,
                            onIncrease = { viewModel.increaseQuantity(item) },
                            onDecrease = { viewModel.decreaseQuantity(item) },
                            onRemove = { viewModel.removeFromCart(item) }
                        )
                    }
                }

                // BOUTON PAY
                Button(
                    onClick = { navController.navigate("payment") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    enabled = cartItems.isNotEmpty()
                ) {
                    Text(
                        text = "Pay ${"%.2f".format(totalPrice)} $",
                        style = MaterialTheme.typography.h6
                    )
                }

                // ⭐ NOTICE LIVRAISON GRATUITE
                Text(
                    text = "🚚 Free delivery on all orders !",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun CartItem(
    product: Product,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = rememberAsyncImagePainter(product.image),
                contentDescription = product.title,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(product.title, maxLines = 2)
                Text("${product.price} $", style = MaterialTheme.typography.body2)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease) {
                    Icon(painter = painterResource(R.drawable.decrease_24), contentDescription = "Decrease")
                }
                Text(quantity.toString())

                IconButton(onClick = onIncrease) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
            }

            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}
