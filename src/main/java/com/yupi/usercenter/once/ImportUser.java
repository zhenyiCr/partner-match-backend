package com.yupi.usercenter.once;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class ImportUser {
    public static void main(String[] args) {
        String fileName = "D:\\work\\伙伴匹配（项目经验）\\user-center-backend-master\\src\\main\\java\\com\\yupi\\usercenter\\once\\1.xlsx";
        List<IndexOrNameData> tableDataList = EasyExcel.read(fileName).head(IndexOrNameData.class).sheet().doReadSync();
        System.out.println("总数:" + tableDataList.size());
        Map<String, List<IndexOrNameData>> listMap = tableDataList.stream()
                .filter(indexOrNameData -> StringUtils.isNotBlank(indexOrNameData.getUsername()))
                .collect(Collectors.groupingBy(IndexOrNameData::getUsername));
        System.out.println(listMap.size());
    }

}
