package com.yupi.usercenter.model.request;

// [编程学习交流圈](https://www.code-nav.cn/) 快速入门编程不走弯路！30+ 原创学习路线和专栏、500+ 编程学习指南、1000+ 编程精华文章、20T+ 编程资源汇总

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 队伍添加请求体
 *
 */
@Data
public class TeamUpdateRequest implements Serializable {


    private static final long serialVersionUID = 8479235310615309531L;
    /**
     * 队伍 id
     */
    private Long id;
    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id（队长 id）
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;

}
