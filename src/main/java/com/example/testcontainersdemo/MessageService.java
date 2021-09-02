package com.example.testcontainersdemo;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MessageService {

    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public List<Message> getMessages() {
        return this.repository.findAll();
    }

    public Message addMessage(String user, String text) {
        return this.repository.save(new Message(user, text));
    }

}
