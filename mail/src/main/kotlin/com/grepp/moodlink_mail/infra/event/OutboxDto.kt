package com.grepp.moodlink_mail.infra.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

data class OutboxDto(
    val eventId:String,
    val eventType:String,
    val sourceService: String,
    val payload: String,
    ) {
}