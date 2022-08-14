package com.cs.crm.workbench.service.impl;

import com.cs.crm.commons.ActivityPage;
import com.cs.crm.workbench.domain.Activity;
import com.cs.crm.workbench.mapper.ActivityMapper;
import com.cs.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Override
    public int saveCreateActivity(Activity activities) {
        return activityMapper.insert(activities);
    }

    @Override
    public PageInfo<Activity> queryActivityByPage(ActivityPage activityPage) {
        PageHelper.startPage(activityPage.getPageNumber(),activityPage.getPageSize());
        List<Activity> activities = activityMapper.selectByPage(activityPage);
        PageInfo<Activity> pageInfo=new PageInfo<>(activities);
        return pageInfo;
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteByActivityIds(ids);
    }

    @Override
    public Activity getById(String id) {
        Activity activity = activityMapper.selectByPrimaryKey(id);
        return activity;
    }

    @Override
    public int saveEditActivity(Activity activity) {
        return activityMapper.updateByPrimaryKey(activity);
    }

    @Override
    public List<Activity> queryActivity() {
        List<Activity> activities = activityMapper.selectAllActivity();
        return activities;
    }

    @Override
    public List<Activity> queryActivityByIds(String[] ids) {
        return activityMapper.selectActivityByIds(ids);
    }

    @Override
    public int saveCreateByList(List<Activity> activityList) {
        return activityMapper.saveActivitiesByList(activityList);
    }
}
