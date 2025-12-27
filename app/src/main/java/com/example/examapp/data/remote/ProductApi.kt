package com.example.examapp.data.remote


import com.example.examapp.data.model.Product
import retrofit2.http.GET

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<Product>
}
