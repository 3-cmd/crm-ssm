package com.cs.crm.workbench.service;

import cn.hutool.db.Page;
import com.cs.crm.commons.ActivityPage;
import com.cs.crm.workbench.domain.Activity;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ActivityService {
    int saveCreateActivity(Activity activity);
    PageInfo<Activity> queryActivityByPage(ActivityPage activityPage);
}
