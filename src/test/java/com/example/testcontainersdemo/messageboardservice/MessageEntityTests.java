package com.example.testcontainersdemo.messageboardservice;

import com.example.testcontainersdemo.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
@ActiveProfiles("test")
class MessageEntityTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void messageCanBePersisted(){
        assertThatNoException().isThrownBy( () -> this.testEntityManager.persist(new Message("Dani_Rojas", "Football is life!")));
    }

}
