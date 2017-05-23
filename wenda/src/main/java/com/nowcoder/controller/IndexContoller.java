package com.nowcoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seu on 2017/5/23.
 */
//利用注解指定这里是一个Controller
@Controller
public class IndexContoller {
    @RequestMapping("/")
    @ResponseBody
    public String index(){
        return "Hello ,World!";
    }
}
