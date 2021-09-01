package com.example.testcontainersdemo.messageboardservice;

import com.example.testcontainersdemo.Message;
import com.example.testcontainersdemo.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTests {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void findAllReturnsMessages() {
        this.messageRepository.save(new Message("Dani_Rojas", "Football is life!"));
        assertThat(this.messageRepository.findAll().size()).isEqualTo(1);
        assertThat(this.messageRepository.findAll().get(0)).isInstanceOf(Message.class);
        assertThat(this.messageRepository.findAll().get(0).getUsername()).isEqualTo("Dani_Rojas");
        assertThat(this.messageRepository.findAll().get(0).getText()).isEqualTo("Football is life!");
    }

}
