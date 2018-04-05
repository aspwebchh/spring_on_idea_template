package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import service.UserInfoService;

import java.io.PrintWriter;

@Controller
public class Default {
    @Autowired
    UserInfoService userInfoService;

    @RequestMapping(value = "/welcome")
    public void welcome(PrintWriter pw) {
        pw.write("hello world");
    }


    @RequestMapping(value = "/add_user")
    public void addUser(PrintWriter pw) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("陈宏鸿");
        userInfo.setAddr("杭州");
        userInfo.setAge(30);
        userInfo.setMob("10086");
        userInfoService.add(userInfo);
    }

    @RequestMapping(value = "/update_user")
    public void updateUser(PrintWriter pw) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setName("陈大侠");
        userInfo.setAddr("杭州萧山");
        userInfo.setAge(32);
        userInfo.setMob("300200");
        userInfoService.update(userInfo);
    }


    @RequestMapping(value = "/get_user")
    public void getUser(PrintWriter pw) {
        UserInfo userInfo = userInfoService.get(1);
        String json = JSON.toJSONString(userInfo, SerializerFeature.BrowserCompatible);
        pw.println(json);
    }


    @RequestMapping(value = "/delete_user")
    public void deleteUser(PrintWriter pw) {
        userInfoService.del(2);
    }
}
