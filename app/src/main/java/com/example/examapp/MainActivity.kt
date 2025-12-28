package com.example.examapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.examapp.data.local.AppDatabase
import com.example.examapp.repository.ProductRepository
import com.example.examapp.ui.screens.HomeScreen
import com.example.examapp.ui.screens.ProductDetailScreen
import com.example.examapp.viewmodel.ProductViewModel
import com.example.examapp.ui.screens.CartScreen
import com.example.examapp.ui.screens.PaymentScreen
import com.example.examapp.ui.screens.WelcomeScreen

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_db"
        ).build()

        val repository = ProductRepository(db.productDao())
        val viewModel = ProductViewModel(repository)

        setContent {
            val navController = rememberNavController()

            MaterialTheme {
                Surface {
                    NavHost(navController, startDestination = "welcome") {

                        composable("home") {
                            HomeScreen(viewModel, navController)
                        }
                        composable("welcome") {
                            WelcomeScreen( navController)
                        }

                        composable("cart") {
                            CartScreen(viewModel, navController)
                        }
                        composable("payment"){
                            PaymentScreen(navController,viewModel)
                        }

                        composable(
                            "details/{productId}",
                            arguments = listOf(navArgument("productId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("productId") ?: 0
                            val product = viewModel.products.collectAsState().value.first { it.id == id }
                            ProductDetailScreen(product, viewModel, navController)
                        }
                    }

                }
            }
        }

    }
}


@Composable
fun AppNavGraph(viewModel: ProductViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(viewModel = viewModel, navController = navController)
        }
        composable("cart") {
            CartScreen(viewModel = viewModel, navController = navController)
        }
    }
}

