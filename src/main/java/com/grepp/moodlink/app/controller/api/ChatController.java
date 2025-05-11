package com.grepp.moodlink.app.controller.api;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.*;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class ChatController {
    private final OllamaChatModel chatModel;

    @Autowired
    public ChatController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String prompt) {
        ChatResponse response = chatModel.call(new Prompt(prompt));
        return response.getResult().getOutput().toString();
    }
}
