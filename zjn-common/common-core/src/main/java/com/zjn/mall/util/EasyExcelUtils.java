package com.zjn.mall.util;

import com.alibaba.excel.EasyExcel;
import com.zjn.mall.domain.Order;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName EasyExcelUtils
 * @Description EasyExcel工具类
 * @createTime 2024年09月13日 09:27:00
 */
public class EasyExcelUtils {
    public static <T> void exportExcel(String fileName, String sheetName, Class<T> head, List<T> data) {
        fileName = fileName + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, head).sheet(sheetName).doWrite(data);
    }
}
