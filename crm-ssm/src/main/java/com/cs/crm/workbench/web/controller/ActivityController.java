package com.cs.crm.workbench.web.controller;

import com.cs.crm.commons.ActivityPage;
import com.cs.crm.commons.contants.Contacts;
import com.cs.crm.commons.domain.Result;
import com.cs.crm.commons.utils.UUIDUtils;
import com.cs.crm.settings.domain.User;
import com.cs.crm.workbench.domain.Activity;
import com.cs.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
}
