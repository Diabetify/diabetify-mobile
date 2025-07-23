package com.itb.diabetify.e2e.repository

import com.itb.diabetify.data.remote.user.request.EditUserRequest
import com.itb.diabetify.domain.model.User
import com.itb.diabetify.domain.repository.UserRepository
import com.itb.diabetify.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepository {

    var shouldFailEditUser = false
    var shouldFailFetchUser = false
    var shouldFailGetUser = false
    var userGender = "Laki-laki"

    override suspend fun getToken(): String? {
        return null
    }

    override suspend fun editUser(editUserRequest: EditUserRequest): Resource<Unit> {
        if (shouldFailEditUser) {
            return Resource.Error("Failed to edit user")
        }
        return Resource.Success(Unit)
    }

    override suspend fun fetchUser(): Resource<Unit> {
        if (shouldFailFetchUser) {
            return Resource.Error("Failed to fetch user")
        }
        return Resource.Success(Unit)
    }

    override fun getUser(): Flow<User?> {
        if (shouldFailGetUser) {
            return flowOf(null)
        }
        return flowOf(
            User(
                name = "Test User",
                email = "test@example.com",
                gender = userGender,
                dob = "01/01/1990"
            )
        )
    }
}
