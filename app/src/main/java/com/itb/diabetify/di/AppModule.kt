package com.itb.diabetify.di

import android.app.Application
import com.itb.diabetify.data.manager.LocalUserManagerImpl
import com.itb.diabetify.data.remote.auth.ApiService
import com.itb.diabetify.data.repository.AuthRepositoryImpl
import com.itb.diabetify.domain.manager.LocalUserManager
import com.itb.diabetify.domain.repository.AuthRepository
import com.itb.diabetify.domain.usecases.app_entry.AppEntryUseCase
import com.itb.diabetify.domain.usecases.app_entry.ReadAppEntry
import com.itb.diabetify.domain.usecases.app_entry.SaveAppEntry
import com.itb.diabetify.domain.usecases.auth.CreateAccountUseCase
import com.itb.diabetify.domain.usecases.auth.SendVerificationUseCase
import com.itb.diabetify.util.Constants.BASE_URL
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

    @Provides
    @Singleton
    fun providesSendVerificationUseCase(
        repository: AuthRepository
    ): SendVerificationUseCase {
        return SendVerificationUseCase(repository)
    }
}