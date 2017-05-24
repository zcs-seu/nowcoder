package com.nowcoder.model;

/**
 * Created by seu on 2017/5/24.
 */
public class User {
    private String name;

    public User(){}

    public User(String name){
        this.name=name;
    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getDescription(){
        return "This is "+name;
    }
}
