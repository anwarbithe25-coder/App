package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Gamepad
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.AppDetailScreen
import com.example.ui.screens.DeveloperHubScreen
import com.example.ui.screens.GamesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.theme.FossCyanPrimary
import com.example.ui.theme.FossDarkBg
import com.example.ui.theme.FossEmeraldGreen
import com.example.ui.theme.FossSurfaceDark
import com.example.ui.theme.FossSurfaceElevated
import com.example.ui.theme.FossTextPrimary
import com.example.ui.theme.FossTextSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.OpenPlayViewModel
import kotlinx.coroutines.delay

enum class ScreenTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    EXPLORE("Explore", Icons.Filled.Explore, Icons.Outlined.Explore, "nav_explore"),
    GAMES("Games", Icons.Filled.Gamepad, Icons.Outlined.Gamepad, "nav_games"),
    DEV_HUB("Dev Hub", Icons.Filled.Code, Icons.Outlined.Code, "nav_dev_hub"),
    LIBRARY("Library", Icons.Filled.LibraryBooks, Icons.Outlined.LibraryBooks, "nav_library")
}

@androidx.compose.material3.ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    private val viewModel: OpenPlayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                OpenPlayApp(viewModel = viewModel)
            }
        }
    }
}

@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun OpenPlayApp(viewModel: OpenPlayViewModel) {
    var currentTab by remember { mutableStateOf(ScreenTab.EXPLORE) }
    val selectedAppId by viewModel.selectedAppId.collectAsState()
    val uiNotice by viewModel.uiNotice.collectAsState()

    // Handle system back button when in detail screen
    BackHandler(enabled = selectedAppId != null) {
        viewModel.clearSelectedApp()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FossDarkBg,
        bottomBar = {
            if (selectedAppId == null) {
                NavigationBar(
                    containerColor = FossSurfaceDark,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_nav_bar")
                ) {
                    ScreenTab.values().forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentTab = tab },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = FossCyanPrimary,
                                unselectedIconColor = FossTextSecondary,
                                unselectedTextColor = FossTextSecondary,
                                indicatorColor = FossCyanPrimary
                            ),
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (selectedAppId != null) {
                AppDetailScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.clearSelectedApp() }
                )
            } else {
                when (currentTab) {
                    ScreenTab.EXPLORE -> HomeScreen(
                        viewModel = viewModel,
                        onAppClick = { appId -> viewModel.selectApp(appId) },
                        onNavigateToGames = { currentTab = ScreenTab.GAMES },
                        onNavigateToLibrary = { currentTab = ScreenTab.LIBRARY }
                    )
                    ScreenTab.GAMES -> GamesScreen(
                        viewModel = viewModel,
                        onAppClick = { appId -> viewModel.selectApp(appId) },
                        onUploadGameClick = { currentTab = ScreenTab.DEV_HUB }
                    )
                    ScreenTab.DEV_HUB -> DeveloperHubScreen(
                        viewModel = viewModel,
                        onAppClick = { appId -> viewModel.selectApp(appId) }
                    )
                    ScreenTab.LIBRARY -> LibraryScreen(
                        viewModel = viewModel,
                        onAppClick = { appId -> viewModel.selectApp(appId) }
                    )
                }
            }

            // Floating Toast/Notice Banner
            AnimatedVisibility(
                visible = uiNotice != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                if (uiNotice != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = FossSurfaceElevated),
                        shape = RoundedCornerShape(12.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(FossCyanPrimary, FossEmeraldGreen))),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("notice_banner")
                    ) {
                        Text(
                            text = uiNotice ?: "",
                            color = FossTextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }

                    LaunchedEffect(uiNotice) {
                        delay(3500)
                        viewModel.clearNotice()
                    }
                }
            }
        }
    }
}
