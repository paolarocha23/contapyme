package com.example.contapyme.data.repository

import android.content.Context
import com.example.contapyme.data.api.ApiClient
import com.example.contapyme.data.model.ChangePasswordRequest
import com.example.contapyme.data.model.ChangePasswordResponse
import com.example.contapyme.data.model.ForgotPasswordRequest
import com.example.contapyme.data.model.ForgotPasswordResponse
import com.example.contapyme.data.model.LoginRequest
import com.example.contapyme.data.model.LoginResponse
import com.example.contapyme.data.model.MeResponse
import com.example.contapyme.data.model.Resend2FARequest
import com.example.contapyme.data.model.Resend2FAResponse
import com.example.contapyme.data.model.ResetPasswordRequest
import com.example.contapyme.data.model.ResetPasswordResponse
import com.example.contapyme.data.model.Verify2FARequest
import com.example.contapyme.data.model.Verify2FAResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val context: Context) {
    private fun authApi() = ApiClient.getAuthApi(context)

    suspend fun login(nombreUsuario: String, contrasena: String): Result<LoginResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authApi().login(LoginRequest(nombreUsuario, contrasena))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.success(LoginResponse(success = false, message = parseErrorMessage(errorBody, response.message())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verify2FA(tempToken: String, codigo: String): Result<Verify2FAResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authApi().verify2FA(Verify2FARequest(tempToken, codigo))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.success(Verify2FAResponse(success = false, message = parseErrorMessage(errorBody, response.message())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resend2FA(tempToken: String): Result<Resend2FAResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authApi().resend2FA(Resend2FARequest(tempToken))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.success(Resend2FAResponse(success = false, message = parseErrorMessage(errorBody, response.message())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun forgotPassword(correoOUsuario: String): Result<ForgotPasswordResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authApi().forgotPassword(ForgotPasswordRequest(correoOUsuario))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.success(ForgotPasswordResponse(success = false, message = parseErrorMessage(errorBody, response.message())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(token: String, nuevaContrasena: String): Result<ResetPasswordResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authApi().resetPassword(ResetPasswordRequest(token, nuevaContrasena))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.success(ResetPasswordResponse(success = false, message = parseErrorMessage(errorBody, response.message())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(token: String, contrasenaActual: String, nuevaContrasena: String): Result<ChangePasswordResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authApi().changePassword("Bearer $token", ChangePasswordRequest(contrasenaActual, nuevaContrasena))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.success(ChangePasswordResponse(success = false, message = parseErrorMessage(errorBody, response.message())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMe(token: String): Result<MeResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authApi().getMe("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.success(MeResponse(success = false, message = parseErrorMessage(errorBody, response.message())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseErrorMessage(errorBody: String?, fallback: String): String {
        return try {
            if (errorBody.isNullOrEmpty()) return fallback
            val gson = com.google.gson.Gson()
            val obj = gson.fromJson(errorBody, com.example.contapyme.data.model.ApiErrorResponse::class.java)
            obj.message ?: fallback
        } catch (e: Exception) {
            fallback
        }
    }
}
