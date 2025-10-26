package com.example.data.di

import com.example.data.remote.MessageApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)  // 앱 전체에서 단일 인스턴스 사용
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())  // 코틀린 data class 직렬화 지원
            .build()

    // 💡 왜 Moshi인가?
    // - Gson보다 빠름 (코틀린 특화)
    // - 코틀린 null safety 지원
    // - 더 적은 메모리 사용

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // 💡 HttpLoggingInterceptor 레벨 설명
    // - NONE: 로그 없음
    // - BASIC: 요청/응답 라인만
    // - HEADERS: 헤더 포함
    // - BODY: 전체 요청/응답 내용

    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://www.naver.com/")  // 실제 API URL
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideMessageApi(retrofit: Retrofit): MessageApi =
        retrofit.create(MessageApi::class.java)
}