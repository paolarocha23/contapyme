package com.example.contapyme.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.contapyme.data.api.ApiClient
import com.example.contapyme.data.local.SessionManager
import com.example.contapyme.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isError: Boolean = false,
    val tempToken: String? = null,
    val emailMasked: String? = null,
    val sessionToken: String? = null,
    val isLoggedIn: Boolean = false,
    val requires2FA: Boolean = false
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)
    private val repository = AuthRepository(application)
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    val apiOrigin = sessionManager.apiOrigin
    val tokenFlow = sessionManager.token

    init {
        viewModelScope.launch {
            val token = sessionManager.token.first()
            if (!token.isNullOrEmpty()) {
                _uiState.value = _uiState.value.copy(sessionToken = token, isLoggedIn = true)
            }
        }
    }

    fun setApiOrigin(url: String) {
        viewModelScope.launch {
            sessionManager.setApiOrigin(url)
            ApiClient.reset()
            _uiState.value = _uiState.value.copy(message = "URL actualizada correctamente", isError = false)
        }
    }

    fun login(nombreUsuario: String, contrasena: String) {
        if (nombreUsuario.isBlank() || contrasena.isBlank()) {
            _uiState.value = _uiState.value.copy(message = "Por favor, introduce tu nombre de usuario y contraseña.", isError = true)
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null, isError = false)
            repository.login(nombreUsuario, contrasena)
                .onSuccess { response ->
                    if (response.success && response.requires2FA && response.tempToken != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            requires2FA = true,
                            tempToken = response.tempToken,
                            emailMasked = response.emailMasked,
                            message = response.message
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isError = true,
                            message = response.message ?: "Error al iniciar sesión"
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = true,
                        message = e.message ?: "Error de conexión"
                    )
                }
        }
    }

    fun verify2FA(codigo: String) {
        val tempToken = _uiState.value.tempToken
        if (tempToken.isNullOrEmpty()) {
            _uiState.value = _uiState.value.copy(message = "Sesión de verificación expirada.", isError = true)
            return
        }
        if (codigo.isBlank() || codigo.length != 6) {
            _uiState.value = _uiState.value.copy(message = "Ingresa el código de 6 dígitos.", isError = true)
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null, isError = false)
            repository.verify2FA(tempToken, codigo)
                .onSuccess { response ->
                    if (response.success && response.token != null) {
                        val token = response.token
                        sessionManager.saveSession(
                            token = token,
                            userName = response.user?.nombreUsuario,
                            email = response.user?.correo,
                            rolName = response.user?.rolNombre
                        )
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            sessionToken = token,
                            isLoggedIn = true,
                            requires2FA = false,
                            tempToken = null,
                            message = response.message
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isError = true,
                            message = response.message ?: "Código inválido"
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = true,
                        message = e.message ?: "Error de conexión"
                    )
                }
        }
    }

    fun resend2FA() {
        val tempToken = _uiState.value.tempToken
        if (tempToken.isNullOrEmpty()) {
            _uiState.value = _uiState.value.copy(message = "Sesión de verificación expirada.", isError = true)
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null, isError = false)
            repository.resend2FA(tempToken)
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = !response.success,
                        message = response.message
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = true,
                        message = e.message ?: "Error de conexión"
                    )
                }
        }
    }

    fun forgotPassword(correoOUsuario: String) {
        if (correoOUsuario.isBlank()) {
            _uiState.value = _uiState.value.copy(message = "Por favor, introduce tu correo electrónico o nombre de usuario.", isError = true)
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null, isError = false)
            repository.forgotPassword(correoOUsuario)
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = !response.success,
                        message = response.message
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = true,
                        message = e.message ?: "Error de conexión"
                    )
                }
        }
    }

    fun resetPassword(token: String, nuevaContrasena: String) {
        if (token.isBlank() || nuevaContrasena.isBlank()) {
            _uiState.value = _uiState.value.copy(message = "Faltan datos requeridos.", isError = true)
            return
        }
        if (nuevaContrasena.length < 6) {
            _uiState.value = _uiState.value.copy(message = "La nueva contraseña debe tener al menos 6 caracteres.", isError = true)
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null, isError = false)
            repository.resetPassword(token, nuevaContrasena)
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = !response.success,
                        message = response.message
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = true,
                        message = e.message ?: "Error de conexión"
                    )
                }
        }
    }

    fun changePassword(contrasenaActual: String, nuevaContrasena: String) {
        if (contrasenaActual.isBlank() || nuevaContrasena.isBlank()) {
            _uiState.value = _uiState.value.copy(message = "Debes proporcionar la contraseña actual y la nueva contraseña.", isError = true)
            return
        }
        if (nuevaContrasena.length < 6) {
            _uiState.value = _uiState.value.copy(message = "La nueva contraseña debe contener al menos 6 caracteres.", isError = true)
            return
        }
        val token = _uiState.value.sessionToken
        if (token.isNullOrEmpty()) {
            _uiState.value = _uiState.value.copy(message = "No hay sesión activa.", isError = true)
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null, isError = false)
            repository.changePassword(token, contrasenaActual, nuevaContrasena)
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = !response.success,
                        message = response.message
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = true,
                        message = e.message ?: "Error de conexión"
                    )
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _uiState.value = AuthUiState()
            ApiClient.reset()
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, isError = false)
    }

    fun reset2FAFlow() {
        _uiState.value = _uiState.value.copy(
            requires2FA = false,
            tempToken = null,
            emailMasked = null,
            message = null
        )
    }
}
