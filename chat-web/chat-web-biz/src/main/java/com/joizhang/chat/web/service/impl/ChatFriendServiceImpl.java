package com.joizhang.chat.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.web.api.constant.FriendRequestStatus;
import com.joizhang.chat.web.api.constant.MessageContentType;
import com.joizhang.chat.web.api.entity.ChatFriend;
import com.joizhang.chat.web.api.entity.ChatMessage;
import com.joizhang.chat.web.mapper.ChatFriendMapper;
import com.joizhang.chat.web.service.ChatFriendService;
import com.joizhang.chat.web.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatFriendServiceImpl extends ServiceImpl<ChatFriendMapper, ChatFriend> implements ChatFriendService {

    private final ChatMessageService messageService;

    @Override
    public boolean exist(ChatFriend chatFriend) {
        LambdaQueryWrapper<ChatFriend> queryWrapper = Wrappers.<ChatFriend>lambdaQuery()
                .eq(ChatFriend::getUserId, chatFriend.getUserId())
                .eq(ChatFriend::getFriendId, chatFriend.getFriendId());
        return baseMapper.exists(queryWrapper);
    }

    @Transactional
    @Override
    public void saveAndSendToMQ(ChatFriend chatFriend) {
        chatFriend.setRequestStatus(FriendRequestStatus.PENDING.getStatus());
        this.save(chatFriend);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSenderId(chatFriend.getUserId());
        chatMessage.setReceiverId(chatFriend.getFriendId());
        chatMessage.setContent(FriendRequestStatus.PENDING.getStatus().toString());
        chatMessage.setContentType(MessageContentType.FRIEND_REQ.getType());
        messageService.sendToMQ(chatMessage);
    }
}
