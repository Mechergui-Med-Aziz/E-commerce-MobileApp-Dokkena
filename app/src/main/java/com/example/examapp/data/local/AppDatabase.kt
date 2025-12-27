package com.example.examapp.data.local



import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.examapp.data.model.Product

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
