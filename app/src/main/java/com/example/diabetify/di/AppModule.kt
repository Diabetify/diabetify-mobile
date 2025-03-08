package com.example.diabetify.di

import android.app.Application
import com.example.diabetify.data.manager.LocalUserManagerImpl
import com.example.diabetify.data.remote.auth.ApiService
import com.example.diabetify.data.repository.AuthRepositoryImpl
import com.example.diabetify.domain.manager.LocalUserManager
import com.example.diabetify.domain.repository.AuthRepository
import com.example.diabetify.domain.usecases.app_entry.AppEntryUseCase
import com.example.diabetify.domain.usecases.app_entry.ReadAppEntry
import com.example.diabetify.domain.usecases.app_entry.SaveAppEntry
import com.example.diabetify.domain.usecases.auth.CreateAccountUseCase
import com.example.diabetify.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLocalUserManager(
        application: Application
    ): LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManager: LocalUserManager
    ) = AppEntryUseCase(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )

    @Provides
    @Singleton
    fun providesApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(
        apiService: ApiService
    ): AuthRepository {
        return AuthRepositoryImpl(
            apiService = apiService
        )
    }

    @Provides
    @Singleton
    fun providesCreateAccountUseCase(
        repository: AuthRepository
    ): CreateAccountUseCase {
        return CreateAccountUseCase(repository)
    }
}