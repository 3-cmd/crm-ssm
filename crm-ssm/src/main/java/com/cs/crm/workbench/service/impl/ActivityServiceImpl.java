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
}
