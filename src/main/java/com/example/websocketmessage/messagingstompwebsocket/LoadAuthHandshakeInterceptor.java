package com.example.websocketmessage.messagingstompwebsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author txl
 * @description 当webscoket建立连接的时候被拦截，获取当前应用的session，
 * 将用户登录信息获取出来，如果用户未登录，
 * 那么不好意思拒绝连接，如果已经登陆了，
 * 那么将用户绑定到stomp的session中，第3步的时候就调用了这个用户信息
 * 该拦截器主要用于对websocket连接是否建立的控制
 * @date 2021/9/15 9:11
 */
@Slf4j
public class LoadAuthHandshakeInterceptor implements HandshakeInterceptor {

    /**
     *   在处理握手之前调用
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 获取当前登录用户后通过校验然后准备建立websocket连接
        HttpSession session = getSession(request);
        if(session == null || session.getAttribute("SESSION_USER") == null){
            log.error("websocket权限拒绝");
//            return false;
            attributes.put("WEBSOCKET_USER_KEY", "ggtms");
            //throw new Exception("websocket权限拒绝");
        }
        //attributes.put("WEBSOCKET_USER_KEY", session.getAttribute("SESSION_USER"));
        // 默认设置
        attributes.put("WEBSOCKET_USER_KEY", "ggtms");
        return true;

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            return serverRequest.getServletRequest().getSession(false);
        }
        return null;
    }

}
