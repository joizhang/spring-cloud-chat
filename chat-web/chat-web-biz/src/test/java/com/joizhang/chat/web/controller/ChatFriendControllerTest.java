package com.joizhang.chat.web.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class ChatFriendControllerTest {

    @Test
    void save() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysAgo = now.minusDays(3);
//        System.out.println(Duration.between().get(ChronoUnit.SECONDS) / (24 * 60 * 60));
        System.out.println(LocalDateTimeUtil.between(threeDaysAgo, now, ChronoUnit.SECONDS));
    }
}