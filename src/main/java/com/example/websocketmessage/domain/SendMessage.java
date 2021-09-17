package com.example.websocketmessage.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author txl
 * @description
 * @date 2021/9/13 16:19
 */
@Data
public class SendMessage {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private String content;

    private Integer type;

    private Long receiverId;

    private Long sendId;
}
