package com.yupi.usercenter.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 7132556857756071421L;
    /**
     * 页码
     */
    protected int pageNum = 1;
    /**
     * 每页数量
     */
    protected int pageSize = 10;


}
