package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by seu on 2017/5/31.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;

    //请求之前执行，多用于身份验证、权限控制
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket=null;
        if(httpServletRequest.getCookies()!=null){
            //请求携带了cookies，遍历各cookie，查看是否含有名为ticket的cookie
            for(Cookie cookie:httpServletRequest.getCookies()){
                if(cookie.getName().equals("ticket")){
                    //含有名为ticket的cookie，取该cookie的值
                    ticket=cookie.getValue();
                    break;
                }
            }
        }

        if(ticket!=null){
            LoginTicket loginTicket =loginTicketDAO.selectByTicket(ticket);
            if(loginTicket==null//该ticket无效，没有与其相关联的user
                    || loginTicket.getExpired().before(new Date())//尚处于有效期之内
                    || loginTicket.getStatus()!=0){//尚且有效
                return true;//返回true请求继续执行，否则请求中断
            }
            //该ticket有效，获取与其相关联的user
            User user=userDAO.selectById(loginTicket.getUserId());

            //将user注入hostHolder，方便在其他位置访问
            hostHolder.setUser(user);
        }
        return true;
    }

    //渲染之前执行
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //如此一来可以在视图velocity中使用该user
        if(modelAndView!=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
