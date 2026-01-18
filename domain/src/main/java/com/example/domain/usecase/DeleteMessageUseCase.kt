package com.example.domain.usecase

import arrow.core.Either
import com.example.domain.error.AppError

interface DeleteMessageUseCase {
    suspend operator fun invoke(id: Int): Either<AppError, Unit>
}