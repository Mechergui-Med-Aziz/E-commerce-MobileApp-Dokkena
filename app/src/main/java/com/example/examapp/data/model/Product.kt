package com.example.examapp.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
    data class Product(
        @PrimaryKey val id: Int,
        val title: String,
        val description: String,
        val price: Double,
        val image: String
    )
