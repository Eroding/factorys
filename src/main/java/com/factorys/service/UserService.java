package com.factorys.service;

import com.factorys.error.BusinessException;
import com.factorys.service.model.UserModel;

public interface UserService {

    UserModel  getUserById(Integer id);

    void  register(UserModel userModel) throws BusinessException;
}
