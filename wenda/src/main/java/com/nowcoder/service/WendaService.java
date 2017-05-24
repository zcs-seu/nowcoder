package com.nowcoder.service;

import org.springframework.stereotype.Service;

/**
 * Created by seu on 2017/5/24.
 */
//定义了一个简单的服务并通过@Service注解的形式注入了整个项目之中
@Service
public class WendaService {
    public String getMessage(int userId){
        return "Hello Message:"+String.valueOf(userId);
    }
}
