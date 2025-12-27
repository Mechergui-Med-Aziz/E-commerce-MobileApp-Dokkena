package com.example.examapp.repository

import com.example.examapp.data.local.ProductDao
import com.example.examapp.data.model.Product
import com.example.examapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val dao: ProductDao) {

    val products: Flow<List<Product>> = dao.getAllProducts()

    suspend fun fetchAndSaveProducts() {
        val remoteProducts = RetrofitInstance.api.getProducts()
        dao.insertProducts(remoteProducts)
    }
}
