package com.drag0n.testcoffee

import android.app.Application
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.drag0n.testcoffee.data.api.ApiCoffee
import com.drag0n.testcoffee.data.sharedPreferense.SharedPreferenseImp
import com.drag0n.testcoffee.data.sharedPreferense.SharedPreferenseRepository
import com.drag0n.testcoffee.domain.geo.GeoLocationImp
import com.drag0n.testcoffee.domain.geo.GeoLocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun providesSharedPreferense(@ApplicationContext context: Context): SharedPreferenseRepository {
        return SharedPreferenseImp(context)
    }

    @Provides
    @Singleton
    fun providesGeo(@ApplicationContext context: Context): GeoLocationRepository {
        return GeoLocationImp(context)
    }



    @Provides
    @Singleton
    fun providesApiCoffee() : ApiCoffee {
        val BASE_URL = "http://212.41.30.90:35005/"
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).client(client)
            .build()
        return retrofit.create(ApiCoffee::class.java)
    }


}

