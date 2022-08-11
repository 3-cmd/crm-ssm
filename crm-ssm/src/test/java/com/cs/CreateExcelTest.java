package com.cs;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 使用apache的poi来生成excel文件
 */
public class CreateExcelTest {
    @Test
    public void test() throws IOException {
        //创建HSSFWorkbook对象,来创建一个excel文件
        HSSFWorkbook wb=new HSSFWorkbook();
        //使用HSSFWorkbook对象来创建HSSFSheet对象,对应excel中的一页
        HSSFSheet sheet = wb.createSheet("学生列表");
        //使用sheet来创建HSSFROW,对应sheet中的一行
        HSSFRow row = sheet.createRow(0);//行号,从零开始
        //使用row对象来创建一个列,代表该行的那一列,行和列可以定义一个单元格
        HSSFCell cell = row.createCell(0);//列的编号,从0开始
        cell.setCellValue("学号");
        cell=row.createCell(1);
        cell.setCellValue("姓名");
        cell=row.createCell(2);
        cell.setCellValue("年龄");
        for (int i = 1; i <= 10; i++) {
            row=sheet.createRow(i);
            cell=row.createCell(0);
            cell.setCellValue(100+i);
            cell=row.createCell(1);
            cell.setCellValue("NAME"+i);
            cell=row.createCell(2);
            cell.setCellValue(20+i);
        }
        //生成excel文件
        OutputStream os=new FileOutputStream("d:\\student.xls");
        wb.write(os);
        os.close();
        wb.close();
        System.out.println("============create ok===========");
    }
}
