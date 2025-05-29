package com.itb.diabetify.data.remote.user

import com.itb.diabetify.data.remote.user.request.EditUserRequest
import com.itb.diabetify.data.remote.user.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.PATCH

interface UserApiService {
    @PATCH("users/me")
    suspend fun editUser(
        @Body editUserRequest: EditUserRequest
    ) : UserResponse
}