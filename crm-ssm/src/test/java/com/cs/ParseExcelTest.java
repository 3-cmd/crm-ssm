package com.cs;

import com.cs.crm.commons.utils.ParseExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * 使用apache-poi解析excel文件
 */

public class ParseExcelTest {
    public static void main(String[] args) throws IOException {
        //根据excel文件HSSFWorkbook对象,封装了excel文件的所有信息
        InputStream is = new FileInputStream("D:\\aaa.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        //根据wb获取sheet对象
        HSSFSheet sheet = wb.getSheetAt(0);//根据页得下表获取从零开始
        HSSFRow row=null;
        HSSFCell cell=null;
        //根据sheet获取HSSFROW对象,封装了一行的所有数据
        for (int i = 0; i <=sheet.getLastRowNum(); i++) { //sheet.getLastRowNum()最后一行的下标
            row = sheet.getRow(i);
            //根据row对象来获取列对象,一行一列得到一个单元
            for (int j = 0; j < row.getLastCellNum(); j++) {//row.getLastCellNum()最后一列的下表+1
                cell = row.getCell(j);//下表从零开始一次增加
                //获取列中的数据
                cell.getCellType();
            }
        }
    }
    @Test
    public void test1() throws IOException {
        //根据excel文件HSSFWorkbook对象,封装了excel文件的所有信息
        InputStream is = new FileInputStream("D:\\aaa.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row=null;
        HSSFCell cell=null;
        for (int i = 0; i <=sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                System.out.print(ParseExcelUtils.getCellValueForStr(cell) + " ");
            }
        System.out.println();
        }
        }
    @Test
    public void tset2(){
        String a="2000.0";
        double v = Double.parseDouble(a);

        System.out.println(v);

//        int i = Integer.parseInt(a);
//        //long l = Long.parseLong(i);
//        System.out.println(i);
    }
    }

