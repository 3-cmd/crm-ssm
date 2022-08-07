package com.cs.crm.settings.web.interceptor;

import com.cs.crm.commons.contants.Contacts;
import com.cs.crm.settings.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 登陆方法之前调用该方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //查看session中是否存在用户
        User user = (User) request.getSession().getAttribute(Contacts.SESSION_USER);
        if (user==null){
            //String contextPath = request.getContextPath();
            //System.out.println(contextPath);
            response.sendRedirect("/");//跳转到登陆首页
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
