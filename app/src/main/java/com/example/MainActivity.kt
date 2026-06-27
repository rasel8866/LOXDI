package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.*
import com.example.ui.components.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPink
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassBg
import com.example.ui.theme.TextWhite
import com.example.ui.theme.TextGray
import com.example.ui.viewmodel.IncomeTaskViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    private val viewModel: IncomeTaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppMain(viewModel)
            }
        }
    }
}

@Composable
fun AppMain(viewModel: IncomeTaskViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val isMaintenance = profile?.isMaintenanceMode == true

    // Immersive custom Maintenance blocker
    if (isMaintenance) {
        MaintenanceScreen(
            onBypassClicked = {
                viewModel.setMaintenanceMode(false)
            }
        )
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Crossfade(targetState = viewModel.currentScreen, label = "screen_nav") { screen ->
            when (screen) {
                Screen.Splash -> SplashScreen()
                Screen.Welcome -> WelcomeScreen(viewModel)
                Screen.Login -> UserLoginScreen(viewModel)
                Screen.Register -> RegisterScreen(viewModel)
                Screen.MainContainer -> MainContainerScreen(viewModel)
                Screen.ReferAndEarn -> ReferAndEarnScreen(viewModel)
                Screen.TaskDetailVisit -> TaskDetailVisitScreen(viewModel)
                Screen.TaskDetailWatch -> TaskDetailWatchScreen(viewModel)
                Screen.TaskDetailSurvey -> TaskDetailSurveyScreen(viewModel)
                Screen.History -> HistoryScreen(viewModel)
                Screen.WithdrawHistory -> WalletScreen(viewModel)
                Screen.Notifications -> NotificationsScreen(viewModel)
                Screen.Settings -> SettingsScreen(viewModel)
                Screen.AdminPanel -> AdminPanelScreen(viewModel)
            }
        }

        // Global Visual Notification Toast Dialog
        if (viewModel.showNotificationDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { viewModel.showNotificationDialog = false },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.82f)
                        .background(Color(0xFF1E1E24), RoundedCornerShape(20.dp))
                        .border(
                            1.dp,
                            if (viewModel.isSuccessNotification) Color(0xFF10B981) else NeonPink,
                            RoundedCornerShape(20.dp)
                        )
                        .padding(20.dp)
                        .clickable(enabled = false) {}
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (viewModel.isSuccessNotification) "💚" else "⚠️",
                            fontSize = 36.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = viewModel.notificationTitle,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = viewModel.notificationMessage,
                            color = TextGray,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        if (viewModel.isSuccessNotification) listOf(Color(0xFF10B981), Color(0xFF059669))
                                        else listOf(NeonPurple, NeonPink)
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.showNotificationDialog = false }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Okay",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
