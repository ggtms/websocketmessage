package com.example.websocketmessage.messagingstompwebsocket;

import lombok.Data;

import java.security.Principal;

/**
 * @author txl
 * @description 在该类中设置用户信息
 * @date 2021/9/15 9:32
 */
public class User implements Principal {

    @Override
    public String getName() {
        // 获取用户唯一标识信息
        return "test";
    }


}
