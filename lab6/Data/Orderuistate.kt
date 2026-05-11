package com.example.lab6.data

data class OrderUiState(
    val quantity: Int = 0,
    val catType: String = "",
    val date: String = "",
    val price: String = "",
    val pickupOptions: List<String> = listOf()
)