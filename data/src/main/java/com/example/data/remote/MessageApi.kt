package com.example.data.remote

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface MessageApi {
    @GET("posts")
    suspend fun getMessages(): List<MessageDto>

    @GET("posts/{id}")
    suspend fun getMessage(@Path("id") id: Int): MessageDto

    @DELETE("posts/{id}")
    suspend fun deleteMessage(@Path("id") id: Int)
}

// 💡 Retrofit 어노테이션 설명
// - @GET: HTTP GET 요청
// - suspend: 코루틴 함수 (비동기 처리)
// - @Path: URL 경로 파라미터