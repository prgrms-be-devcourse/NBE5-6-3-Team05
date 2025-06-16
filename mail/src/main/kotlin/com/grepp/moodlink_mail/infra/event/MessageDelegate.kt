package com.grepp.moodlink_mail.infra.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.grepp.moodlink_mail.app.model.code.MailTemplatePath
import com.grepp.moodlink_mail.infra.mail.MailTemplate
import com.grepp.moodlink_mail.infra.mail.SmtpDto
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


interface MessageDelegate {
    fun handleMessage(message: String)
}

@Component
class EventMessageDelegate(
    private val mailTemplate: MailTemplate,
    private val objectMapper: ObjectMapper
) : MessageDelegate {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun handleMessage(message: String) = runBlocking {
        log.info(message)
        val outbox = objectMapper.readValue(message, OutboxDto::class.java)
        log.info("{}",outbox)
        if(outbox.eventType.uppercase() == MailTemplatePath.SIGNUP_COMPLETE.name){
            sendSignupCompleteMail(outbox)
        }
        if(outbox.eventType.uppercase() == MailTemplatePath.SIGNUP_VERIFY.name){
            sendSignupVerifyMail(outbox)
        }

    }

    private suspend fun sendSignupVerifyMail(outbox: OutboxDto) {
        val email = outbox.payload.split(",")[0]
        val token = outbox.payload.split(",")[1]
        val dto = SmtpDto(
            from = "moodlink",
            to = email,
            subject = "회원 가입을 완료해주세요.",
            templatePath = MailTemplatePath.SIGNUP_VERIFY.path,
            properties = mutableMapOf(
                "token" to token,
                "domain" to "http://localhost:8080")
        )
        mailTemplate.send(dto)
    }


    private suspend fun sendSignupCompleteMail(outbox: OutboxDto) {
        val dto = SmtpDto(
            from = "grepp",
            to = outbox.payload,
            subject = "환영합니다! 고객님",
            templatePath = MailTemplatePath.SIGNUP_COMPLETE.path
        )
        mailTemplate.send(dto)
    }
}