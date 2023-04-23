package com.joizhang.chat.web.api.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageContentType {

    ERROR(0, "Error"),

    TEXT(1, "Text and emoji"),

    IMAGE(2, "Image"),

    VIDEO(3, "Video"),

    AUDIO(4, "Audio"),

    FRIEND_REQ(5, "Friend request");

    private final int type;

    private final String description;
}
