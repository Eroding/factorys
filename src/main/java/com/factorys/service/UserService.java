package com.factorys.service;

import com.factorys.error.BusinessException;
import com.factorys.service.model.UserModel;

public interface UserService {

    UserModel  getUserById(Integer id);

    void  register(UserModel userModel) throws BusinessException;


    /*
    telphone:用户注册的手机号码
    password：用户加密后的密码

     */
    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;
}
