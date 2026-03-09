package com.yupi.usercenter.once;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
/*
 * 导入导出用户数据
 */
@Data
public class IndexOrNameData {

    @ExcelProperty("成员编号")
    private String id;

    @ExcelProperty("成员昵称")
    private String username;

}