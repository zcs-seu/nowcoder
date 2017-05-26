package com.nowcoder.controller;

import com.nowcoder.model.User;

import com.nowcoder.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by seu on 2017/5/23.
 */
//利用注解指定这里是一个Controller
@Controller
public class IndexContoller {

    //通过注解导入WendaService对象
    @Autowired
    WendaService wendaService;

    @RequestMapping(path = {"/","/index"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession session){
        return wendaService.getMessage(1)+"Hello ,World!"+session.getAttribute("msg");
    }

    //使用模板来显示页面,Spring velocity模板框架默认采用".vm"作为后缀，因此无需指明"home.html"
    //默认后缀可以在application.properties通过设置spring.velocity.suffix进行配置
    @RequestMapping(path = {"/vm"},method = {RequestMethod.POST,RequestMethod.GET})
    public String template(Model model){
        model.addAttribute("key","value");
        String[] colorsArr=new String[]{
                                    "RED","GREEN","YELLOW"
                                    };
        List<String> colors= Arrays.asList(colorsArr);
        model.addAttribute("colors",colors);

        Map<String,String> map=new HashMap<>();
        for(int i=0;i<4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("map",map);

        model.addAttribute("user",new User("Mike"));
        return "home";
    }

    @RequestMapping("/profile/{groupID}/{userID}")
    @ResponseBody
    public String profile(
            //设置路径参数
            @PathVariable("groupID") String groupID,
            @PathVariable("userID") int userID,
            //设置请求参数，并预置默认参数，不仅支持Get也支持Post请求
            @RequestParam(value = "type",defaultValue = "1") int type,
            @RequestParam(value = "key",defaultValue = "nowcoder") String key){
        return String.format("{%d},{%s},{%d},{%s}",userID,groupID,type,key);
    }

    //测试请求相关代码
    @RequestMapping(path = {"/request"},method = {RequestMethod.GET})
    @ResponseBody//表示返回的是文本而非页面模板
    public String request(Model model, HttpServletRequest request,
                          HttpServletResponse response, HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionID){
        StringBuilder sb = new StringBuilder();
        //（1）查看request封装的数据部分
        //获得请求的方法：Get、Post等
        sb.append(request.getMethod()+"<br>");
        //获得请求字符串，Get请求时为？后面跟的内容
        sb.append(request.getQueryString()+"<br>");
        sb.append(request.getPathInfo()+"<br>");
        //获得请求中与本响应函数映射模式相匹配的URL部分
        sb.append(request.getRequestURI()+"<br>");
        //获得请求中的URL部分
        sb.append(request.getRequestURL()+"<br>");
        //获得所有的请求头中的key-value结构
        Enumeration<String> headerNames=request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name=headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }
        //获得请求中的所有Cookie
        if(request.getCookies()!=null){
            Cookie[] cookies = request.getCookies();
            for(Cookie cookie:cookies){
                sb.append("Cookie:"+cookie.getName()+"-"+cookie.getValue()+"<br>");
            }

        }
        //利用输入参数注解的形式获取某cookie
        sb.append("CookieValue:"+sessionID);

        //设置response
        response.addHeader("nowcoder","hello");
        response.addCookie(new Cookie("username","cookie"));

        return sb.toString();
    }

    //测试重定向相关
    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirectView(@PathVariable("code") int code,
                           HttpSession session){
        session.setAttribute("msg","jump from redirect");
        RedirectView red = new RedirectView("/");//指定跳转的页面地址
        if(code==301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);//设置永久跳转301
        }
        return red;
        //return "redirect:/";//默认302临时跳转
    }

    @RequestMapping(path = {"/admin"})
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if("admin".equals(key)){
            return "Hello admin!";
        }
        throw new IllegalArgumentException("输入参数不对");

    }

    //设置页面异常处理机制
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }
}
