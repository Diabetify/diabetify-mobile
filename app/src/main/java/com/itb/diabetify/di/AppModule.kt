package com.itb.diabetify.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.itb.diabetify.data.manager.ActivityManagerImpl
import com.itb.diabetify.data.manager.LocalUserManagerImpl
import com.itb.diabetify.data.manager.PredictionManagerImpl
import com.itb.diabetify.data.manager.ProfileManagerImpl
import com.itb.diabetify.data.manager.TokenManagerImpl
import com.itb.diabetify.data.manager.UserManagerImpl
import com.itb.diabetify.data.remote.activity.ActivityApiService
import com.itb.diabetify.data.remote.auth.AuthApiService
import com.itb.diabetify.data.remote.interceptor.AuthInterceptor
import com.itb.diabetify.data.remote.prediction.PredictionApiService
import com.itb.diabetify.data.remote.profile.ProfileApiService
import com.itb.diabetify.data.remote.user.UserApiService
import com.itb.diabetify.data.repository.ActivityRepositoryImpl
import com.itb.diabetify.data.repository.AuthRepositoryImpl
import com.itb.diabetify.data.repository.PredictionRepositoryImpl
import com.itb.diabetify.data.repository.ProfileRepositoryImpl
import com.itb.diabetify.data.repository.UserRepositoryImpl
import com.itb.diabetify.domain.manager.LocalUserManager
import com.itb.diabetify.domain.manager.TokenManager
import com.itb.diabetify.domain.manager.UserManager
import com.itb.diabetify.domain.manager.ActivityManager
import com.itb.diabetify.domain.manager.PredictionManager
import com.itb.diabetify.domain.manager.ProfileManager
import com.itb.diabetify.domain.repository.ActivityRepository
import com.itb.diabetify.domain.repository.AuthRepository
import com.itb.diabetify.domain.repository.PredictionRepository
import com.itb.diabetify.domain.repository.ProfileRepository
import com.itb.diabetify.domain.repository.UserRepository
import com.itb.diabetify.domain.usecases.activity.AddActivityUseCase
import com.itb.diabetify.domain.usecases.activity.GetActivityTodayUseCase
import com.itb.diabetify.domain.usecases.app_entry.AppEntryUseCase
import com.itb.diabetify.domain.usecases.app_entry.ReadAppEntry
import com.itb.diabetify.domain.usecases.app_entry.SaveAppEntry
import com.itb.diabetify.domain.usecases.auth.ChangePasswordUseCase
import com.itb.diabetify.domain.usecases.auth.CreateAccountUseCase
import com.itb.diabetify.domain.usecases.auth.GoogleLoginUseCase
import com.itb.diabetify.domain.usecases.auth.LoginUseCase
import com.itb.diabetify.domain.usecases.auth.LogoutUseCase
import com.itb.diabetify.domain.usecases.auth.SendVerificationUseCase
import com.itb.diabetify.domain.usecases.auth.VerifyOtpUseCase
import com.itb.diabetify.domain.usecases.prediction.GetLatestPredictionUseCase
import com.itb.diabetify.domain.usecases.profile.GetProfileUseCase
import com.itb.diabetify.domain.usecases.user.EditUserUseCase
import com.itb.diabetify.domain.usecases.user.GetUserUseCase
import com.itb.diabetify.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesLocalUserManager(
        application: Application
    ): LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun providesAppEntryUseCases(
        localUserManager: LocalUserManager
    ) = AppEntryUseCase(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )

    @Provides
    @Singleton
    fun providesTokenManager(
        @ApplicationContext context: Context
    ): TokenManager {
        return TokenManagerImpl(context)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesAuthApiService(okHttpClient: OkHttpClient): AuthApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(
        authApiService: AuthApiService,
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepositoryImpl(
            authApiService = authApiService,
            tokenManager = tokenManager
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
        repository: AuthRepository,
    ): SendVerificationUseCase {
        return SendVerificationUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesVerifyOtpUseCase(
        repository: AuthRepository
    ): VerifyOtpUseCase {
        return VerifyOtpUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesLoginUseCase(
        repository: AuthRepository
    ): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesGoogleLoginUseCase(
        repository: AuthRepository
    ): GoogleLoginUseCase {
        return GoogleLoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesChangePasswordUseCase(
        repository: AuthRepository
    ): ChangePasswordUseCase {
        return ChangePasswordUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesLogoutUseCase(
        repository: AuthRepository
    ): LogoutUseCase {
        return LogoutUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun providesUserApiService(okHttpClient: OkHttpClient): UserApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesUserManager(
        @ApplicationContext context: Context,
        gson: Gson
    ): UserManager {
        return UserManagerImpl(context, gson)
    }

    @Provides
    @Singleton
    fun providesUserRepository(
        userApiService: UserApiService,
        tokenManager: TokenManager,
        userManager: UserManager
    ): UserRepository {
        return UserRepositoryImpl(
            userApiService = userApiService,
            tokenManager = tokenManager,
            userManager = userManager
        )
    }

    @Provides
    @Singleton
    fun providesGetUserUseCase(
        repository: UserRepository
    ): GetUserUseCase {
        return GetUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesEditUserUseCase(
        repository: UserRepository
    ): EditUserUseCase {
        return EditUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesActivityApiService(okHttpClient: OkHttpClient): ActivityApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ActivityApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesActivityManager(): ActivityManager {
        return ActivityManagerImpl()
    }

    @Provides
    @Singleton
    fun providesActivityRepository(
        activityApiService: ActivityApiService,
        tokenManager: TokenManager,
        activityManager: ActivityManager
    ): ActivityRepository {
        return ActivityRepositoryImpl(
            activityApiService = activityApiService,
            tokenManager = tokenManager,
            activityManager = activityManager
        )
    }

    @Provides
    @Singleton
    fun providesAddActivityUseCase(
        repository: ActivityRepository
    ): AddActivityUseCase {
        return AddActivityUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesGetActivityByDateUseCase(
        repository: ActivityRepository
    ): GetActivityTodayUseCase {
        return GetActivityTodayUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesPredictionApiService(okHttpClient: OkHttpClient): PredictionApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PredictionApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesPredictionManager(): PredictionManager {
        return PredictionManagerImpl()
    }

    @Provides
    @Singleton
    fun providesPredictionRepository(
        predictionApiService: PredictionApiService,
        tokenManager: TokenManager,
        predictionManager: PredictionManager
    ): PredictionRepository {
        return PredictionRepositoryImpl(
            predictionApiService = predictionApiService,
            tokenManager = tokenManager,
            predictionManager = predictionManager
        )
    }

    @Provides
    @Singleton
    fun providesGetLatestPredictionUseCase(
        repository: PredictionRepository
    ): GetLatestPredictionUseCase {
        return GetLatestPredictionUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesProfileApiService(okHttpClient: OkHttpClient): ProfileApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesProfileManager(): ProfileManager {
        return ProfileManagerImpl()
    }

    @Provides
    @Singleton
    fun providesProfileRepository(
        profileApiService: ProfileApiService,
        tokenManager: TokenManager,
        profileManager: ProfileManager
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            profileApiService = profileApiService,
            tokenManager = tokenManager,
            profileManager = profileManager
        )
    }

    @Provides
    @Singleton
    fun providesGetProfileUseCase(
        repository: ProfileRepository
    ): GetProfileUseCase {
        return GetProfileUseCase(repository)
    }
}