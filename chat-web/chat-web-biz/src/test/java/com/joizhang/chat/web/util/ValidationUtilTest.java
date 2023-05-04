package com.joizhang.chat.web.util;

import com.joizhang.chat.web.api.entity.ChatMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void validate() {
        ChatMessage chatMessage = new ChatMessage();
        BeanValidationResult result = ValidationUtil.warpValidate(chatMessage);
        assertFalse(result.isSuccess());
        assertEquals(4, result.getErrorMessages().size());
    }
}