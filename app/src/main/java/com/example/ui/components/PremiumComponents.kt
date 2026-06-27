package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

// 1. Frosted Glassmorphism Card
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 1.dp,
    glowColor: Color = NeonPurple.copy(alpha = 0.15f),
    content: @Composable ColumnScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAnim by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAnim"
    )

    Box(
        modifier = modifier
            .drawBehind {
                drawRect(
                    color = glowColor.copy(alpha = glowColor.alpha * glowAnim),
                    size = size
                )
            }
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x261E1E2E), // Subtle dark purple-gray
                        Color(0x3B181824)  // Even darker purple-gray
                    )
                )
            )
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonPurple.copy(alpha = 0.5f),
                        NeonPink.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(16.dp)
    ) {
        Column {
            content()
        }
    }
}

// 2. Premium Solid Pink/Purple Gradient Button
@Composable
fun GlowingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val animScale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .scale(animScale.value)
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (enabled) listOf(NeonPurple, NeonPink) else listOf(Color.Gray, Color.DarkGray)
                )
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    onClick()
                }
            )
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// 3. Outlined Glowing Button
@Composable
fun OutlinedGlowingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(colors = listOf(NeonPurple, NeonPink)),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = NeonPink,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// 4. Neon Icon Circle
@Composable
fun NeonIconBox(
    icon: ImageVector,
    color: Color = NeonPurple,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(54.dp)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(color.copy(alpha = 0.8f), color.copy(alpha = 0.2f))
                ),
                shape = CircleShape
            )
            .background(color.copy(alpha = 0.1f), shape = CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(26.dp)
        )
        
        if (badgeCount > 0) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .size(18.dp)
                    .background(NeonPink, shape = CircleShape)
            ) {
                Text(
                    text = badgeCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// 5. Animated Balance Display (with hide/reveal eye trigger)
@Composable
fun AnimatedBalanceDisplay(
    balance: Double,
    modifier: Modifier = Modifier,
    currencySymbol: String = "৳"
) {
    var isBalanceVisible by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x33FFFFFF))
            .clickable { isBalanceVisible = !isBalanceVisible }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            contentDescription = "Toggle Balance",
            tint = TextLightGray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isBalanceVisible) "$currencySymbol ${String.format("%.2f", balance)}" else "Balance Locked •••",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = if (isBalanceVisible) 0.sp else 1.5.sp
        )
    }
}

// 6. Sliding Banner Carousel
@Composable
fun BannerSlider(modifier: Modifier = Modifier) {
    val banners = listOf(
        "🔥 Limited Offer: 1.5x Daily rewards today!",
        "✨ Register with referral for instant ৳50.00",
        "📢 Maintenance alert: June 28 at 02:00 AM UTC",
        "🏆 Top users lead the table! Climb up to win ৳500"
    )
    var currentIdx by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIdx = (currentIdx + 1) % banners.size
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF3B0764), // Dark Violet
                        Color(0xFF09090B),
                        Color(0xFF500724)  // Dark Pink
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(colors = listOf(NeonPurple.copy(alpha = 0.3f), NeonPink.copy(alpha = 0.3f))),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = banners[currentIdx],
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .background(NeonPink, shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "INFO",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// 7. Premium Bottom Navigation with Glowing Floating Rocket Button
@Composable
fun PremiumBottomNav(
    activeTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rocket_pulse")
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rocketScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(95.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Base Navigation Glass Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                .background(Color(0xE60D0D11)) // Dark transparent
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            NeonPurple.copy(alpha = 0.3f),
                            NeonPink.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tab 1: Home
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected("Home") }
                ) {
                    Text(
                        text = "🏠",
                        fontSize = 20.sp,
                        color = if (activeTab == "Home") Color.White else TextLightGray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Home",
                        fontSize = 11.sp,
                        fontWeight = if (activeTab == "Home") FontWeight.Bold else FontWeight.Normal,
                        color = if (activeTab == "Home") NeonPurple else TextLightGray
                    )
                }

                // Tab 2: Tasks
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected("Tasks") }
                ) {
                    Text(
                        text = "📋",
                        fontSize = 20.sp,
                        color = if (activeTab == "Tasks") Color.White else TextLightGray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Tasks",
                        fontSize = 11.sp,
                        fontWeight = if (activeTab == "Tasks") FontWeight.Bold else FontWeight.Normal,
                        color = if (activeTab == "Tasks") NeonPurple else TextLightGray
                    )
                }

                // Spacer for rocket floating button
                Spacer(modifier = Modifier.width(70.dp))

                // Tab 3: Refer
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected("Refer") }
                ) {
                    Text(
                        text = "👥",
                        fontSize = 20.sp,
                        color = if (activeTab == "Refer") Color.White else TextLightGray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Refer",
                        fontSize = 11.sp,
                        fontWeight = if (activeTab == "Refer") FontWeight.Bold else FontWeight.Normal,
                        color = if (activeTab == "Refer") NeonPurple else TextLightGray
                    )
                }

                // Tab 4: Profile
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected("Profile") }
                ) {
                    Text(
                        text = "👤",
                        fontSize = 20.sp,
                        color = if (activeTab == "Profile") Color.White else TextLightGray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Profile",
                        fontSize = 11.sp,
                        fontWeight = if (activeTab == "Profile") FontWeight.Bold else FontWeight.Normal,
                        color = if (activeTab == "Profile") NeonPurple else TextLightGray
                    )
                }
            }
        }

        // Floating Rocket Button centered above the bar
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-5).dp)
                .scale(scaleAnim)
                .size(72.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(NeonPurple, Color.Transparent)
                    ),
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(colors = listOf(NeonPink, NeonPurple)),
                    shape = CircleShape
                )
                .clickable {
                    // Triggers Task navigation or special reward screen
                    onTabSelected("Tasks")
                }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        brush = Brush.verticalGradient(colors = listOf(NeonPink, AccentPurple)),
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = "🚀",
                    fontSize = 28.sp
                )
            }
        }
    }
}

// 8. Shimmer Loading Skeleton Card (for tasks lists etc.)
@Composable
fun ShimmerTaskCard(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E1E24).copy(alpha = alphaAnim))
            .border(width = 1.dp, color = Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = 0.2f))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(16.dp)
                        .background(Color.Gray.copy(alpha = 0.2f))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(12.dp)
                        .background(Color.Gray.copy(alpha = 0.2f))
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray.copy(alpha = 0.2f))
            )
        }
    }
}

// 9. GradientButton Composable (alias to GlowingButton)
@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    GlowingButton(text = text, onClick = onClick, modifier = modifier, enabled = enabled, icon = icon)
}

// 10. GlassBorderButton Composable (alias to OutlinedGlowingButton)
@Composable
fun GlassBorderButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    OutlinedGlowingButton(text = text, onClick = onClick, modifier = modifier, icon = icon)
}

// 11. Immersive Maintenance Screen with secret bypass action
@Composable
fun MaintenanceScreen(
    onBypassClicked: () -> Unit = {},
    onAdminSecretPin: () -> Unit = {}
) {
    var tapCount by remember { mutableStateOf(0) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepBlack, Color(0xFF130F26))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            // Animated glowing badge with warning sign
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0x1A8B5CF6), shape = CircleShape)
                    .border(2.dp, Brush.linearGradient(listOf(NeonPurple, NeonPink)), CircleShape)
                    .clickable {
                        tapCount++
                        if (tapCount >= 5) {
                            onAdminSecretPin()
                            tapCount = 0
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🚧",
                    fontSize = 48.sp
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "System Maintenance",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "আমরা অ্যাপটি আরও উন্নত করার জন্য কাজ করছি। খুব শীঘ্রই আমরা নতুন আপডেট এবং আকর্ষণীয় ফিচার নিয়ে ফিরে আসবো। আমাদের সাথেই থাকুন!",
                color = TextLightGray,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Glass card showing estimated time
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 16.dp,
                borderWidth = 1.dp,
                glowColor = NeonPurple.copy(alpha = 0.2f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "⏱️",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Column {
                        Text(
                            text = "Estimated Completion Time",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Within 1 - 2 Hours",
                            color = NeonPink,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
        
        // Footnote with version
        Text(
            text = "v2.0 Premium Purple Edition",
            color = TextLightGray.copy(alpha = 0.5f),
            fontSize = 11.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

