package com.seda.navigationcompose.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seda.navigationcompose.R
import com.seda.navigationcompose.components.FormattedPriceLabel
import com.seda.navigationcompose.data.OrderUiState

@Composable
fun OrderSummaryScreen(orderUiState: OrderUiState,
                       onCancelButtonClicked:()->Unit,
                       onSendButtonClicked:(String,String)->Unit,
                       modifier: Modifier=Modifier ){

    val resources = LocalContext.current.resources
    val numberOfCupcakes = resources.getQuantityString(
        R.plurals.cupcakes,
        orderUiState.quantity,
        orderUiState.quantity
    )
    val orderSummary = stringResource(R.string.order_details,
        numberOfCupcakes,
        orderUiState.flavor,
        orderUiState.date,
        orderUiState.quantity)

    val items = listOf(
        Pair(stringResource(R.string.quantity),numberOfCupcakes),
        Pair(stringResource(R.string.flavor),orderUiState.flavor),
        Pair(stringResource(R.string.pickup_date),orderUiState.date)
    )
    val newOrder = stringResource(R.string.new_cupcake_order)

   Column(modifier = modifier.padding(16.dp),
           verticalArrangement = Arrangement.spacedBy(8.dp)) {

           items.forEach { item->
               Text(item.first.uppercase())
               Text(text = item.second, fontWeight = FontWeight.Bold)
               Divider(thickness = 1.dp)
           }
       Spacer(modifier = Modifier.height(8.dp))
 FormattedPriceLabel(subtotal = orderUiState.price, modifier = Modifier.align(Alignment.End))
Button(modifier=Modifier.fillMaxWidth(), onClick = {onSendButtonClicked(newOrder,orderSummary)}) {
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



@Preview
@Composable
fun OrderSummaryPreview(){
    OrderSummaryScreen(
        orderUiState = OrderUiState(0, "Test", "Test", "$300.00"),
        onSendButtonClicked = { subject: String, summary: String -> },
        onCancelButtonClicked = {}
    )
}