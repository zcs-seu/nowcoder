package com.nowcoder.controller;

import com.nowcoder.model.ViewObject;
import com.nowcoder.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sun.security.krb5.internal.Ticket;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by seu on 2017/5/31.
 */
@Controller
public class LogininController {
    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(IndexContoller.class);

    @RequestMapping(path = {"/reg/"},method = {RequestMethod.POST,RequestMethod.GET})
    public String reg(Model model,
                      @RequestParam("username")  String username,
                      @RequestParam("password")  String password,
                      @RequestParam("next") String next,
                      @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                      HttpServletResponse response){
        try{
            Map<String,String> map=userService.register(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                //如果点击了记住我，就保持ticket5天
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                //如果登录前要跳转的页面不为空，则跳转到原目的页面
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }
                //登陆成功，跳转到首页
                return "redirect:/";
            }else{
                //出错了，将错误信息返回给前端
                model.addAttribute("msg",map.get("msg"));
                //用户重新登录
                return "login";
            }
        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String regloginPage(Model model, @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(path = {"/login/"},method = {RequestMethod.POST,RequestMethod.GET})
    public String login(Model model,
                        @RequestParam("username")  String username,
                        @RequestParam("password")  String password,
                        @RequestParam(value="next", required = false) String next,
                        @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){
        try{
            Map<String,String> map=userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                //如果点击了记住我，就保持ticket5天
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                //如果登录前要跳转的页面不为空，则跳转到原目的页面
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }
                //登陆成功，跳转到首页
                return "redirect:/";
            }else{
                //出错了，将错误信息返回给前端
                model.addAttribute("msg",map.get("msg"));
                //用户重新登录
                return "login";
            }
        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return "login";
        }
    }
    //退出
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        //使得ticket失效
        userService.logout(ticket);
        //返回首页
        return "redirect:/";
    }
}
