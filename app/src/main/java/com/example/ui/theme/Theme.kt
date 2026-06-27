package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PremiumColorScheme = darkColorScheme(
    primary = NeonPurple,
    secondary = NeonPink,
    tertiary = AccentPurple,
    background = DeepBlack,
    surface = DarkCardBg,
    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite,
    error = GlowRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // We enforce dark theme for the premium look
    dynamicColor: Boolean = false, // Disable dynamic color to retain our distinct custom identity
    content: @Composable () -> Unit,
) {
    // We always use the custom PremiumColorScheme for this branding experience
    MaterialTheme(
        colorScheme = PremiumColorScheme,
        typography = Typography,
        content = content
    )
}
