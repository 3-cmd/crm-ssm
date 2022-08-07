package com.cs.crm.settings.web.controller;

import cn.hutool.Hutool;
import cn.hutool.core.date.DateUtil;
import com.cs.crm.commons.contants.Contacts;
import com.cs.crm.settings.domain.User;
import com.cs.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cs.crm.commons.domain.Result;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 当用户访问默认的 "/"这个路径时会跳转到indexController,将转发访问WEB-INF/pages下的login.jsp中
 * 当用户跳转到WEB-INF/pages下的login.jsp中时,根据其中的代码,浏览器的地址栏会访问 "settings/qx/user/login.jsp"
 * 这个静态资源,而外部无法访问,所以编写这个controller来转发请求,从内部进行访问
 *
 */
@Controller
public class userController {

    @Autowired
    private UserService userService;
    /**
     * 这个url要和当前这个controller方法所访问的资源路径相同
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        //跳转到登陆页面
        return "settings/qx/user/login";
    }
    @ResponseBody
    @RequestMapping("/settings/qx/user/login")
    public Result<User> login(String loginAct, String loginPwd, boolean isRemPwd, HttpServletRequest request, HttpServletResponse response) throws ParseException {
        Map<String ,String> userMap=new HashMap<>();
        userMap.put("loginAct",loginAct);
        userMap.put("loginPwd",loginPwd);
        User user = userService.queryUserByLoginActAndPwd(userMap);
        if (user==null){
            //用户名或密码错误
            return Result.error("用户名或密码错误");
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //user.getExpiretime()为该用户截至多会而过期
        //将字符串的时间转为上面的格式日期,必须指定simpleDateFormat的格式与字符串时间格式相等
        Date parse = simpleDateFormat.parse(user.getExpiretime());
        long time = parse.getTime();//将转化为日期格式的时间转为毫秒值
        if (time<System.currentTimeMillis()){
            //查看过期日期与当前时间相比,如果数据库中查询的日期小于当前日期证明已经过期
            return Result.error("账户过期");
        }
        if ("0".equals(user.getLockstatus())){
            return Result.error("状态被锁定");
        }
        //查看ip地址是否受限,如果受限登录失败
        if (!user.getAllowips().contains(request.getRemoteAddr())){
            return Result.error("ip受限,无法登录");
        }
        //将用户存储到session中,因为发送ajax请求的页面与展示用户信息的页面不是同一个页面,返回的user数据无法使用
        request.getSession().setAttribute(Contacts.SESSION_USER,user);
        //登陆成功,则查询是否选择了记住密码
        if (isRemPwd){
            //选择记住,则写入cookie
            Cookie c1 = new Cookie("loginAct", user.getLoginact());
            c1.setMaxAge(10*24*60*60);
            response.addCookie(c1);
            Cookie c2 = new Cookie("loginPwd", user.getLoginpwd());
            c2.setMaxAge(10*24*60*60);
            response.addCookie(c2);
        }else {
            //没有选择,或者选择之后重新不选择,则覆盖cookie并删除
            Cookie c1 = new Cookie("loginAct", "");
            c1.setMaxAge(0);
            response.addCookie(c1);
            Cookie c2 = new Cookie("loginPwd", "");
            c2.setMaxAge(0);
            response.addCookie(c2);
        }
        return Result.success(user);
    }

    @RequestMapping("/settings/qx/user/loginOut")
    public String loginOut(HttpServletRequest request,HttpServletResponse response){
        //销毁session
        request.getSession().invalidate();
        //删除cookie
        Cookie c1 = new Cookie("loginAct", "");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd", "");
        c2.setMaxAge(0);
        response.addCookie(c2);
        //跳转到首页
        return "redirect:/";
    }

}
