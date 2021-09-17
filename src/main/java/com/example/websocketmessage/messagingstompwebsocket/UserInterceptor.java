package com.example.websocketmessage.messagingstompwebsocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

/**
 * @author txl
 * @description
 * @date 2021/9/15 9:07
 */
public class UserInterceptor implements ChannelInterceptor {

    /**
     * preSend里当连接方式为CONNECT的时候获取session里的用户信息，注入stompHeaderAccessor。
     * 注意一点的是用户类需要实现java.security.Principal。preSend有很多连接方式，
     * 包括DISCONNECT,SUBSCRIBE，DISSUBSCRIBE，可以用这些连接方式监控用户的上线下线，
     * 统计每个订阅的在线人数等等
     * @param message
     * @param channel
     * @return
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        //accessor.
        //StompHeaderAccessor accessor2 = (StompHeaderAccessor) headers;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            User user2 = new User();
            // 设置已连接上websocket的用户
            accessor.setUser(user2);
        }
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {

    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return false;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return null;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        // 存储消息
    }

}
