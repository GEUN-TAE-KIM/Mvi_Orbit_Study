package com.example.domain.usecase

interface ClearAndReloadMessagesUseCase {
    suspend operator fun invoke(): Result<Unit>
}