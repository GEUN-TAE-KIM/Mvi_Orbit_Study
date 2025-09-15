package com.example.jetpack_mvi_orbit_study.data

import com.example.jetpack_mvi_orbit_study.data.local.MessageDao
import com.example.jetpack_mvi_orbit_study.data.remote.MessageApi
import com.example.jetpack_mvi_orbit_study.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val api: MessageApi,
    private val dao: MessageDao
) {
    // 로컬 데이터베이스에서 메시지 목록 가져오기
    val messages: Flow<List<Message>> =
        dao.getAllFlow()
            .map { entities -> entities.map { it.toDomain() } }
            .distinctUntilChanged()  // 중복 방출 방지

    // 특정 ID의 메시지 가져오기
    fun message(id: Int): Flow<Message?> =
        dao.findFlow(id).map { it?.toDomain() }

    // 원격 서버에서 데이터 가져와서 로컬 DB에 저장
    suspend fun refresh(): Result<Unit> = runCatching {
        val remoteMessages = api.getMessages()
        val entities = remoteMessages.map { it.toEntity() }
        dao.insertAll(entities)
    }
}

// 💡 Repository 패턴의 장점
// 1. 데이터 소스 추상화 (API/DB 구분 없이 사용)
// 2. 캐싱 전략 구현 (오프라인 우선, 온라인 우선 등)
// 3. 테스트 용이성 (Mock Repository 쉽게 생성)
// 4. 단일 책임 원칙 (데이터 접근 로직만 담당)

// 💡 distinctUntilChanged() 사용 이유
// - 같은 데이터가 연속으로 방출되는 것을 방지
// - UI 불필요한 recomposition 방지
// - 성능 최적화

// 💡 Result<Unit> 사용 이유
// - 성공/실패 명확하게 표현
// - 예외 처리를 호출하는 쪽에서 결정
// - 함수형 프로그래밍 스타일