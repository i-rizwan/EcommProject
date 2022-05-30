package com.infigo.watchsaleapp.network


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


private const val BASE_URL = "https://api.wazirx.com/sapi/v1/"


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun providesBaseUrl(): String {
        return BASE_URL
    }

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()


    @Provides
    @Singleton
    fun providesRetrofitService(retrofit: Retrofit): ApiCommunicator {
        return retrofit.create(ApiCommunicator::class.java)
    }


    @Provides
    @Singleton
    fun providesApiRepository(
        apiService: ApiCommunicator
    ): ApiRepository {
        return ApiRepository(apiService)
    }
}