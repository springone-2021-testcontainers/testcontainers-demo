package com.example.testcontainersdemo.messageboardservice;

import com.example.testcontainersdemo.Message;
import com.example.testcontainersdemo.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class MessageControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService service;

    @Test
    void getMessagesShouldReturnMessages() throws Exception {
        Message message = new Message("Dani_Rojas", "Football is life!");
        List<Message> messageList = new ArrayList<Message>();
        messageList.add(message);

        BDDMockito.given(this.service.getMessages()).willReturn(messageList);

        String expectedValue = this.objectMapper.writeValueAsString(messageList);
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/message"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedValue));
        verify(this.service, atLeastOnce()).getMessages();
    }

}
