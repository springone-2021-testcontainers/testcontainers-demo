package com.example.testcontainersdemo;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<Message> getMessages() {
        return this.service.getMessages();
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ApiResponse postMessage (@RequestBody CreateMessage newMessage) {
        try {
            Message result = this.service.addMessage(newMessage.getUsername(), newMessage.getText());
            return new ApiResponse(result.getId().toString(),"Success","Create" );
        } catch (Exception e) {
            return new ApiResponse("0", e.getMessage(), "Create");
        }
    }

}
