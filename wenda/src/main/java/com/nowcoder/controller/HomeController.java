package com.nowcoder.controller;

import com.nowcoder.model.Question;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seu on 2017/5/26.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(IndexContoller.class);
    //通过注解导入WendaService对象
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;

    @RequestMapping(path = {"/user/{userId}"},method = {RequestMethod.POST,RequestMethod.GET})
    public String userIndex(Model model, @PathVariable("userId") int userId){

        List<ViewObject> vos=getQuestions(userId,0,10);

        model.addAttribute("vos",vos);
        return "index";
    }

    @RequestMapping(path = {"/","/index"},method = {RequestMethod.POST,RequestMethod.GET})
    public String index(Model model){

        List<ViewObject> vos=getQuestions(0,0,10);

        model.addAttribute("vos",vos);
        return "index";
    }

    public List<ViewObject> getQuestions(int userId, int offset,int limit){
        List<Question> questionList=questionService.getLatestQuestions(userId,offset,limit);
        List<ViewObject> vos=new ArrayList<>();
        for(Question q:questionList){
            ViewObject viewObject=new ViewObject();
            viewObject.set("question",q);
            viewObject.set("user",userService.getUser(q.getUserId()));
            vos.add(viewObject);
        }
        return vos;
    }
}
