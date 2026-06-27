package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.BottomTab
import com.example.ui.viewmodel.IncomeTaskViewModel
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 1. Splash Screen
@Composable
fun SplashScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "rocket_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(DeepBlack, RichDark, DarkPurple.copy(alpha = 0.6f))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Glowing Purple/Pink Logo Card
            Box(
                modifier = Modifier
                    .size(120.dp * scale)
                    .shadow(24.dp, CircleShape, ambientColor = NeonPurple, spotColor = NeonPink)
                    .background(
                        Brush.linearGradient(GradientPurplePink),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.RocketLaunch,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "INCOME TASK",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Premium Earnings Network",
                color = NeonPink,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(80.dp))

            // Premium loader loop
            CircularProgressIndicator(
                color = NeonPurple,
                strokeWidth = 3.dp,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

// 2. Welcome Intro Screen
@Composable
fun WelcomeScreen(viewModel: IncomeTaskViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        // Futuristic Top Accent Glows
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(NeonPurple.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Welcome to",
                    color = TextGray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Income Task App",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Premium Edition",
                    color = NeonPink,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            // Real-time horizontal sliding banner
            BannerSlider()

            // Large Frosted Welcome Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "🚀 Earn rewards by completing micro-tasks, watching viral videos, taking expert surveys, and referring your network easily.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Instant cashouts via bKash, Nagad, and Rocket directly into your wallet.",
                        color = TextGray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }

            // Navigation Actions Block
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GradientButton(
                    text = "Sign In to Account",
                    onClick = { viewModel.currentScreen = Screen.Login },
                    modifier = Modifier.fillMaxWidth()
                )

                GlassBorderButton(
                    text = "Create New Account",
                    onClick = { viewModel.currentScreen = Screen.Register },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

// 3. User Sign In Screen
@Composable
fun UserLoginScreen(viewModel: IncomeTaskViewModel) {
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo & Header
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(NeonPurple.copy(alpha = 0.1f), CircleShape)
                    .border(1.dp, NeonPurple, CircleShape)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = null,
                    tint = NeonPurple,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome Back",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Secure login using your phone or email.",
                color = TextGray,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Login glass box form
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = viewModel.loginEmail,
                        onValueChange = { viewModel.loginEmail = it },
                        label = { Text("Email or Phone Number", color = TextGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassBg,
                            unfocusedContainerColor = GlassBg
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.Email, contentDescription = null, tint = NeonPurple)
                        }
                    )

                    OutlinedTextField(
                        value = viewModel.loginPassword,
                        onValueChange = { viewModel.loginPassword = it },
                        label = { Text("Enter Password", color = TextGray) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassBg,
                            unfocusedContainerColor = GlassBg
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.Key, contentDescription = null, tint = NeonPurple)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                                    contentDescription = null,
                                    tint = TextGray
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    GradientButton(
                        text = "Sign In Now",
                        onClick = { viewModel.login() },
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Rounded.Login
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Don't have an account?", color = TextGray, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Sign Up",
                    color = NeonPink,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.currentScreen = Screen.Register }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Back option
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { viewModel.currentScreen = Screen.Welcome },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Back to home", color = TextGray, fontSize = 13.sp)
            }
        }

        // Shimmer Overlay loader
        if (viewModel.isShimmerLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = NeonPurple)
            }
        }
    }
}

// 4. Registration Screen
@Composable
fun RegisterScreen(viewModel: IncomeTaskViewModel) {
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Create Account",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Join now to start earning instantly.",
                color = TextGray,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(28.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Full name
                    OutlinedTextField(
                        value = viewModel.regFullName,
                        onValueChange = { viewModel.regFullName = it },
                        label = { Text("Full Name", color = TextGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassBg,
                            unfocusedContainerColor = GlassBg
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.Person, contentDescription = null, tint = NeonPurple)
                        }
                    )

                    // Username
                    OutlinedTextField(
                        value = viewModel.regUsername,
                        onValueChange = { viewModel.regUsername = it },
                        label = { Text("Username", color = TextGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassBg,
                            unfocusedContainerColor = GlassBg
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.AlternateEmail, contentDescription = null, tint = NeonPurple)
                        }
                    )

                    // Phone
                    OutlinedTextField(
                        value = viewModel.regPhone,
                        onValueChange = { viewModel.regPhone = it },
                        label = { Text("Phone Number", color = TextGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassBg,
                            unfocusedContainerColor = GlassBg
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.Phone, contentDescription = null, tint = NeonPurple)
                        }
                    )

                    // Email
                    OutlinedTextField(
                        value = viewModel.regEmail,
                        onValueChange = { viewModel.regEmail = it },
                        label = { Text("Email Address", color = TextGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassBg,
                            unfocusedContainerColor = GlassBg
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.Email, contentDescription = null, tint = NeonPurple)
                        }
                    )

                    // Password
                    OutlinedTextField(
                        value = viewModel.regPassword,
                        onValueChange = { viewModel.regPassword = it },
                        label = { Text("Password", color = TextGray) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassBg,
                            unfocusedContainerColor = GlassBg
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.Lock, contentDescription = null, tint = NeonPurple)
                        }
                    )

                    // Confirm Password
                    OutlinedTextField(
                        value = viewModel.regConfirmPassword,
                        onValueChange = { viewModel.regConfirmPassword = it },
                        label = { Text("Confirm Password", color = TextGray) },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassBg,
                            unfocusedContainerColor = GlassBg
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.Shield, contentDescription = null, tint = NeonPurple)
                        }
                    )

                    // Referral Code
                    OutlinedTextField(
                        value = viewModel.regReferralCode,
                        onValueChange = { viewModel.regReferralCode = it },
                        label = { Text("Referral Code (Optional)", color = TextGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassBg,
                            unfocusedContainerColor = GlassBg
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.Group, contentDescription = null, tint = NeonPurple)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    GradientButton(
                        text = "Register Account",
                        onClick = { viewModel.register() },
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Rounded.AppRegistration
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Already have an account?", color = TextGray, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Login",
                    color = NeonPink,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.currentScreen = Screen.Login }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        if (viewModel.isShimmerLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = NeonPurple)
            }
        }
    }
}

// 5. Main Navigated Container (Hosts Bottom Tabs)
@Composable
fun MainContainerScreen(viewModel: IncomeTaskViewModel) {
    Scaffold(
        bottomBar = {
            PremiumBottomNav(
                activeTab = when (viewModel.currentTab) {
                    BottomTab.Home -> "Home"
                    BottomTab.Tasks -> "Tasks"
                    BottomTab.Wallet -> "Wallet"
                    BottomTab.Profile -> "Profile"
                },
                onTabSelected = { tab ->
                    viewModel.currentTab = when (tab) {
                        "Home" -> BottomTab.Home
                        "Tasks" -> BottomTab.Tasks
                        "Wallet" -> BottomTab.Wallet
                        "Profile" -> BottomTab.Profile
                        else -> BottomTab.Home
                    }
                }
            )
        },
        containerColor = DeepBlack
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (viewModel.currentTab) {
                BottomTab.Home -> HomeScreen(viewModel)
                BottomTab.Tasks -> TasksScreen(viewModel)
                BottomTab.Wallet -> WalletScreen(viewModel)
                BottomTab.Profile -> ProfileScreen(viewModel)
            }
        }
    }
}

// 6. Home Tab Screen
@Composable
fun HomeScreen(viewModel: IncomeTaskViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val tasksList by viewModel.tasks.collectAsState()

    // Filter available top premium tasks
    val availableTasks = tasksList.filter { it.status == "Available" }.take(3)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // User Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(Brush.linearGradient(GradientPurplePink), CircleShape)
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (profile?.name?.take(1) ?: "U").uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Assalamu Alaikum 👋",
                            color = TextGray,
                            fontSize = 11.sp
                        )
                        Text(
                            text = profile?.name ?: "Premium User",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                // Interactive Notifications Hub Badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0x13FFFFFF), CircleShape)
                        .clickable { viewModel.currentScreen = Screen.Notifications },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🔔", fontSize = 18.sp)
                    if (viewModel.notificationBadgeCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(NeonPink, CircleShape)
                                .align(Alignment.TopEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = viewModel.notificationBadgeCount.toString(),
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Premium Glass Balance Display Panel
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                glowColor = NeonPurple.copy(alpha = 0.2f)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Current Wallet Balance",
                        color = TextGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    AnimatedBalanceDisplay(
                        balance = profile?.totalBalance ?: 1250.0,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Today's Earnings", color = TextGray, fontSize = 11.sp)
                            Text(
                                text = "৳ ${String.format("%.2f", profile?.todayEarnings ?: 45.00)}",
                                color = NeonPink,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Daily Bonus Claim Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.horizontalGradient(listOf(NeonPurple, NeonPink)))
                                .clickable { viewModel.triggerDailyBonus() }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🎁", fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Daily Gift",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Carousel Slide Banner
            BannerSlider()

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Actions Segment
            Text(
                text = "Premium Features",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Feature 1: Refer card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1E1E24))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                        .clickable { viewModel.currentScreen = Screen.ReferAndEarn }
                        .padding(16.dp)
                ) {
                    Column {
                        Text(text = "👥", fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Refer & Earn", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Get ৳50 / referral", color = TextGray, fontSize = 10.sp)
                    }
                }

                // Feature 2: Transaction Logs History card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1E1E24))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                        .clickable { viewModel.currentScreen = Screen.History }
                        .padding(16.dp)
                ) {
                    Column {
                        Text(text = "📜", fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "History Logs", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = "View payouts list", color = TextGray, fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Daily Hot Tasks Row Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "High Reward Hot Tasks",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "See All",
                    color = NeonPink,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.currentTab = BottomTab.Tasks }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Task List
            if (availableTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tasks available. Pull to refresh!", color = TextGray, fontSize = 12.sp)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    availableTasks.forEach { task ->
                        TaskRow(task = task, onClick = { viewModel.startTask(task) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

// Helper Task Composable Row
@Composable
fun TaskRow(task: Task, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x331F1F29))
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Task type icon with glowing ring
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(Color(0x13FFFFFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (task.category) {
                        "Visit" -> "🌐"
                        "Watch" -> "🎥"
                        "Survey" -> "📝"
                        else -> "📋"
                    },
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "⏱️ ${task.durationSeconds}s duration • ${task.category}",
                    color = TextGray,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Reward details tag
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "+৳${String.format("%.2f", task.reward)}",
                    color = NeonPink,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Claim",
                    color = NeonPurple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// 7. Tasks Tab Screen (Segmented list)
@Composable
fun TasksScreen(viewModel: IncomeTaskViewModel) {
    val tasksList by viewModel.tasks.collectAsState()
    var selectedCategory by remember { mutableStateOf("Visit") }

    val filteredTasks = tasksList.filter { it.category == selectedCategory }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Earning Tasks Lobby",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "Select any category below to fetch available tasks.",
                color = TextGray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Premium Category Selection Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Visit", "Watch", "Survey").forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(NeonPurple, NeonPink))
                                else Brush.linearGradient(listOf(Color(0xFF1E1E24), Color(0xFF1E1E24)))
                            )
                            .clickable { selectedCategory = cat }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📋", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No tasks available under $selectedCategory.", color = TextGray, fontSize = 13.sp)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredTasks) { task ->
                        TaskRow(task = task, onClick = { viewModel.startTask(task) })
                    }
                }
            }
        }
    }
}

// 8. Wallet Tab Screen (Withdraw portal)
@Composable
fun WalletScreen(viewModel: IncomeTaskViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val withdrawalsList by viewModel.withdrawals.collectAsState()
    var selectedWalletTab by remember { mutableStateOf("Payout") } // Payout or History

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Secure Wallet Portal",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Segments
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Payout", "History").forEach { tab ->
                    val isSelected = selectedWalletTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(NeonPurple, NeonPink))
                                else Brush.linearGradient(listOf(Color(0xFF1E1E24), Color(0xFF1E1E24)))
                            )
                            .clickable { selectedWalletTab = tab }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (selectedWalletTab == "Payout") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Current balance tag
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Available Balance", color = TextGray, fontSize = 11.sp)
                                Text(
                                    text = "৳ ${String.format("%.2f", profile?.totalBalance ?: 1250.0)}",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Text(text = "🪙 Secure Cashout", fontSize = 13.sp)
                        }
                    }

                    // Form
                    Text(text = "Enter Withdrawal Details", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    // Method dropdown simulator
                    var showDropdown by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E1E24))
                            .clickable { showDropdown = !showDropdown }
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Method: ${viewModel.withdrawMethod}", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null, tint = Color.White)
                        }
                    }

                    if (showDropdown) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF131318), RoundedCornerShape(12.dp))
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("bKash", "Nagad", "Rocket", "Bank Transfer").forEach { met ->
                                Text(
                                    text = met,
                                    color = if (viewModel.withdrawMethod == met) NeonPurple else Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.withdrawMethod = met
                                            showDropdown = false
                                        }
                                        .padding(10.dp)
                                )
                            }
                        }
                    }

                    // Amount input
                    OutlinedTextField(
                        value = viewModel.withdrawAmount,
                        onValueChange = { viewModel.withdrawAmount = it },
                        label = { Text("Enter Amount (৳)", color = TextGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassBg,
                            unfocusedContainerColor = GlassBg
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.AttachMoney, contentDescription = null, tint = NeonPurple)
                        }
                    )

                    // Account field
                    OutlinedTextField(
                        value = viewModel.withdrawAccount,
                        onValueChange = { viewModel.withdrawAccount = it },
                        label = { Text("Account/Card/Phone Number", color = TextGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassBg,
                            unfocusedContainerColor = GlassBg
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.AccountBalance, contentDescription = null, tint = NeonPurple)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    GradientButton(
                        text = "Submit Request",
                        onClick = { viewModel.submitWithdrawal() },
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Rounded.CheckCircle
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Notice card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF24151C))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "⚠️ Minimum withdrawal amount is ৳ 100.00 for mobile banking (bKash/Nagad/Rocket) and ৳ 500.00 for direct Bank Transfer. Payout requests are audited manually and approved within 24 hours.",
                            color = NeonPink,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            } else {
                if (withdrawalsList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No historic withdrawal records found.", color = TextGray, fontSize = 12.sp)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(withdrawalsList) { withdrawal ->
                            WithdrawalRow(withdrawal = withdrawal)
                        }
                    }
                }
            }
        }
    }
}

// Withdrawal historic item
@Composable
fun WithdrawalRow(withdrawal: Withdrawal) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1E1E24))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Requested via ${withdrawal.method}", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = "Account: ${withdrawal.account}", color = TextGray, fontSize = 11.sp)
                Text(
                    text = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault()).format(java.util.Date(withdrawal.timestamp)),
                    color = TextDarkGray,
                    fontSize = 9.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "৳ ${String.format("%.2f", withdrawal.amount)}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )

                Box(
                    modifier = Modifier
                        .background(
                            when (withdrawal.status) {
                                "Approved" -> Color(0xFF10B981).copy(alpha = 0.2f)
                                "Rejected" -> NeonPink.copy(alpha = 0.2f)
                                else -> Color(0xFFFBBF24).copy(alpha = 0.2f)
                            },
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = withdrawal.status,
                        color = when (withdrawal.status) {
                            "Approved" -> Color(0xFF10B981)
                            "Rejected" -> NeonPink
                            else -> Color(0xFFFBBF24)
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// 9. Refer & Earn Lobby Screen
@Composable
fun ReferAndEarnScreen(viewModel: IncomeTaskViewModel) {
    val context = LocalContext.current
    val profile by viewModel.userProfile.collectAsState()
    val referralsList by viewModel.referrals.collectAsState()

    val referralLink = "https://incometask.com/ref/${profile?.username ?: "jahid"}"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.currentScreen = Screen.MainContainer },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0x13FFFFFF), CircleShape)
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Refer & Earn Panel", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Invite banner card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                glowColor = NeonPink.copy(alpha = 0.15f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "👥", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Invite Friends, Earn Millions!",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Share your exclusive referral code with your friends and earn instant ৳50.00 for every valid registration!",
                        color = TextGray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Stats grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Total Referrals
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1E1E24))
                        .padding(12.dp)
                ) {
                    Column {
                        Text("Total Referred", color = TextGray, fontSize = 11.sp)
                        Text("${referralsList.size}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    }
                }

                // Total earned referral money
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1E1E24))
                        .padding(12.dp)
                ) {
                    Column {
                        Text("Referral Earned", color = TextGray, fontSize = 11.sp)
                        Text("৳${String.format("%.2f", referralsList.sumOf { it.amount })}", color = NeonPink, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Referral code visual container
            Text(text = "Your Invitation Link", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF131318))
                    .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = referralLink,
                    color = NeonPurple,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .background(Color(0x13FFFFFF), RoundedCornerShape(8.dp))
                        .clickable {
                            // Copy to clipboard simulation
                            viewModel.showNotification(
                                title = "Copied!",
                                message = "Invitation link copied to clipboard successfully.",
                                isSuccess = true
                            )
                        }
                        .padding(8.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Social shares
            Text(text = "Quick Share", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("WhatsApp", "Facebook", "Telegram", "Twitter").forEach { plat ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x13FFFFFF))
                            .clickable {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "Earn extra income using Income Task! Register now using my referral link: $referralLink")
                                }
                                context.startActivity(Intent.createChooser(intent, "Share via"))
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = plat, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Referral list segment
            Text(
                text = "Referred Users List (${referralsList.size})",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (referralsList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No users referred yet. Start inviting!", color = TextGray, fontSize = 12.sp)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    referralsList.forEach { ref ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E1E24))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = ref.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "Status: ${ref.status}", color = TextGray, fontSize = 11.sp)
                                    Text(text = ref.date, color = TextDarkGray, fontSize = 9.sp)
                                }

                                Text(
                                    text = "+৳${String.format("%.2f", ref.amount)}",
                                    color = Color(0xFF10B981),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// 10. Notifications Center Screen
@Composable
fun NotificationsScreen(viewModel: IncomeTaskViewModel) {
    val notifsList by viewModel.notifications.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.currentScreen = Screen.MainContainer },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0x13FFFFFF), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Notifications", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                }

                // Mark read
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x13FFFFFF))
                        .clickable { viewModel.markAllNotificationsRead() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(text = "Read All", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (notifsList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🔔", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Empty inbox", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(text = "New reward credits will alert you here.", color = TextGray, fontSize = 11.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notifsList) { notif ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (notif.isRead) Color(0xFF1E1E24) else Color(0xFF28203A))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color(0x13FFFFFF), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "🔔", fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = notif.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = notif.message, color = TextGray, fontSize = 11.sp)
                                    Text(
                                        text = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault()).format(java.util.Date(notif.timestamp)),
                                        color = TextDarkGray,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 11. Profile Tab Screen
@Composable
fun ProfileScreen(viewModel: IncomeTaskViewModel) {
    val profile by viewModel.userProfile.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Premium User Profile",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Profile info card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Brush.linearGradient(GradientPurplePink), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (profile?.name?.take(1) ?: "U").uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = profile?.name ?: "Jahid Hasan",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Username: @${profile?.username ?: "jahid_hasan"}",
                        color = TextGray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Level/XP Milestone Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Level ${profile?.level ?: 7}",
                            color = NeonPurple,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${profile?.xp ?: 3450} / 5000 XP",
                            color = NeonPink,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Linear Indicator
                    LinearProgressIndicator(
                        progress = { ((profile?.xp ?: 3450).toFloat() / 5000f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = NeonPurple,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Details card grid
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ProfileDetailField(icon = Icons.Rounded.Email, label = "Email ID", value = profile?.email ?: "xrhasan05@gmail.com")
                ProfileDetailField(icon = Icons.Rounded.Phone, label = "Phone Number", value = profile?.phone ?: "+8801712345678")
                ProfileDetailField(icon = Icons.Rounded.Key, label = "Refer Code", value = profile?.referralCode ?: "JAHID123")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Actions list
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SettingsNavigatorRow(title = "App Customizer & Language Settings", icon = Icons.Rounded.Settings) {
                    viewModel.currentScreen = Screen.Settings
                }

                SettingsNavigatorRow(title = "History Transaction Records", icon = Icons.Rounded.History) {
                    viewModel.currentScreen = Screen.History
                }

                Spacer(modifier = Modifier.height(14.dp))

                GradientButton(
                    text = "Sign Out",
                    onClick = { viewModel.logout() },
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Rounded.Logout
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

// Profile list item helper
@Composable
fun ProfileDetailField(icon: ImageVector, label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E1E24))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = label, color = TextGray, fontSize = 10.sp)
                Text(text = value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Settings row helper
@Composable
fun SettingsNavigatorRow(title: String, icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x13FFFFFF))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = NeonPink, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
            Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null, tint = TextGray, modifier = Modifier.size(18.dp))
        }
    }
}

// 12. App Settings Screen (with secret admin panel gate)
@Composable
fun SettingsScreen(viewModel: IncomeTaskViewModel) {
    var tapVersionCount by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.currentScreen = Screen.MainContainer },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0x13FFFFFF), CircleShape)
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "App Settings", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Toggles
            var soundEnabled by remember { mutableStateOf(true) }
            var hapticEnabled by remember { mutableStateOf(true) }

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🔈 Enable App Sounds", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = { soundEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonPurple, checkedTrackColor = NeonPurple.copy(alpha = 0.3f))
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "📳 Enable Haptic Vibrations", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Switch(
                            checked = hapticEnabled,
                            onCheckedChange = { hapticEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonPurple, checkedTrackColor = NeonPurple.copy(alpha = 0.3f))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Secret Door Version tag
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        tapVersionCount++
                        if (tapVersionCount >= 5) {
                            viewModel.showAdminPinDialog = true
                            tapVersionCount = 0
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Income Task App Premium Edition",
                    color = TextGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "v2.0 (Build 506) • Tap version 5 times for Admin Web Console",
                    color = TextDarkGray,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Admin Pin Verification Overlay Dialog
        if (viewModel.showAdminPinDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clickable { viewModel.showAdminPinDialog = false },
                contentAlignment = Alignment.Center
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(16.dp)
                        .clickable(enabled = false) {},
                    glowColor = NeonPurple
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🔑", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Admin Authorization",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Enter secure developer bypass PIN (Hint: 1234)",
                            color = TextGray,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = viewModel.adminPinInput,
                            onValueChange = { viewModel.adminPinInput = it },
                            label = { Text("4-Digit Secure PIN", color = TextGray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedBorderColor = NeonPurple,
                                unfocusedBorderColor = GlassBorder,
                                focusedContainerColor = GlassBg,
                                unfocusedContainerColor = GlassBg
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation()
                        )

                        if (viewModel.adminPinError.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = viewModel.adminPinError, color = NeonPink, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x13FFFFFF))
                                    .clickable { viewModel.showAdminPinDialog = false }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Cancel", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Brush.horizontalGradient(listOf(NeonPurple, NeonPink)))
                                    .clickable { viewModel.verifyAdminPin() }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Verify", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// 13. Admin Web Control Panel Console
@Composable
fun AdminPanelScreen(viewModel: IncomeTaskViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.currentScreen = Screen.MainContainer },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0x13FFFFFF), CircleShape)
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Admin Web Console", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Gross metrics
            Text(text = "Income Network Metrics", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Total users
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1E1E24))
                        .padding(14.dp)
                ) {
                    Column {
                        Text("Gross Network Users", color = TextGray, fontSize = 10.sp)
                        Text("1,245 Active", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Total payout budget
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1E1E24))
                        .padding(14.dp)
                ) {
                    Column {
                        Text("Gross Approved Payouts", color = TextGray, fontSize = 10.sp)
                        Text("৳ 14,350.00", color = Color(0xFF10B981), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Master controls
            Text(text = "App Maintenance Controls", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            val mActive = profile?.isMaintenanceMode == true
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF24151C))
                    .border(1.dp, NeonPink.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .clickable { viewModel.setMaintenanceMode(!mActive) }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (mActive) "Maintenance Mode: ACTIVE" else "Maintenance Mode: INACTIVE",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Toggling this will immediately lock standard users out with a gorgeous immersive screen.",
                            color = TextGray,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(if (mActive) NeonPink else Color.DarkGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Rounded.PowerSettingsNew, contentDescription = null, tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Seed utilities
            Text(text = "Developer Database Seed Options", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            // Restore defaults
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x13FFFFFF))
                    .clickable {
                        viewModel.logout()
                    }
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "⚙️ Reset Profile & Flush Cached Tables", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 14. History Transaction Logs Screen
@Composable
fun HistoryScreen(viewModel: IncomeTaskViewModel) {
    val withdrawalsList by viewModel.withdrawals.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.currentScreen = Screen.MainContainer },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0x13FFFFFF), CircleShape)
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Transaction History", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (withdrawalsList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No payout history records found.", color = TextGray, fontSize = 12.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(withdrawalsList) { withdrawal ->
                        WithdrawalRow(withdrawal = withdrawal)
                    }
                }
            }
        }
    }
}

// 15. Task Detail Screens (Visit)
@Composable
fun TaskDetailVisitScreen(viewModel: IncomeTaskViewModel) {
    val task = viewModel.activeTask ?: return
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(40.dp))

                // Header
                Text(
                    text = "Website Visiting Lobby",
                    color = NeonPurple,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Text(
                    text = task.title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Timer progress circle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(160.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { viewModel.taskProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = NeonPink,
                        trackColor = Color.White.copy(alpha = 0.05f),
                        strokeWidth = 10.dp
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${viewModel.taskTimeRemaining}",
                            color = Color.White,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Seconds left",
                            color = TextGray,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(text = "Instruction Details", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.description,
                            color = TextGray,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Actions
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                GradientButton(
                    text = "Visit External Webpage Now",
                    onClick = {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(task.externalUrl))
                        context.startActivity(browserIntent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Rounded.OpenInBrowser
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (viewModel.taskTimeRemaining == 0 && !viewModel.isTaskRewardClaimed) Brush.horizontalGradient(listOf(NeonPurple, NeonPink))
                            else Brush.linearGradient(listOf(Color.DarkGray, Color.DarkGray))
                        )
                        .clickable(enabled = (viewModel.taskTimeRemaining == 0 && !viewModel.isTaskRewardClaimed)) {
                            viewModel.claimTaskReward()
                            viewModel.currentScreen = Screen.MainContainer
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (viewModel.isTaskRewardClaimed) "Coins Claimed Successfully!" else "Collect Payout (৳${String.format("%.2f", task.reward)})",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                GlassBorderButton(
                    text = "Cancel & Exit",
                    onClick = {
                        viewModel.stopTaskTimer()
                        viewModel.currentScreen = Screen.MainContainer
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// 16. Task Detail Screens (Watch)
@Composable
fun TaskDetailWatchScreen(viewModel: IncomeTaskViewModel) {
    val task = viewModel.activeTask ?: return
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(40.dp))

                // Header
                Text(
                    text = "Watch & Earn Video Lobby",
                    color = NeonPurple,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Text(
                    text = task.title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Timer progress circle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(160.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { viewModel.taskProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = NeonPink,
                        trackColor = Color.White.copy(alpha = 0.05f),
                        strokeWidth = 10.dp
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${viewModel.taskTimeRemaining}",
                            color = Color.White,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Seconds left",
                            color = TextGray,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(text = "Video Watch Instruction", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.description,
                            color = TextGray,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Actions
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                GradientButton(
                    text = "Play Video Content Now",
                    onClick = {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(task.externalUrl))
                        context.startActivity(browserIntent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Rounded.PlayCircle
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (viewModel.taskTimeRemaining == 0 && !viewModel.isTaskRewardClaimed) Brush.horizontalGradient(listOf(NeonPurple, NeonPink))
                            else Brush.linearGradient(listOf(Color.DarkGray, Color.DarkGray))
                        )
                        .clickable(enabled = (viewModel.taskTimeRemaining == 0 && !viewModel.isTaskRewardClaimed)) {
                            viewModel.claimTaskReward()
                            viewModel.currentScreen = Screen.MainContainer
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (viewModel.isTaskRewardClaimed) "Coins Claimed Successfully!" else "Collect Payout (৳${String.format("%.2f", task.reward)})",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                GlassBorderButton(
                    text = "Cancel & Exit",
                    onClick = {
                        viewModel.stopTaskTimer()
                        viewModel.currentScreen = Screen.MainContainer
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// 17. Task Detail Screens (Survey)
@Composable
fun TaskDetailSurveyScreen(viewModel: IncomeTaskViewModel) {
    val task = viewModel.activeTask ?: return

    // Sample question sets
    val questions = listOf(
        "Which mobile banking app do you use most frequently?" to listOf("bKash", "Nagad", "Rocket", "Upay"),
        "How many times do you use internet banking weekly?" to listOf("1-2 times", "3-5 times", "Daily", "Never"),
        "What is your primary source of online micro-earnings?" to listOf("Completing microtasks", "Freelancing", "Blogging", "Social media promotions")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Premium Market Survey Panel",
                    color = NeonPurple,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Text(
                    text = task.title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Answer all survey questions below carefully to authorize rewards.",
                    color = TextGray,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Render questions
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    questions.forEachIndexed { qIdx, qPair ->
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = "${qIdx + 1}. ${qPair.first}",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                qPair.second.forEach { opt ->
                                    val isSelected = viewModel.selectedSurveyAnswers[qIdx] == opt
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) NeonPurple.copy(alpha = 0.2f) else Color(0x13FFFFFF))
                                            .border(1.dp, if (isSelected) NeonPurple else Color.Transparent, RoundedCornerShape(8.dp))
                                            .clickable {
                                                val copy = viewModel.selectedSurveyAnswers.toMutableMap()
                                                copy[qIdx] = opt
                                                viewModel.selectedSurveyAnswers = copy
                                            }
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = {
                                                val copy = viewModel.selectedSurveyAnswers.toMutableMap()
                                                copy[qIdx] = opt
                                                viewModel.selectedSurveyAnswers = copy
                                            },
                                            colors = RadioButtonDefaults.colors(selectedColor = NeonPurple, unselectedColor = Color.Gray)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = opt, color = Color.White, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit block
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val surveyCompleted = viewModel.selectedSurveyAnswers.size == questions.size

                GradientButton(
                    text = if (viewModel.isTaskRewardClaimed) "Coins Credited Successfully!" else "Submit & Collect Reward (৳${String.format("%.2f", task.reward)})",
                    onClick = {
                        viewModel.claimTaskReward()
                        viewModel.currentScreen = Screen.MainContainer
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = (surveyCompleted && !viewModel.isTaskRewardClaimed)
                )

                GlassBorderButton(
                    text = "Cancel & Exit",
                    onClick = { viewModel.currentScreen = Screen.MainContainer },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
