package com.factorys.controller;


import com.factorys.controller.viewobject.UserVo;
import com.factorys.error.BusinessException;
import com.factorys.error.EmBusinessError;
import com.factorys.response.CommonReturnType;
import com.factorys.service.UserService;
import com.factorys.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin
public class UserController extends  BaseController{

    @Autowired
    private UserService userService;


    @Autowired
    private  HttpServletRequest httpServletRequest;

    //测试html页面是否可用
    @RequestMapping("/test")
    public String test(){
        return "../firstdemo";
    }





//用户注册接口
@RequestMapping(value = "regiser",method = {RequestMethod.POST},consumes ={CONTENT_TYPE_FORMED} )
@ResponseBody
    public CommonReturnType regiser(@RequestParam(name = "telphone")String telphone,
                                    @RequestParam(name = "otpCode")String otpCode,
                                    @RequestParam(name = "name")String name,
                                    @RequestParam(name = "gender")Integer gender,
                                    @RequestParam(name = "age")Integer age,
                                    @RequestParam(name = "password")String passeord
                                    ) throws BusinessException {
   //验证手机号和otpCode相符合，
       String insessionotpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);

//alibaba 已经在方法里做好了判空处理
       if(!com.alibaba.druid.util.StringUtils.equals(otpCode,insessionotpCode)){
    throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
       }
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
userModel.setEncrptPassword(MD5Encoder.encode(passeord.getBytes()));
userService.register(userModel);
        return CommonReturnType.creat(null);
    }




    @RequestMapping(value = "getotp",method = {RequestMethod.POST},consumes ={CONTENT_TYPE_FORMED} )
    @ResponseBody
    public  CommonReturnType getOtp(@RequestParam(name="telphone")String telphone){

        //按照一定的规则生成otp验证码

        Random random = new Random();
        int randomint  = random.nextInt(99999);
        randomint += 10000;
        String otpCode = String.valueOf(randomint);


        //将otp验证码同对应的手机号关联，使用httpsession的方式绑定他的手机号和optcode

        httpServletRequest.getSession().setAttribute(telphone,otpCode);

//还没有做完



//将otp验证码通过短信方式发送给用户
System.out.println("telphone="+telphone+"otp="+otpCode);
return   CommonReturnType.creat(null);

    }


@RequestMapping("/get")
@ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id")Integer id) throws BusinessException {
        //调用service服务获取某id的返回值

   UserModel userModel =  userService.getUserById(id);
   //将核心领域定义的usermodel的值拷贝给uservo，将vo传给前端，


    if(userModel ==null){
      throw new BusinessException(EmBusinessError.USER_NOT_EXIST);

    }
UserVo userVo = convertFromModel(userModel);


//返回通用对象
return  CommonReturnType.creat(userVo);
    }

private  UserVo convertFromModel(UserModel userModel){

    if (userModel == null){
        return  null;
    }
    UserVo userVo = new UserVo();
    BeanUtils.copyProperties(userModel,userVo);


return  userVo;


}




}
