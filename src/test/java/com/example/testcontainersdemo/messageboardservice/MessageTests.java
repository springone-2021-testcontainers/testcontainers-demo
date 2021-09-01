package com.example.testcontainersdemo.messageboardservice;

import com.example.testcontainersdemo.Message;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

public class MessageTests {

    @Test
    void createMessageWithoutFirstAndUnderscoreAndLastShouldThrowException() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Message("andy", "I am here!"));
    }

    @Test
    void createMessageWithEmptyTextShouldThrowException() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Message("Dani_Rojas", ""));
    }

    @Test
    void createMessageWithValidUsernameAndTextShouldSucceed() {
        assertThatNoException().isThrownBy(() -> new Message("Dani_Rojas", "Football is life!"));
    }
    
}




