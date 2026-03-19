package com.yupi.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 退出队伍请求体
 */
@Data

public class TeamQuitRequest implements Serializable {
    private static final long serialVersionUID = 2488620741724732180L;

    /**
     * 队伍id
     */
    private Long teamId;
}
