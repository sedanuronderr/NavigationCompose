package com.seda.navigationcompose

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seda.navigationcompose.data.DataSource.flavors
import com.seda.navigationcompose.data.DataSource.quantityOptions
import com.seda.navigationcompose.fragments.OrderSummaryPreview
import com.seda.navigationcompose.fragments.OrderSummaryScreen


enum class CupcakeScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Flavor(title = R.string.choose_flavor),
    Pickup(title = R.string.choose_pickup_date),
    Summary(title = R.string.order_summary)
}
@Composable
fun CupcakeApp(  modifier: Modifier = Modifier,viewModel: OrderViewModel = viewModel(), navController: NavHostController = rememberNavController()){
    Scaffold { innerPadding->
val uiState by viewModel.uiState.collectAsState()
        NavHost(navController = navController, startDestination = CupcakeScreen.Start.name , modifier = modifier.padding(innerPadding) ){
            composable(route = CupcakeScreen.Start.name){
                StartOrderScreen(quantityOptions = quantityOptions,
                    onNextButtonClicked =
                    {
                    viewModel.setQuantity(it)
                        navController.navigate(CupcakeScreen.Flavor.name)
                })

            }
            composable(route = CupcakeScreen.Flavor.name) {
                val context = LocalContext.current

                SelectOptionScreen(subtotal = uiState.price,
                    onNextButtonClicked = {
                    navController.navigate(CupcakeScreen.Pickup.name)
                }, onCancelButtonClicked = {
                    cancelOrderAndNavigateToStart(viewModel,navController)
                }, options = flavors.map {
                        id-> context.resources.getString(id)
                                         },

                    onSelectionChanged = {
                    viewModel.setFlavor(it)
                })
            }
            composable(route = CupcakeScreen.Pickup.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel,navController)
                    },
                    options = uiState.pickupOptions,
                    onSelectionChanged = { viewModel.setDate(it) }
                )
            }

            composable(route = CupcakeScreen.Summary.name){
                val context =LocalContext.current
                  OrderSummaryScreen(
                      orderUiState = uiState,
                      onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel,navController) },
                      onSendButtonClicked = {subject :String,summary:String ->
                          shareOrder(context,subject=subject, summary = summary)

                      }
                  )



            }


        }

    }
}
private fun shareOrder(context: Context, subject: String, summary: String) {
    // Create an ACTION_SEND implicit intent with order details in the intent extras
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_cupcake_order)
        )
    )
}
private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
       viewModel.resetOrder()
    navController.popBackStack(CupcakeScreen.Start.name, inclusive = false)
}


