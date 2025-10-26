package com.example.presentation.home

import androidx.lifecycle.ViewModel
import com.example.domain.model.Message
import com.example.domain.usecase.ClearAndReloadMessagesUseCase
import com.example.domain.usecase.DeleteMessageUseCase
import com.example.domain.usecase.ObserveMessagesUseCase
import com.example.domain.usecase.RefreshMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val refreshMessagesUseCase: RefreshMessagesUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val clearAndReloadMessagesUseCase: ClearAndReloadMessagesUseCase
) : ContainerHost<HomeState, HomeSideEffect>, ViewModel() {

    // Orbit Container 초기화
    override val container = container<HomeState, HomeSideEffect>(
        initialState = HomeState()
    )

    init {
        // 초기 데이터 로드
        loadInitialData()

        // Room DB 변경사항 실시간 관찰
        observeMessages()
    }

    /**
     * 일반 함수에서 intent 호출
     * - UI 이벤트 처리 진입점
     */
    fun onIntent(intent: HomeIntent) = when (intent) {
        HomeIntent.Load -> loadInitialData()
        HomeIntent.Refresh -> onRefresh()
        is HomeIntent.Delete -> onDeleteMessage(intent.id)
        HomeIntent.ClearAndReload -> onClearAndReload()
        HomeIntent.ToggleEmpty -> onToggleEmptyState()
    }

    /**
     * intent 예제 1: Flow collect로 실시간 데이터 관찰
     * - Room DB의 Flow를 구독하여 데이터 변경 시 자동으로 UI 업데이트
     */
    private fun observeMessages() = intent {
        observeMessagesUseCase().collect { messages ->
            reduce {
                state.copy(
                    items = messages,
                    isEmpty = messages.isEmpty()
                )
            }
        }
    }

    /**
     * intent 예제 2: 초기 데이터 로드
     * - 앱 시작 시 서버에서 최신 데이터 가져오기
     */
    private fun loadInitialData() = intent {
        // reduce: 로딩 상태 시작
        reduce { state.copy(isLoading = true) }

        // 뷰모델에서 비즈니스 로직 호출
        // postSideEffect: 결과에 따른 일회성 이벤트 발생
        refreshMessagesUseCase()
            .onSuccess {
                postSideEffect(HomeSideEffect.ShowSnackBar("Data loaded successfully"))
            }
            .onFailure { error ->
                postSideEffect(HomeSideEffect.ShowError(error.message ?: "Unknown error"))
            }

        // reduce: 로딩 상태 종료
        reduce { state.copy(isLoading = false) }
    }

    /**
     * intent 예제 3: Pull-to-Refresh
     * - 사용자가 당겨서 새로고침할 때 처리
     */
    fun onRefresh() = intent {
        // reduce: 새로고침 상태 시작 (스와이프 인디케이터 표시)
        reduce { state.copy(isRefreshing = true) }

        delay(500) // UX를 위한 최소 딜레이

        // postSideEffect: 새로고침 결과 알림
        refreshMessagesUseCase()
            .onSuccess {
                postSideEffect(HomeSideEffect.ShowSnackBar("Updated!"))
            }
            .onFailure { error ->
                postSideEffect(HomeSideEffect.ShowError("Refresh failed: ${error.message}"))
            }

        // reduce: 새로고침 상태 종료
        reduce { state.copy(isRefreshing = false) }
    }

    /**
     * intent 예제 4: 아이템 삭제 (Optimistic UI Update)
     * - 삭제 버튼 클릭 시 즉시 UI 업데이트 후 서버 요청
     */
    fun onDeleteMessage(id: Int) = intent {
        // 현재 아이템 목록 백업 (실패 시 롤백용)
        val previousItems = state.items

        // reduce: Optimistic UI - 즉시 화면에서 제거
        reduce {
            state.copy(items = state.items.filter { it.id != id })
        }

        // postSideEffect: 삭제 중임을 알림
        postSideEffect(HomeSideEffect.ShowSnackBar("Deleting..."))

        deleteMessageUseCase(id)
            .onSuccess {
                postSideEffect(HomeSideEffect.ShowSnackBar("Message deleted"))
            }
            .onFailure {
                // 실패 시 롤백
                reduce { state.copy(items = previousItems) }
                postSideEffect(HomeSideEffect.ShowError("Failed to delete message"))
            }
    }

    /**
     * intent 예제 5: 여러 작업 순차 실행
     * - 데이터 초기화 후 새로 로드
     */
    fun onClearAndReload() = intent {
        // reduce: 모든 데이터 클리어
        reduce {
            state.copy(
                items = emptyList(),
                isEmpty = true,
                isLoading = true
            )
        }

        // postSideEffect: 클리어 알림
        postSideEffect(HomeSideEffect.ShowSnackBar("Clearing data..."))

        clearAndReloadMessagesUseCase()
            .onSuccess {
                postSideEffect(HomeSideEffect.ShowSnackBar("Data reloaded!"))
            }
            .onFailure {
                postSideEffect(HomeSideEffect.ShowError("Failed to reload"))
            }

        reduce { state.copy(isLoading = false) }
    }

    /**
     * intent 예제 6: 조건부 상태 업데이트
     * - 특정 조건에서만 상태 변경
     */
    fun onToggleEmptyState() = intent {
        if (state.items.isEmpty()) {
            // 비어있을 때는 더미 데이터 표시
            val dummyMessages = listOf(
                Message(id = -1, title = "Sample 1", body = "This is a sample message"),
                Message(id = -2, title = "Sample 2", body = "Another sample message")
            )

            // reduce: 더미 데이터로 업데이트
            reduce {
                state.copy(
                    items = dummyMessages,
                    isEmpty = false
                )
            }

            // postSideEffect: 더미 데이터 표시 알림
            postSideEffect(HomeSideEffect.ShowSnackBar("Showing sample data"))
        } else {
            // reduce: 데이터 클리어
            reduce {
                state.copy(
                    items = emptyList(),
                    isEmpty = true
                )
            }

            // postSideEffect: 클리어 알림
            postSideEffect(HomeSideEffect.ShowSnackBar("Cleared"))
        }
    }
}

// 💡 ViewModel의 역할
// 1. UI 상태 관리 (State)
// 2. 사용자 액션 처리 (Intent)
// 3. 비즈니스 로직 실행
// 4. 외부 효과 발생 (SideEffect)

// 💡 intent { } 블록의 의미
// - 상태 변경과 사이드 이펙트를 안전하게 처리
// - 코루틴 스코프 제공
// - 예외 처리 자동화

// 💡 reduce { } 블록의 의미
// - 현재 상태를 새로운 상태로 변경
// - 불변성 유지하며 상태 업데이트
// - UI 자동 업데이트 트리거