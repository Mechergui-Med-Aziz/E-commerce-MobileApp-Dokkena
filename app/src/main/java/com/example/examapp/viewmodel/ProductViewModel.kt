package com.example.examapp.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examapp.data.model.CartItem
import com.example.examapp.data.model.Product
import com.example.examapp.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart = _cart.asStateFlow()

    init {
        viewModelScope.launch {
            repository.fetchAndSaveProducts()
            repository.products.collect { _products.value = it }
        }
    }

    fun addToCart(product: Product): Boolean {
        val current = _cart.value.toMutableList()

        val exists = current.any { it.product.id == product.id }

        return if (exists) {
            false
        } else {
            current.add(CartItem(product, 1))
            _cart.value = current
            true
        }
    }


    fun increaseQuantity(item: CartItem) {
        _cart.value = _cart.value.map {
            if (it.product.id == item.product.id)
                it.copy(quantity = it.quantity + 1)
            else it
        }
    }

    fun decreaseQuantity(item: CartItem) {
        _cart.value = _cart.value.mapNotNull {
            if (it.product.id == item.product.id) {
                if (it.quantity > 1) it.copy(quantity = it.quantity - 1)
                else null
            } else it
        }
    }

    fun removeFromCart(item: CartItem) {
        _cart.value = _cart.value.filter {
            it.product.id != item.product.id
        }
    }
    fun clearCart() {
        _cart.value = emptyList()
    }

}
