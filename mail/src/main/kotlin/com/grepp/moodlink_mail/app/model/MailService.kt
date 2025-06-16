package com.grepp.moodlink_mail.app.model

import com.grepp.moodlink_mail.app.controller.payload.SmtpRequest
import com.grepp.moodlink_mail.app.model.code.MailTemplatePath
import com.grepp.moodlink_mail.infra.mail.MailTemplate
import com.grepp.moodlink_mail.infra.mail.SmtpDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MailService(
    private val template: MailTemplate
) {
    private val log = LoggerFactory.getLogger(javaClass)

    val semaphore = Semaphore(3)

    fun send(request: SmtpRequest) = runBlocking{
        request.to.forEach {
            launch(Dispatchers.IO) {
                val dto = SmtpDto(
                    templatePath = MailTemplatePath
                        .valueOf(request.eventType.uppercase()).path,
                    from = request.from,
                    to = it,
                    subject = request.subject,
                    properties = request.properties
                )
                semaphore.withPermit {
                    template.send(dto)
                }
            }
        }
    }
}