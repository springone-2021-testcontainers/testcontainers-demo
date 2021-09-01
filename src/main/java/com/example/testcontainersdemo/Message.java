package com.example.testcontainersdemo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    private Integer id;

    private String username;

    private String text;

    public Message(String username, String text) {
        // Check username
        Assert.hasText(username, () -> "Failure: Name format must be First_Last");
        username = username.trim();
        int underscoreIndex = username.indexOf("_");
        Assert.isTrue(underscoreIndex > 0 && underscoreIndex < username.length() - 1, () -> "Failure: Name format must be First_Last");
        // Check text
        Assert.hasText(text, () -> "Failure: Text must not be blank");
        text = text.trim();

        this.username = username;
        this.text = text;
    }

}
