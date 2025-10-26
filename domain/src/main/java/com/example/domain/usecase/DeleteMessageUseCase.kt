package com.example.domain.usecase

interface DeleteMessageUseCase {
    suspend operator fun invoke(id: Int): Result<Unit>
}