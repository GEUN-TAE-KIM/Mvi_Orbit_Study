package com.example.domain.usecase

import arrow.core.Either
import com.example.domain.error.AppError
import com.example.domain.model.Message

interface RefreshMessagesUseCase {
    suspend operator fun invoke(): Either<AppError, List<Message>>
}