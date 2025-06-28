package com.example.jetpack_mvi_orbit_study.data

import com.example.jetpack_mvi_orbit_study.data.local.MessageEntity
import com.example.jetpack_mvi_orbit_study.data.remote.MessageDto
import com.example.jetpack_mvi_orbit_study.model.Message

fun MessageDto.toEntity() = MessageEntity(id, title, body)
fun MessageEntity.toDomain() = Message(id, title, body)