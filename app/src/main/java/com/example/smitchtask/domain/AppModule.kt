package com.example.smitchtask.domain

import android.content.Context
import android.net.nsd.NsdManager
import com.example.smitchtask.MyApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context):   MyApplication{
        return app as MyApplication
    }
}
