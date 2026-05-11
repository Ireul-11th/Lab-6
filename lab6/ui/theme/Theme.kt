package com.example.lab6.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CafeColorScheme = lightColorScheme(
    primary = CoffeePrimary,
    onPrimary = CoffeeOnPrimary,
    primaryContainer = CoffeePrimaryContainer,
    onPrimaryContainer = CoffeeOnPrimaryContainer,
    secondary = CoffeeSecondary,
    onSecondary = CoffeeOnSecondary,
    secondaryContainer = CoffeeSecondaryContainer,
    onSecondaryContainer = CoffeeOnSecondaryContainer,
    tertiary = CoffeeTertiary,
    onTertiary = CoffeeOnTertiary,
    tertiaryContainer = CoffeeTertiaryContainer,
    onTertiaryContainer = CoffeeOnTertiaryContainer,
    background = CoffeeBackground,
    onBackground = CoffeeOnBackground,
    surface = CoffeeSurface,
    onSurface = CoffeeOnSurface,
    surfaceVariant = CoffeeSurfaceVariant,
    onSurfaceVariant = CoffeeOnSurfaceVariant,
    outline = CoffeeOutline
)

@Composable
fun Lab6Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CafeColorScheme,
        typography = Typography,
        content = content
    )
}