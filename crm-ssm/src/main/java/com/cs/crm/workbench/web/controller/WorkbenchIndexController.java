package com.cs.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkbenchIndexController {
    /**
     * 登陆成功后,跳转业务主界面
     * @return
     */
    @RequestMapping("/workbench/index.do")
    public String index(){

        return "workbench/index";
    }

}
