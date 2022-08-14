package com.cs.crm.workbench.web.controller;

import com.cs.crm.commons.ActivityPage;
import com.cs.crm.commons.contants.Contacts;
import com.cs.crm.commons.domain.Result;
import com.cs.crm.commons.utils.ParseExcelUtils;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @RequestMapping("workbench/activity/importActivity")
    @ResponseBody
    public Result importActivity(MultipartFile activityFile,HttpServletRequest request){
        try {
            User user= (User) request.getSession().getAttribute(Contacts.SESSION_USER);
            //获取前段传递过来的文件,获取这个文件的输入流
            InputStream is = activityFile.getInputStream();
            //解析excel文件,获取文件中的数据
            HSSFWorkbook wb=new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row=null;
            HSSFCell cell=null;
            List<Activity> list=new ArrayList<>();
            //数据只能从excel文件中一行一行的取出来,而每一行代表着一个用户的全部信息
            for (int i = 1; i <=sheet.getLastRowNum(); i++) {
                row=sheet.getRow(i);
                //一行代表一个用户,为该用户设置无法手动录入excel文件的字段
                Activity activity=new Activity();//每一行创建一个对象,将这些对象加入list集合中去,批量插入即可
                activity.setId(UUIDUtils.getUUID());
                //此处的所有者我们规定,谁导入则谁是该活动的所有者
                activity.setOwner(user.getId());
                activity.setCreateby(user.getId());
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                activity.setCreatetime(sdf.format(new Date()));
                //获取excel表中存在的每一个字段的值,此时的每一个字段的值是有顺序的,如果不按规定,则无法倒入成功
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell=row.getCell(j);
                    if (cell.equals("") || cell==null){
                        break;
                    }
                    if (j==0){
                        activity.setName(ParseExcelUtils.getCellValueForStr(cell));
                    }else if (j==1){
                        activity.setStartdate(ParseExcelUtils.getCellValueForStr(cell));
                    }else if (j==2){
                        activity.setEnddate(ParseExcelUtils.getCellValueForStr(cell));
                    }else if (j==3){
                        activity.setBudgetcost(Double.parseDouble(ParseExcelUtils.getCellValueForStr(cell)));
                    }else if (j==4){
                        activity.setDescription(ParseExcelUtils.getCellValueForStr(cell));
                    }
                }
                list.add(activity);
            }
            int ret = activityService.saveCreateByList(list);
            return Result.success("导入"+ret+"条数据");
        }catch (IOException e){
            e.printStackTrace();
            return Result.error("系统繁忙,请稍后重试...");
        }

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

    /**
     * 文件上传功能测试
     * 配置文件上传解析器
     * @param userName
     * @param myFile
     * @return
     */
    @RequestMapping("workbench/activity/fileUpLoadTest")
    @ResponseBody
    public Result fileUpLoadTest(String userName, MultipartFile myFile) throws Exception{
        //把文本数据打印到控制台
        System.out.println("userName:"+userName );
        InputStream is = myFile.getInputStream();
        System.out.println(is);
        //把文件在服务指定的目录中生成一个同样的文件
        myFile.transferTo(new File("D:\\",myFile.getOriginalFilename()));
        return Result.success("成功");
    }
}
