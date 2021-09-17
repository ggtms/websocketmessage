package com.example.websocketmessage.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author txl
 * @description
 * @date 2021/9/13 11:16
 */
@TableName("message")
@Data
public class Message {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String title;

    private String content;

    private Integer type;

    private Long receiverId;

    private Long sendId;

    private Integer state;

}
