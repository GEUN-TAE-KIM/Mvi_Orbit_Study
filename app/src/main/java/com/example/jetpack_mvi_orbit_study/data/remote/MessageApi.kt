package com.example.jetpack_mvi_orbit_study.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface MessageApi {
    @GET("posts")
    suspend fun getMessages(): List<MessageDto>

    @GET("posts/{id}")
    suspend fun getMessage(@Path("id") id: Int): MessageDto
}