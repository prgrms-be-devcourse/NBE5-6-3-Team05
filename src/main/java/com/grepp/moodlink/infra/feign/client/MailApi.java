package com.grepp.moodlink.infra.feign.client;


import com.grepp.moodlink.app.model.member.dto.SmtpDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name="moodlink-mail",
    url="http://localhost:8081/mail/"
)
public interface MailApi {
    @PostMapping("send")
    void sendMail(
        @RequestHeader(name = "x-member-id")
        String userId,
        @RequestHeader(name = "x-member-role")
        String role,
        @RequestBody
        SmtpDto payload
    );
}
