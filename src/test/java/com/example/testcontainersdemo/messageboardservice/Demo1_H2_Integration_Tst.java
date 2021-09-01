package com.example.testcontainersdemo.messageboardservice;

import com.example.testcontainersdemo.Message;
import com.example.testcontainersdemo.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

// FAILS bc of different in syntax between H2 and Postgres "INTERVAL"
@DataJpaTest(properties = {
        "spring.flyway.enabled=true",
        "spring.flyway.locations=classpath:db/migration,classpath:db/demo1",
        "logging.level.org.flywaydb=debug",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL95Dialect",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL95Dialect",
        "spring.datasource.url=jdbc:h2:~/test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=test",
        "spring.datasource.password=test",
        "spring.datasource.driverClassName=org.h2.Driver"
})

public class Demo1_H2_Integration_Tst {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void findAllReturnsAllMessages() {

        assertThat(this.messageRepository.findAll().size()).isEqualTo(1);
        assertThat(this.messageRepository.findAll().get(0)).isInstanceOf(Message.class);
        assertThat(this.messageRepository.findAll().get(0).getUsername()).isEqualTo("Dani_Rojas");
        assertThat(this.messageRepository.findAll().get(0).getText()).isEqualTo("Football is life!");
    }
}
