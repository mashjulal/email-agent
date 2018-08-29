package com.mashjulal.android.emailagent.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Module(
        includes = [
            AndroidSupportInjectionModule::class,
            ViewModule::class,
            RepositoryModule::class,
            RoomModule::class,
            FirebaseModule::class,
            DataSourceModule::class,
            InteractorModule::class,
            UtilsModule::class
        ]
)
class AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(app)

    @Provides
    @Singleton
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideResources(context: Context): Resources = context.resources
}