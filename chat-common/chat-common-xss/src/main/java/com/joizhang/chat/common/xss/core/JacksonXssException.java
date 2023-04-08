package com.joizhang.chat.common.xss.core;

import lombok.Getter;

import java.io.IOException;

/**
 * xss jackson 异常
 */
@Getter
public class JacksonXssException extends IOException implements XssException {

    private final String input;

    public JacksonXssException(String input, String message) {
        super(message);
        this.input = input;
    }

}
