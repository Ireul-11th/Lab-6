package com.example.lab6.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lab6.R
import com.example.lab6.data.OrderUiState

@Composable
fun SummaryScreen(
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val resources = LocalContext.current.resources

    val catQuantityText = resources.getQuantityString(
        R.plurals.cats,
        orderUiState.quantity,
        orderUiState.quantity
    )

    val orderSummary = stringResource(
        R.string.order_details,
        catQuantityText,
        orderUiState.catType,
        orderUiState.date,
        orderUiState.price
    )
    val newOrderTitle = stringResource(R.string.new_cat_order)

    val items = listOf(
        Pair(stringResource(R.string.quantity), catQuantityText),
        Pair(stringResource(R.string.cat_type), orderUiState.catType),
        Pair(stringResource(R.string.pickup_date), orderUiState.date)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { (label, value) ->
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                HorizontalDivider()
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.subtotal_price, orderUiState.price),
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Row(modifier = Modifier.padding(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSendButtonClicked(newOrderTitle, orderSummary) }
                ) {
                    Text(stringResource(R.string.send))
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}