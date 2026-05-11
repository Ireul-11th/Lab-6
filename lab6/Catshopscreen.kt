package com.example.lab6

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lab6.data.DataSource
import com.example.lab6.ui.OrderViewModel
import com.example.lab6.ui.SelectOptionScreen
import com.example.lab6.ui.StartOrderScreen
import com.example.lab6.ui.SummaryScreen

enum class CatShopScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    CatType(title = R.string.choose_cat_type),
    Pickup(title = R.string.choose_pickup_date),
    Summary(title = R.string.order_summary)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatShopAppBar(
    currentScreen: CatShopScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun CatShopApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CatShopScreen.valueOf(
        backStackEntry?.destination?.route ?: CatShopScreen.Start.name
    )

    Scaffold(
        topBar = {
            CatShopAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = CatShopScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = CatShopScreen.Start.name) {
                StartOrderScreen(
                    quantityOptions = DataSource.quantityOptions,
                    onNextButtonClicked = {
                        viewModel.setQuantity(it)
                        navController.navigate(CatShopScreen.CatType.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }

            composable(route = CatShopScreen.CatType.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = DataSource.catTypes,
                    onSelectionChanged = { viewModel.setCatType(it) },
                    onNextButtonClicked = { navController.navigate(CatShopScreen.Pickup.name) },
                    onCancelButtonClicked = { cancelAndGoToStart(viewModel, navController) },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            composable(route = CatShopScreen.Pickup.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = uiState.pickupOptions,
                    onSelectionChanged = { viewModel.setDate(it) },
                    onNextButtonClicked = { navController.navigate(CatShopScreen.Summary.name) },
                    onCancelButtonClicked = { cancelAndGoToStart(viewModel, navController) },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            composable(route = CatShopScreen.Summary.name) {
                val context = LocalContext.current
                SummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = { cancelAndGoToStart(viewModel, navController) },
                    onSendButtonClicked = { subject, summary ->
                        shareOrder(context, subject, summary)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

private fun cancelAndGoToStart(viewModel: OrderViewModel, navController: NavHostController) {
    viewModel.resetOrder()
    navController.popBackStack(CatShopScreen.Start.name, inclusive = false)
}

private fun shareOrder(context: android.content.Context, subject: String, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.new_cat_order)))
}