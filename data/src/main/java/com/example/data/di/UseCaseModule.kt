package com.example.data.di

import com.example.data.usecase.*
import com.example.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindObserveMessagesUseCase(
        impl: ObserveMessagesUseCaseImpl
    ): ObserveMessagesUseCase

    @Binds
    abstract fun bindObserveMessageDetailUseCase(
        impl: ObserveMessageDetailUseCaseImpl
    ): ObserveMessageDetailUseCase

    @Binds
    abstract fun bindRefreshMessagesUseCase(
        impl: RefreshMessagesUseCaseImpl
    ): RefreshMessagesUseCase

    @Binds
    abstract fun bindDeleteMessageUseCase(
        impl: DeleteMessageUseCaseImpl
    ): DeleteMessageUseCase

    @Binds
    abstract fun bindClearAndReloadMessagesUseCase(
        impl: ClearAndReloadMessagesUseCaseImpl
    ): ClearAndReloadMessagesUseCase
}