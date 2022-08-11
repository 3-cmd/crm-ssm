package com.cs.crm.workbench.web.controller;

import com.cs.crm.commons.ActivityPage;
import com.cs.crm.commons.contants.Contacts;
import com.cs.crm.commons.domain.Result;
import com.cs.crm.commons.utils.UUIDUtils;
import com.cs.crm.settings.domain.User;
import com.cs.crm.workbench.domain.Activity;
import com.cs.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @RequestMapping("/workbench/activity/saveCreateActivity")
    @ResponseBody
    public Result<Activity> saveCreateActivity(Activity activity, HttpServletRequest request){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String format = simpleDateFormat.format(new Date());
        String uuid = UUIDUtils.getUUID();
        User user = (User) request.getSession().getAttribute(Contacts.SESSION_USER);
        activity.setId(uuid);
        activity.setCreatetime(format);
        activity.setCreateby(user.getId());
        try {
            int i = activityService.saveCreateActivity(activity);
            if (i>=1){
                return Result.success(activity);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("系统繁忙,请稍后重试...");
        }
        return Result.error("系统繁忙,请稍后重试...");
    }
    @RequestMapping("workbench/activity/getActivityByPage")
    @ResponseBody
    public Result<PageInfo<Activity>> getActivityByPage(ActivityPage activityPage){
        PageInfo<Activity> pageInfo = activityService.queryActivityByPage(activityPage);
        return Result.success(pageInfo);
    }

    @RequestMapping("workbench/activity/deleteActivityByIds")
    @ResponseBody
    public Result deleteActivityByIds(String[] ids){
        System.out.println(ids);
        try {
            int i = activityService.deleteActivityByIds(ids);
            if (i>=1){
                return Result.success("删除成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("系统繁忙,请稍后重试...");
        }
        return Result.error("系统繁忙,请稍后重试...");
    }
    @RequestMapping("workbench/activity/getActivityById")
    @ResponseBody
    public Result<Activity> getActivityById(String id,HttpServletRequest request){
        Activity activity = activityService.getById(id);
        User user = (User) request.getSession().getAttribute(Contacts.SESSION_USER);
        //设置activity的editBy的值为登陆用户的id
        activity.setEditby(user.getId());
        return Result.success(activity);
    }

    @RequestMapping("workbench/activity/saveEditActivity")
    @ResponseBody
    public Result saveEditActivity(Activity activity,HttpServletRequest request){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String format = simpleDateFormat.format(new Date());
        User user = (User) request.getSession().getAttribute(Contacts.SESSION_USER);
        activity.setEdittime(format);
        activity.setEditby(user.getId());
        try {
            int i = activityService.saveEditActivity(activity);
            if (i>=1){
                return Result.success("修改成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("系统繁忙,请稍后重试...");
        }
        return Result.error("系统繁忙,请稍后重试...");
    }

    /**
     * 全部导出excel文件
     * @param response
     */
    @RequestMapping("workbench/activity/exportAllActivity")
    public void exportAllActivity(HttpServletResponse response) throws IOException {
        List<Activity> activities = activityService.queryActivity();
        //根据查询结果,生成excel文件
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");
        //遍历list,创建每一行的数据
        Activity activity=null;
        if (activities!=null && activities.size()>0){
        for (int i=0;i<activities.size();i++) {
            activity = activities.get(i);
            row = sheet.createRow(i + 1);
            //每一行创建11列
            cell = row.createCell(0);
            cell.setCellValue(activity.getId());
            cell = row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell = row.createCell(2);
            cell.setCellValue(activity.getName());
            cell = row.createCell(3);
            cell.setCellValue(activity.getStartdate());
            cell = row.createCell(4);
            cell.setCellValue(activity.getEnddate());
            cell = row.createCell(5);
            cell.setCellValue(activity.getBudgetcost());
            cell = row.createCell(6);
            cell.setCellValue(activity.getDescription());
            cell = row.createCell(7);
            cell.setCellValue(activity.getCreatetime());
            cell = row.createCell(8);
            cell.setCellValue(activity.getCreateby());
            cell = row.createCell(9);
            cell.setCellValue(activity.getEdittime());
            cell = row.createCell(10);
            cell.setCellValue(activity.getEditby());
        }
        }
        //把excel文件输出
        response.setContentType("application/octet-stream;charset=utf-8");
        //设置相应头,会将文件进行下载
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        OutputStream out = response.getOutputStream();
        wb.write(out);
        wb.close();
        out.flush();
    }

    /**
     *
     */
    @RequestMapping("workbench/activity/exportActivityByIds")
    public void exportActivityByIds(String[] ids,HttpServletResponse response) throws IOException {
        List<Activity> activities = activityService.queryActivityByIds(ids);
        //根据查询结果,生成excel文件
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");
        //遍历list,创建每一行的数据
        Activity activity=null;
        if (activities!=null && activities.size()>0){
            for (int i=0;i<activities.size();i++) {
                activity = activities.get(i);
                row = sheet.createRow(i + 1);
                //每一行创建11列
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartdate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEnddate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getBudgetcost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreatetime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateby());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEdittime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditby());
            }
        }
        //把excel文件输出
        response.setContentType("application/octet-stream;charset=utf-8");
        //设置相应头,会将文件进行下载
        response.addHeader("Content-Disposition","attachment;filename=activityListByIds.xls");
        ServletOutputStream os = response.getOutputStream();
        wb.write(os);
        wb.close();
        os.flush();
    }

    /**
     * 测试导出文件的controller
     * @param response
     * @throws Exception
     */
    @RequestMapping("workbench/activity/fileDownload")
    public void fileDownload(HttpServletResponse response) throws Exception {
        //设置响应信息
        response.setContentType("application/octet-stream;charset=utf-8");
        //设置相应头,会将文件进行下载
        response.addHeader("Content-Disposition","attachment;filename=student.xls");
        //获取需要输出的文件这里我们读取一个excel文件,先创建一个输入流
        FileInputStream is = new FileInputStream("D:\\student.xls");

        byte[] buff=new byte[1024];
        int len;
        //获取输出流
        OutputStream outputStream = response.getOutputStream();
        while ((len=is.read(buff))!=-1){
            outputStream.write(buff,0,len);
        }
        //关闭资源,inputStrean是我们new的,那么就手动关闭
        is.close();
        //这里的outputstrean不是我们创建的,交给服务器来关闭
        outputStream.flush();
    }
}
