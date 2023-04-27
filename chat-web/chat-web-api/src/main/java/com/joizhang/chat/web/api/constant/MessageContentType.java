package com.joizhang.chat.web.api.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息类型
 */
@Getter
@RequiredArgsConstructor
public enum MessageContentType {

    ERROR(1, "Error"),

    TEXT(2, "Text and emoji"),

    IMAGE(3, "Image"),

    VIDEO(4, "Video"),

    AUDIO(5, "Audio"),

    /**
     * {@see com.joizhang.chat.web.api.constant.FriendRequestStatus}
     */
    FRIEND_REQ(5, "Friend request");

    private final Integer type;

    private final String description;
}
