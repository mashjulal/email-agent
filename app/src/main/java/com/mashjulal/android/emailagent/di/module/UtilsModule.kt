package com.mashjulal.android.emailagent.di.module

import com.mashjulal.android.emailagent.data.datasource.impl.remote.StoreUtils
import com.mashjulal.android.emailagent.utils.EmailUtils
import com.mashjulal.android.emailagent.utils.FileUtils
import dagger.Module
import dagger.Provides

@Module
class UtilsModule {

    @Provides
    fun providesEmailUtils(): EmailUtils = EmailUtils

    @Provides
    fun providesStoreUtils(): StoreUtils = StoreUtils

    @Provides
    fun providesFileUtils(): FileUtils = FileUtils
}