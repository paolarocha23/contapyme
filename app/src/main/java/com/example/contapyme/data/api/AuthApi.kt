package com.example.contapyme.data.api

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/verify-2fa")
    suspend fun verify2FA(@Body request: Verify2FARequest): Response<Verify2FAResponse>

    @POST("auth/resend-2fa")
    suspend fun resend2FA(@Body request: Resend2FARequest): Response<Resend2FAResponse>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ForgotPasswordResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>

    @POST("auth/change-password")
    suspend fun changePassword(
        @Header("Authorization") authorization: String,
        @Body request: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

    @GET("auth/me")
    suspend fun getMe(@Header("Authorization") authorization: String): Response<MeResponse>
}
