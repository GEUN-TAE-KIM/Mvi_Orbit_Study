package com.example.domain.usecase

interface RefreshMessagesUseCase {
    suspend operator fun invoke(): Result<Unit>
}