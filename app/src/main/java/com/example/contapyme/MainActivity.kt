package com.example.contapyme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.contapyme.navigation.Routes
import com.example.contapyme.ui.components.FloatingBottomBar
import com.example.contapyme.ui.components.QuickActionSheet
import com.example.contapyme.ui.screens.AgregarClienteScreen
import com.example.contapyme.ui.screens.ClientesScreen
import com.example.contapyme.ui.screens.DashboardScreen
import com.example.contapyme.ui.screens.DetalleAlertaScreen
import com.example.contapyme.ui.screens.DetalleClienteScreen
import com.example.contapyme.ui.screens.DetalleVentaScreen
import com.example.contapyme.ui.screens.InventarioScreen
import com.example.contapyme.ui.screens.NotificacionesScreen
import com.example.contapyme.ui.screens.RegistrarGastoScreen
import com.example.contapyme.ui.screens.RegistrarVentaScreen
import com.example.contapyme.ui.screens.VentasScreen
import com.example.contapyme.ui.screens.auth.ApiConfigScreen
import com.example.contapyme.ui.screens.auth.ChangePasswordScreen
import com.example.contapyme.ui.screens.auth.ForgotPasswordScreen
import com.example.contapyme.ui.screens.auth.LoginScreenNew
import com.example.contapyme.ui.screens.auth.ResetPasswordScreen
import com.example.contapyme.ui.screens.auth.TwoFAScreen
import com.example.contapyme.ui.theme.ContaPymeTheme
import com.example.contapyme.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContaPymeTheme {
                AppNavHost()
            }
        }
    }
}

private val bottomBarRoutes = setOf(
    Routes.DASHBOARD,
    Routes.INVENTARIO,
    Routes.CLIENTES,
    Routes.VENTAS
)

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showQuickActions by remember { mutableStateOf(false) }
    val authViewModel: AuthViewModel = viewModel()

    Box(Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color(0xFFF4F6FB))) {
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Routes.LOGIN) {
                LoginScreenNew(
                    viewModel = authViewModel,
                    onRequires2FA = {
                        navController.navigate(Routes.TWO_FA) {
                            launchSingleTop = true
                        }
                    },
                    onAlreadyLoggedIn = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToForgotPassword = {
                        navController.navigate(Routes.FORGOT_PASSWORD)
                    },
                    onNavigateToApiConfig = {
                        navController.navigate(Routes.API_CONFIG)
                    }
                )
            }
            
            composable(Routes.TWO_FA) {
                TwoFAScreen(
                    viewModel = authViewModel,
                    onBack = {
                        authViewModel.reset2FAFlow()
                        navController.popBackStack()
                    },
                    onVerified = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Routes.FORGOT_PASSWORD) {
                ForgotPasswordScreen(
                    viewModel = authViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToReset = { token ->
                        navController.navigate("${Routes.RESET_PASSWORD}/$token")
                    }
                )
            }
            
            composable("${Routes.RESET_PASSWORD}/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                ResetPasswordScreen(
                    viewModel = authViewModel,
                    token = token,
                    onBack = { navController.popBackStack() },
                    onSuccess = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Routes.CHANGE_PASSWORD) {
                ChangePasswordScreen(
                    viewModel = authViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            
            composable(Routes.API_CONFIG) {
                ApiConfigScreen(
                    viewModel = authViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.DASHBOARD) {
                DashboardScreen(
                    onNotificaciones = { navController.navigate(Routes.NOTIFICACIONES) },
                    onVerAlerta = { id -> navController.navigate(Routes.detalleAlerta(id)) },
                    onVerVentas = { navController.navigate(Routes.VENTAS) }
                )
            }

            composable(Routes.REGISTRAR_VENTA) {
                RegistrarVentaScreen(
                    onBack = { navController.popBackStack() },
                    onGuardar = { navController.popBackStack() }
                )
            }

            composable(Routes.REGISTRAR_GASTO) {
                RegistrarGastoScreen(
                    onBack = { navController.popBackStack() },
                    onGuardar = { navController.popBackStack() }
                )
            }

            composable(Routes.CLIENTES) {
                ClientesScreen(
                    onBack = { navController.popBackStack() },
                    onAgregar = { navController.navigate(Routes.AGREGAR_CLIENTE) },
                    onVerDetalle = { id -> navController.navigate(Routes.detalleCliente(id)) }
                )
            }

            composable(Routes.AGREGAR_CLIENTE) {
                AgregarClienteScreen(
                    onBack = { navController.popBackStack() },
                    onGuardar = { navController.popBackStack() }
                )
            }

            composable("${Routes.DETALLE_CLIENTE}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: "C-0004"
                DetalleClienteScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.VENTAS) {
                VentasScreen(
                    onBack = { navController.popBackStack() },
                    onVerDetalle = { codigo -> navController.navigate(Routes.detalleVenta(codigo)) }
                )
            }

            composable("${Routes.DETALLE_VENTA}/{codigo}") { backStackEntry ->
                val codigo = backStackEntry.arguments?.getString("codigo") ?: "V-0245"
                DetalleVentaScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.INVENTARIO) {
                InventarioScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.NOTIFICACIONES) {
                NotificacionesScreen(
                    onBack = { navController.popBackStack() },
                    onVerDetalle = { id -> navController.navigate(Routes.detalleAlerta(id)) }
                )
            }

            composable("${Routes.DETALLE_ALERTA}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: "N-0087"
                DetalleAlertaScreen(
                    onBack = { navController.popBackStack() },
                    onVerVenta = {
                        navController.navigate(Routes.detalleVenta("V-0241"))
                    }
                )
            }
        }

        if (currentRoute in bottomBarRoutes) {
            FloatingBottomBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo(Routes.DASHBOARD) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                onAddClick = { showQuickActions = true },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        if (showQuickActions) {
            QuickActionSheet(
                onDismiss = { showQuickActions = false },
                onRegistrarVenta = {
                    showQuickActions = false
                    navController.navigate(Routes.REGISTRAR_VENTA)
                },
                onRegistrarGasto = {
                    showQuickActions = false
                    navController.navigate(Routes.REGISTRAR_GASTO)
                }
            )
        }
    }
}
