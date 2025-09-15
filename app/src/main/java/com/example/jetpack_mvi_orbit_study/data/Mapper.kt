package com.example.jetpack_mvi_orbit_study.data

import com.example.jetpack_mvi_orbit_study.data.local.MessageEntity
import com.example.jetpack_mvi_orbit_study.data.remote.MessageDto
import com.example.jetpack_mvi_orbit_study.model.Message

// DTO → Entity 변환
// API 결과(DTO)를 DB에 저장할 수 있게 변환
fun MessageDto.toEntity(): MessageEntity = MessageEntity(
    id = this.id,
    title = this.title,
    body = this.body
)

// Entity → Domain Model 변환
// DB에서 꺼낸 데이터를 UI에 적합한 형태(Domain Model)로 변환
fun MessageEntity.toDomain(): Message = Message(
    id = this.id,
    title = this.title,
    body = this.body
)

// 💡 Mapper 함수를 만드는 이유
// 1. 각 레이어 간 의존성 분리
// 2. 데이터 구조 변경에 대한 유연성
// 3. 변환 로직 중앙화