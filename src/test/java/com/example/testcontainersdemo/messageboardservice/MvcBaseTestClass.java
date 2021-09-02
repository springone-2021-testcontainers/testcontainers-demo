package com.example.testcontainersdemo.messageboardservice;

import com.example.testcontainersdemo.Message;
import com.example.testcontainersdemo.MessageController;
import com.example.testcontainersdemo.MessageService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

public class MvcBaseTestClass {

    private MessageService service;

    private MessageController controller;

    @BeforeEach
    public void setup() {
        this.service = mock(MessageService.class);
        this.controller = new MessageController(this.service);
        RestAssuredMockMvc.standaloneSetup(this.controller);

        Mockito.when(this.service.getMessages())
                .thenReturn(List.of(new Message(1, "Dani_Rojas", "Football is life!")));

        Mockito.when(this.service.addMessage(eq("Dani_Rojas"),eq("Football is life!")))
                .thenReturn(new Message(1,"Dani_Rojas","Football is life!"));

        Mockito.when(this.service.addMessage(eq("andy"),eq("I am here!")))
                .thenThrow(new IllegalArgumentException("Failure: Name format must be First_Last"));

    }

}
