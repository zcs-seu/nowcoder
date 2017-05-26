package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seu on 2017/5/26.
 */
public class ViewObject {
    private Map<String ,Object> objs=new HashMap<>();

    public void set(String key,Object value){
        objs.put(key,value);
    }

    public Object get(String key){
        return objs.get(key);
    }
}
