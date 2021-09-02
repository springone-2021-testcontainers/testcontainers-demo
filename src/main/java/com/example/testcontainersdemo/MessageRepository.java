package com.example.testcontainersdemo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository <Message, Integer> {

    List<Message> findAll();

    Message save(Message newMessage);

}
