package com.cs.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
//因为登录页面在WEB-INF下,无法从外部进行访问,只能从内部进行请求的转发,所用此处是进行页面的跳转
//进行页面的跳转不需要加@responseBody,这个注解是返回数据时所用

/**
 * 此处的controller是为了将外部的请求访问到内部登录页面但是
 * 外部请求无法访问WEB-INF下的静态资源,所以此处通过浏览器请求此controller转发请求到
 * 登录页面,因为这个java文件是内部资源,可以访问WEB-INF下的内容
 */
@Controller
public class indexController {
    /**
     理论上,给controller分配url:http://127.0.0.1:9999/
     为了简便,规定协议://ip:port必须省略 用/代表根目录
     */
    @RequestMapping("/")
    public String login(){
        return "login";
    }
}
