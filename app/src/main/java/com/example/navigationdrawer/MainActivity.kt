package com.example.navigationdrawer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.Gravity.RIGHT
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.navigationdrawer.ui.theme.NavigationDrawerTheme
import kotlinx.coroutines.launch

// Data Class to handle items
data class TabItem(
    val title: String, val unSelectedItem: ImageVector, val selectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationDrawerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                        val scope = rememberCoroutineScope()
                        var selectedItemIndex by rememberSaveable {
                            mutableStateOf(0)
                        }

                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
                            ModalNavigationDrawer(
                                drawerContent = {
                                    PermanentDrawerSheet {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        var selectedTabIndex by remember {
                                            mutableIntStateOf(0)
                                        }

                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            tabRow()
                                            
                                        }

                                    }
                                },
                                drawerState = drawerState
                            ) {
                                Scaffold(
                                    topBar = {
                                        TopAppBar(
                                            title = {
                                                Text(text = "Test App!!!")
                                            },
                                            navigationIcon = {
                                                IconButton(onClick = {
                                                    scope.launch {
                                                        drawerState.open()
                                                    }
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.Menu,
                                                        contentDescription = "Menu"
                                                    )
                                                }
                                            }
                                        )
                                    }
                                ) {

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun tabRow() {
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val tabItem = listOf(
        TabItem(
            title = "Web", unSelectedItem = Icons.Outlined.Home, selectedIcon = Icons.Filled.Home
        ), TabItem(
            title = "Image",
            unSelectedItem = Icons.Outlined.ShoppingCart,
            selectedIcon = Icons.Filled.ShoppingCart
        ), TabItem(
            title = "Translate",
            unSelectedItem = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings
        ), TabItem(
            title = "Maths",
            unSelectedItem = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings
        )

    )

    val pagerState = rememberPagerState {
        tabItem.size
    }

    LaunchedEffect(key1 = selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(key1 = pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress)
            selectedTabIndex = pagerState.currentPage
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabItem.forEachIndexed { index, tabItem ->

                androidx.compose.material3.Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = { Text(text = tabItem.title, color = Color.Black) }

                )
            }
        }

        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                if(tabItem[index].title == "Web" )
                {
                    WebViewScreen()
                }
                else
                    Text(text = tabItem[index].title)
            }
        }
    }
}

@Composable
fun WebViewScreen() {

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()

                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
            }
        },
        update = { webView ->
            webView.loadUrl("https://www.google.com/search?q=android&sca_esv=09054e7a388018cf&tbm=isch&source=hp&biw=1536&bih=735&ei=yhRvZoHDLpClvr0Px5efkQ8&iflsig=AL9hbdgAAAAAZm8i2ppAbkXyPDNll0nd0eP0Cl1vGykB&ved=0ahUKEwiB0Iu4x-CGAxWQkq8BHcfLJ_IQ4dUDCAc&uact=5&oq=android&gs_lp=EgNpbWciB2FuZHJvaWQyCBAAGIAEGLEDMgUQABiABDIFEAAYgAQyCBAAGIAEGLEDMggQABiABBixAzIFEAAYgAQyBRAAGIAEMggQABiABBixAzIIEAAYgAQYsQMyCBAAGIAEGLEDSPIGUHlYngZwAHgAkAEAmAHGAqAByQiqAQcwLjQuMS4xuAEDyAEA-AEBigILZ3dzLXdpei1pbWeYAgagAuwIqAIAwgILEAAYgAQYsQMYgwHCAg4QABiABBixAxiDARiKBcICBBAAGAOYAwSSBwcwLjQuMS4xoAf0HQ&sclient=img")
        }
    )
}

