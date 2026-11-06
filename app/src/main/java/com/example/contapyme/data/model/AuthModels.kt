package com.example.contapyme.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("nombre_usuario")
    val nombreUsuario: String,
    @SerializedName("contrasena")
    val contrasena: String
)

data class LoginResponse(
    val success: Boolean = false,
    val requires2FA: Boolean = false,
    val message: String? = null,
    val tempToken: String? = null,
    val emailMasked: String? = null,
    val code: String? = null
)

data class Verify2FARequest(
    val tempToken: String,
    val codigo: String
)

data class Verify2FAResponse(
    val success: Boolean = false,
    val message: String? = null,
    val token: String? = null,
    val user: UserDto? = null,
    val code: String? = null
)

data class Resend2FARequest(
    val tempToken: String
)

data class Resend2FAResponse(
    val success: Boolean = false,
    val message: String? = null,
    val code: String? = null
)

data class ForgotPasswordRequest(
    @SerializedName("correo_o_usuario")
    val correoOUsuario: String
)

data class ForgotPasswordResponse(
    val success: Boolean = false,
    val message: String? = null,
    val devToken: String? = null,
    val code: String? = null
)

data class ResetPasswordRequest(
    val token: String,
    @SerializedName("nueva_contrasena")
    val nuevaContrasena: String
)

data class ResetPasswordResponse(
    val success: Boolean = false,
    val message: String? = null,
    val code: String? = null
)

data class ChangePasswordRequest(
    @SerializedName("contrasena_actual")
    val contrasenaActual: String,
    @SerializedName("nueva_contrasena")
    val nuevaContrasena: String
)

data class ChangePasswordResponse(
    val success: Boolean = false,
    val message: String? = null,
    val code: String? = null
)

data class MeResponse(
    val success: Boolean = false,
    val user: UserDto? = null,
    val code: String? = null,
    val message: String? = null
)

data class UserDto(
    @SerializedName("id_usuario")
    val idUsuario: Int = 0,
    val nombre: String? = null,
    @SerializedName("apellido_paterno")
    val apellidoPaterno: String? = null,
    @SerializedName("apellido_materno")
    val apellidoMaterno: String? = null,
    @SerializedName("nombre_completo")
    val nombreCompleto: String? = null,
    @SerializedName("nombre_usuario")
    val nombreUsuario: String? = null,
    val correo: String? = null,
    @SerializedName("id_rol")
    val idRol: Int = 0,
    @SerializedName("rol_nombre")
    val rolNombre: String? = null,
    val estatus: Int = 1,
    val permisos: List<String> = emptyList()
)

data class ApiErrorResponse(
    val success: Boolean = false,
    val message: String? = null,
    val code: String? = null
)
