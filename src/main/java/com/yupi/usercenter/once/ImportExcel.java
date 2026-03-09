package com.yupi.usercenter.once;

import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 * 导入excel
 */
public class ImportExcel {

    public static void main(String[] args) {

        String fileName = "D:\\work\\伙伴匹配（项目经验）\\user-center-backend-master\\src\\main\\java\\com\\yupi\\usercenter\\once\\1.xlsx";
        complexHeaderRead(fileName);
    }

    public static void readByListener(String fileName) {
        // 监听器
        EasyExcel.read(fileName, IndexOrNameData.class, new TableListener()).sheet().doRead();
    }

    public static void complexHeaderRead(String fileName) {
        // 同步读
        List<IndexOrNameData> tableDataList = EasyExcel.read(fileName).head(IndexOrNameData.class).sheet().doReadSync();
        for (IndexOrNameData indexOrNameData : tableDataList) {
            System.out.println(indexOrNameData);
        }
    }
}
