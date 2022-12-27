package com.seda.navigationcompose

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
        NavHost(navController = navController, startDestination =CupcakeScreen.Start.name , modifier = modifier.padding(innerPadding) ){
            composable(route = CupcakeScreen.Start.name){
                StartOrderScreen(quantityOptions = quantityOptions,
                    onNextButtonClicked =
                    {
                        navController.navigate(CupcakeScreen.Flavor.name)
                })

            }
            composable(route = CupcakeScreen.Flavor.name) {
                val context = LocalContext.current

                SelectOptionScreen(subtotal = uiState.price, onNextButtonClicked = {
                    navController.navigate(CupcakeScreen.Pickup.name)
                }, onCancelButtonClicked = {
                    cancelOrderAndNavigateToStart(navController)
                }, options = flavors.map {
                        id-> context.resources.getString(id)
                                         },

                    onSelectionChanged = {
                    viewModel.setFlavor(it)
                })
            }
        }

    }
}
private fun cancelOrderAndNavigateToStart(

    navController: NavHostController
) {

    navController.popBackStack(CupcakeScreen.Start.name, inclusive = false)
}


