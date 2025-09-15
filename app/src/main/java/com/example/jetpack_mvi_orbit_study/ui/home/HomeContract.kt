package com.example.jetpack_mvi_orbit_study.ui.home

import com.example.jetpack_mvi_orbit_study.model.Message

/**
 * State: UI에 표시될 데이터를 담는 불변 객체
 * - 모든 UI 상태는 여기에 정의
 */
// 💡 State 설계 원칙
// 1. 불변성 유지 (data class + val)
// 2. UI 렌더링에 필요한 모든 정보 포함
// 3. 기본값 설정으로 초기 상태 명확화
// 4. 단일 책임 원칙 (한 화면의 상태만 관리)
data class HomeState(
    val items: List<Message> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = true
)

/**
 * Intent: 사용자의 의도/액션을 표현
 * - UI에서 발생하는 모든 이벤트
 */
// 💡 Intent 설계 원칙
// 1. sealed interface로 타입 안전성 확보
// 2. 사용자 액션을 명확하게 표현
// 3. 파라미터가 필요한 경우 data class 사용
// 4. 단순한 액션은 object 사용
sealed interface HomeIntent {
    data object Load : HomeIntent
    data object Refresh : HomeIntent
    data class Delete(val id: Int) : HomeIntent
    data object ClearAndReload : HomeIntent
    data object ToggleEmpty : HomeIntent
}

/**
 * SideEffect: 일회성 이벤트
 * - 스낵바, 토스트, 네비게이션 등
 */
// 💡 SideEffect 사용 사례
// - 스낵바 표시
// - 네비게이션
// - 토스트 메시지
// - 다이얼로그 표시
// - 외부 앱 실행 등
sealed interface HomeSideEffect {
    data class ShowSnackBar(val message: String) : HomeSideEffect
    data class ShowError(val message: String) : HomeSideEffect
    data class NavigateToDetail(val messageId: Int) : HomeSideEffect
}
