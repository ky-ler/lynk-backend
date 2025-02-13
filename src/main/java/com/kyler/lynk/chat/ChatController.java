package com.kyler.lynk.chat;

import com.kyler.lynk.common.StringResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@Tag(name = "Chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<StringResponse> createChat(@RequestParam(name = "sender-id") String senderId,
                                                     @RequestParam(name = "recipient-id") String recipientId) {
        final String chatId = chatService.createChat(senderId, recipientId);
        StringResponse stringResponse = new StringResponse(chatId);

        return ResponseEntity.ok(stringResponse);
    }

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getChatsByRecipient(Authentication authentication) {
        return ResponseEntity.ok(chatService.getChatsByRecipientId(authentication));
    }
}
