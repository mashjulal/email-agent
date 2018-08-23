package com.mashjulal.android.emailagent.di.module

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun providesFirebaseDatabase(): FirebaseDatabase
            = FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun providesMailDomainReference(db: FirebaseDatabase): DatabaseReference
            = db.getReference("mail_domain")
}