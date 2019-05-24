package com.factorys.controller;


import com.factorys.error.BusinessException;
import com.factorys.error.EmBusinessError;
import com.factorys.response.CommonReturnType;
import com.factorys.service.OrderService;
import com.factorys.service.model.OrderModel;
import com.factorys.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
//接受跨域请求中的ajax数据，可以在http通信中的seesion中共享数据
@CrossOrigin(allowCredentials="true",origins = {"*"})
public class OrderController extends  BaseController{


@Autowired
private OrderService orderService;

@Autowired
private HttpServletRequest httpServletRequest;



    //封装下单请求
    @RequestMapping(value = "/creatorder",method = {RequestMethod.POST},consumes ={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType creatOrder(@RequestParam(name="itemId")Integer itemId,
                                       @RequestParam(name="amount")Integer amount,
                                       @RequestParam(name="promoId",required = false)Integer promoId) throws BusinessException {



        //判断用户是否登录
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
if(isLogin ==null || !isLogin.booleanValue()){
    throw new BusinessException(EmBusinessError.USER__NOT_LOGIN,"用户未登录");
}


        //获取用户的登录信息
     UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

          OrderModel orderModel =orderService.creatOrder(userModel.getId(),itemId,promoId,amount);
return  CommonReturnType.creat(null);

    }
}
