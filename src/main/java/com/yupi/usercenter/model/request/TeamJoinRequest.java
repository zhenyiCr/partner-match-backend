package com.yupi.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 队伍加入请求体
 *
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = 8479235310615309531L;
    /**
     * 队伍 id
     */
    private Long id;
    /**
     * 密码
     */
    private String password;
}
