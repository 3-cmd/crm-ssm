package com.cs.crm.workbench.service;

import cn.hutool.db.Page;
import com.cs.crm.commons.ActivityPage;
import com.cs.crm.workbench.domain.Activity;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ActivityService {
    /**
     * 新增市场活动功能
     * @param activity
     * @return
     */
    int saveCreateActivity(Activity activity);
    /**
     * 分页查询市场活动
     * @param activityPage
     * @return
     */
    PageInfo<Activity> queryActivityByPage(ActivityPage activityPage);

    /**
     * 删除选中的市场活动
     * @param ids
     * @return
     */
    int deleteActivityByIds(String[] ids );
    /**
     * 根据id查询市场活动
     * @param id
     * @return
     */
    Activity getById(String id);
    /**
     * 修改市场活动
     * @param activity
     * @return
     */
    int saveEditActivity(Activity activity);
    /**
     * 查询所有的市场活动,为批量导出功能
     */
    List<Activity> queryActivity();
    /**
     * 根据选择的id导出excel文件
     */
    List<Activity> queryActivityByIds(String[] ids);
    int saveCreateByList(List<Activity> activityList);
}
