package com.mashjulal.android.emailagent.di.module

import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule

@Module(
        includes = [
            AndroidSupportInjectionModule::class,
            ViewModule::class,
            RepositoryModule::class
        ]
)
class AppModule