package com.example.examapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.examapp.data.model.Product
import com.example.examapp.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: ProductViewModel, navController: NavController) {

    val products by viewModel.products.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var search by remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,

        topBar = {
            TopAppBar(
                elevation = 6.dp,
                backgroundColor = Color.White,
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Dokkena")
                    }
                }
            )
        },

        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    snackbarData = data,
                    modifier = Modifier.padding(top = 10.dp),
                    backgroundColor = Color(0xFF323232)
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // 🔍 BARRE DE RECHERCHE (padding réduit)
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                label = { Text("Search products…") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                singleLine = true,
                shape = MaterialTheme.shapes.large
            )

            val filtered = products.filter {
                it.title.contains(search, ignoreCase = true)
            }

            // LISTE PRODUITS
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .statusBarsPadding()
            ) {
                items(filtered) { product ->
                    ProductCard(
                        product = product,
                        onAddToCart = {
                            val added = viewModel.addToCart(product)

                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    if (added) "Product added to cart"
                                    else "Product already in cart"
                                )
                            }
                        },
                        onClick = { navController.navigate("details/${product.id}") }
                    )
                }
            }

            // BARRE DU BAS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F7F7))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
                IconButton(onClick = { navController.navigate("cart") }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        elevation = 6.dp,
        backgroundColor = Color.White,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = rememberAsyncImagePainter(product.image),
                contentDescription = product.title,
                modifier = Modifier
                    .size(85.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    product.title,
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    "${product.price} $",
                    style = MaterialTheme.typography.body2.copy(color = Color(0xFF4CAF50))
                )
            }

            Button(
                onClick = onAddToCart,
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Add")
            }
        }
    }
}
