package com.example.lab6.ui

import androidx.lifecycle.ViewModel
import com.example.lab6.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val PRICE_PER_CAT = 2500000.0
private const val PRICE_SAME_DAY_SURCHARGE = 500000.0

class OrderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState(pickupOptions = pickupOptions()))
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    fun setQuantity(quantity: Int) {
        _uiState.update { it.copy(quantity = quantity, price = calculatePrice(quantity = quantity)) }
    }

    fun setCatType(catType: String) {
        _uiState.update { it.copy(catType = catType) }
    }

    fun setDate(date: String) {
        _uiState.update { it.copy(date = date, price = calculatePrice(pickupDate = date)) }
    }

    fun resetOrder() {
        _uiState.value = OrderUiState(pickupOptions = pickupOptions())
    }

    private fun calculatePrice(
        quantity: Int = _uiState.value.quantity,
        pickupDate: String = _uiState.value.date
    ): String {
        var total = quantity * PRICE_PER_CAT
        if (pickupOptions()[0] == pickupDate) {
            total += PRICE_SAME_DAY_SURCHARGE
        }
        return formatVnd(total.toLong())
    }

    private fun formatVnd(amount: Long): String {
        val formatter = java.text.DecimalFormat("#,###")
        return "${formatter.format(amount)} VND"
    }

    private fun pickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("EEE, dd/MM", Locale("vi"))
        val calendar = Calendar.getInstance()
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }
}