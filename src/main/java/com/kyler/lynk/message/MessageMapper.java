package com.kyler.lynk.message;

import com.kyler.lynk.file.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class MessageMapper {
    public MessageResponse toMessageResponse(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setContent(message.getContent());
        response.setType(message.getType());
        response.setState(message.getState());
        response.setSenderId(message.getSenderId());
        response.setRecipientId(message.getRecipientId());
        response.setCreatedAt(message.getCreatedDate());
        response.setMedia(FileUtils.readFileFromLocation(message.getMediaFilePath()));
        return response;
    }
}
