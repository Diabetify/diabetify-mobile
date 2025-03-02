package com.example.diabetify.di

import android.app.Application
import com.example.diabetify.data.manager.LocalUserManagerImpl
import com.example.diabetify.domain.manager.LocalUserManager
import com.example.diabetify.domain.usecases.AppEntryUseCases
import com.example.diabetify.domain.usecases.ReadAppEntry
import com.example.diabetify.domain.usecases.SaveAppEntry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    ) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )
}