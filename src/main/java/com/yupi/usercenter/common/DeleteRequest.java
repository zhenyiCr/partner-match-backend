package com.yupi.usercenter.common;

import java.io.Serializable;
import lombok.Data;

@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = 7618780853976101544L;

    private Long id;
}
