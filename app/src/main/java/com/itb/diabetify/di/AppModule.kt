package com.itb.diabetify.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.itb.diabetify.data.manager.ActivityManagerImpl
import com.itb.diabetify.data.manager.ConnectivityManagerImpl
import com.itb.diabetify.data.manager.LocalUserManagerImpl
import com.itb.diabetify.data.manager.PredictionJobManagerImpl
import com.itb.diabetify.data.manager.WhatIfJobManagerImpl
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
import com.itb.diabetify.domain.manager.ConnectivityManager
import com.itb.diabetify.domain.manager.LocalUserManager
import com.itb.diabetify.domain.manager.PredictionJobManager
import com.itb.diabetify.domain.manager.WhatIfJobManager
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
import com.itb.diabetify.domain.usecases.activity.UpdateActivityUseCase
import com.itb.diabetify.domain.usecases.app_entry.AppEntryUseCase
import com.itb.diabetify.domain.usecases.app_entry.ReadAppEntry
import com.itb.diabetify.domain.usecases.app_entry.SaveAppEntry
import com.itb.diabetify.domain.usecases.auth.AuthUseCases
import com.itb.diabetify.domain.usecases.auth.ChangePasswordUseCase
import com.itb.diabetify.domain.usecases.auth.CreateAccountUseCase
import com.itb.diabetify.domain.usecases.auth.LoginUseCase
import com.itb.diabetify.domain.usecases.auth.LogoutUseCase
import com.itb.diabetify.domain.usecases.auth.SendVerificationUseCase
import com.itb.diabetify.domain.usecases.auth.VerifyOtpUseCase
import com.itb.diabetify.domain.usecases.connectivity.CheckConnectivityUseCase
import com.itb.diabetify.domain.usecases.connectivity.ConnectivityUseCases
import com.itb.diabetify.domain.usecases.connectivity.ObserveConnectivityUseCase
import com.itb.diabetify.domain.usecases.prediction.GetLatestPredictionUseCase
import com.itb.diabetify.domain.usecases.prediction.GetPredictionByDateUseCase
import com.itb.diabetify.domain.usecases.prediction.GetPredictionScoreByDateUseCase
import com.itb.diabetify.domain.usecases.prediction.PredictAsyncUseCase
import com.itb.diabetify.domain.usecases.prediction.PredictBackgroundUseCase
import com.itb.diabetify.domain.usecases.prediction.PredictUseCase
import com.itb.diabetify.domain.usecases.prediction.PredictionUseCases
import com.itb.diabetify.domain.usecases.profile.AddProfileUseCase
import com.itb.diabetify.domain.usecases.profile.GetProfileUseCase
import com.itb.diabetify.domain.usecases.profile.UpdateProfileUseCase
import com.itb.diabetify.domain.usecases.user.EditUserUseCase
import com.itb.diabetify.domain.usecases.user.GetUserUseCase
import com.itb.diabetify.domain.usecases.notification.NotificationUseCases
import com.itb.diabetify.domain.usecases.notification.ScheduleNotificationUseCase
import com.itb.diabetify.domain.usecases.notification.CancelNotificationUseCase
import com.itb.diabetify.domain.usecases.notification.GetNotificationPreferencesUseCase
import com.itb.diabetify.domain.usecases.notification.SetNotificationPreferencesUseCase
import com.itb.diabetify.domain.manager.NotificationManager
import com.itb.diabetify.data.manager.NotificationManagerImpl
import com.itb.diabetify.domain.usecases.activity.ActivityUseCases
import com.itb.diabetify.domain.usecases.activity.GetActivityRepositoryUseCase
import com.itb.diabetify.domain.usecases.prediction.ExplainPredictionUseCase
import com.itb.diabetify.domain.usecases.prediction.GetLatestPredictionRepositoryUseCase
import com.itb.diabetify.domain.usecases.prediction.WhatIfPredictionUseCase
import com.itb.diabetify.domain.usecases.prediction.WhatIfPredictionAsyncUseCase
import com.itb.diabetify.domain.usecases.prediction.GetWhatIfJobResultUseCase
import com.itb.diabetify.domain.usecases.profile.GetProfileRepositoryUseCase
import com.itb.diabetify.domain.usecases.profile.ProfileUseCases
import com.itb.diabetify.domain.usecases.user.GetUserRepositoryUseCase
import com.itb.diabetify.domain.usecases.user.UserUseCases
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
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
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
    fun providesAuthUseCases(
        repository: AuthRepository
    ): AuthUseCases {
        return AuthUseCases(
            changePassword = ChangePasswordUseCase(repository),
            createAccount = CreateAccountUseCase(repository),
            login = LoginUseCase(repository),
            logout = LogoutUseCase(repository),
            sendVerification = SendVerificationUseCase(repository),
            verifyOtp = VerifyOtpUseCase(repository)
        )
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
    fun providesUserUseCases(
        repository: UserRepository
    ): UserUseCases {
        return UserUseCases(
            getUser = GetUserUseCase(repository),
            getUserRepository = GetUserRepositoryUseCase(repository),
            editUser = EditUserUseCase(repository)
        )
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
    fun providesActivityUseCases(
        repository: ActivityRepository
    ): ActivityUseCases {
        return ActivityUseCases(
            addActivity = AddActivityUseCase(repository),
            getActivityToday = GetActivityTodayUseCase(repository),
            getActivityRepository = GetActivityRepositoryUseCase(repository),
            updateActivity = UpdateActivityUseCase(repository),
        )
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
    fun providesPredictionJobManager(
        predictionApiService: PredictionApiService
    ): PredictionJobManager {
        return PredictionJobManagerImpl(predictionApiService)
    }

    @Provides
    @Singleton
    fun providesWhatIfJobManager(
        predictionApiService: PredictionApiService
    ): WhatIfJobManager {
        return WhatIfJobManagerImpl(predictionApiService)
    }

    @Provides
    @Singleton
    fun providesPredictionRepository(
        predictionApiService: PredictionApiService,
        tokenManager: TokenManager,
        predictionManager: PredictionManager,
        predictionJobManager: PredictionJobManager,
        whatIfJobManager: WhatIfJobManager
    ): PredictionRepository {
        return PredictionRepositoryImpl(
            predictionApiService = predictionApiService,
            tokenManager = tokenManager,
            predictionManager = predictionManager,
            predictionJobManager = predictionJobManager,
            whatIfJobManager = whatIfJobManager
        )
    }

    @Provides
    @Singleton
    fun providesPredictionUseCases(
        repository: PredictionRepository,
    ): PredictionUseCases {
        return PredictionUseCases(
            getLatestPredictionRepository = GetLatestPredictionRepositoryUseCase(repository),
            getLatestPrediction = GetLatestPredictionUseCase(repository),
            getPredictionByDate = GetPredictionByDateUseCase(repository),
            getPredictionScoreByDate = GetPredictionScoreByDateUseCase(repository),
            predict = PredictUseCase(repository),
            predictAsync = PredictAsyncUseCase(repository),
            predictBackground = PredictBackgroundUseCase(repository),
            explainPrediction = ExplainPredictionUseCase(repository),
            whatIfPrediction = WhatIfPredictionUseCase(repository),
            whatIfPredictionAsync = WhatIfPredictionAsyncUseCase(repository),
            getWhatIfJobResult = GetWhatIfJobResultUseCase(repository)
        )
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
    fun provideProfileUseCases(
        repository: ProfileRepository
    ): ProfileUseCases {
        return ProfileUseCases(
            addProfile = AddProfileUseCase(repository),
            getProfile = GetProfileUseCase(repository),
            getProfileRepository = GetProfileRepositoryUseCase(repository),
            updateProfile = UpdateProfileUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun providesNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return NotificationManagerImpl(context)
    }

    @Provides
    @Singleton
    fun providesNotificationUseCases(
        notificationManager: NotificationManager
    ): NotificationUseCases {
        return NotificationUseCases(
            scheduleNotification = ScheduleNotificationUseCase(notificationManager),
            cancelNotification = CancelNotificationUseCase(notificationManager),
            getNotificationPreferences = GetNotificationPreferencesUseCase(notificationManager),
            setNotificationPreferences = SetNotificationPreferencesUseCase(notificationManager)
        )
    }

    @Provides
    @Singleton
    fun providesConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager {
        return ConnectivityManagerImpl(context)
    }

    @Provides
    @Singleton
    fun providesConnectivityUseCases(
        connectivityManager: ConnectivityManager
    ): ConnectivityUseCases {
        return ConnectivityUseCases(
            observeConnectivity = ObserveConnectivityUseCase(connectivityManager),
            checkConnectivity = CheckConnectivityUseCase(connectivityManager)
        )
    }
}