package com.cs.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 当用户访问默认的 "/"这个路径时会跳转到indexController,将转发访问WEB-INF/pages下的login.jsp中
 * 当用户跳转到WEB-INF/pages下的login.jsp中时,根据其中的代码,浏览器的地址栏会访问 "settings/qx/user/login.jsp"
 * 这个静态资源,而外部无法访问,所以编写这个controller来转发请求,从内部进行访问
 *
 */
@Controller
public class userController {
    /**
     * 这个url要和当前这个controller方法所访问的资源路径相同
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        //跳转到登陆页面
        return "settings/qx/user/login";
    }

}
