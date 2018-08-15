package com.mashjulal.android.emailagent.di.module

import com.mashjulal.android.emailagent.ui.main.MainActivity
import com.mashjulal.android.emailagent.ui.messagecontent.MessageContentActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ViewModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun messageContentActivity(): MessageContentActivity
}