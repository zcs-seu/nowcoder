package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by seu on 2017/5/31.
 */
@Component
public class HostHolder {
    //专门用于存放取出来的用户，为了同时支持多个线程分别访问采用ThreadLocal变量存储
    //对于ThreadLocal变量而言，每一个线程都有自己的独立的存储空间，但又可以使用相同的接口予以访问
    //ThreadLocal变量底层有一种类似于Map<ThreadID,User>的结构
    private static ThreadLocal<User> users=new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }

}
