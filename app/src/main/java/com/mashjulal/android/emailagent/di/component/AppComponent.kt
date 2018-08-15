package com.mashjulal.android.emailagent.di.component

import android.app.Application
import com.mashjulal.android.emailagent.App
import com.mashjulal.android.emailagent.di.module.AppModule
import com.mashjulal.android.emailagent.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AppModule::class
        ]
)
interface AppComponent: AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun create(app: Application):Builder

        fun build(): AppComponent
    }


    fun inject(activity: MainActivity)
}