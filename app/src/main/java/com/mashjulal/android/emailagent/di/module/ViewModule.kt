package com.mashjulal.android.emailagent.di.module

import com.mashjulal.android.emailagent.ui.auth.AuthActivity
import com.mashjulal.android.emailagent.ui.auth.input.AuthFormFragment
import com.mashjulal.android.emailagent.ui.main.MainActivity
import com.mashjulal.android.emailagent.ui.messagecontent.MessageContentActivity
import com.mashjulal.android.emailagent.ui.splash.SplashScreenActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ViewModule {

    @ContributesAndroidInjector
    abstract fun authActivity(): AuthActivity

    @ContributesAndroidInjector
    abstract fun authFormFragment(): AuthFormFragment

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun messageContentActivity(): MessageContentActivity

    @ContributesAndroidInjector
    abstract fun splashScreenActivity(): SplashScreenActivity
}