package com.factorys.service.impl;

import com.factorys.dao.UserDoMapper;
import com.factorys.dao.UserPasswordDoMapper;
import com.factorys.dataobject.UserDo;
import com.factorys.dataobject.UserPasswordDo;
import com.factorys.error.BusinessException;
import com.factorys.error.EmBusinessError;
import com.factorys.service.UserService;
import com.factorys.service.model.UserModel;
import com.factorys.validator.ValidationResult;
import com.factorys.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceimpl implements UserService {


    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private UserPasswordDoMapper userPasswordDoMapper;

    @Autowired
private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {

        UserDo userDo = userDoMapper.selectByPrimaryKey(id);

        if (userDo ==null){
            return  null;
        }
/*
这一步的目的通过用户id获取对应用户的加密密码信息
 */
UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDo.getId());


        return convertFormDataObject(userDo,userPasswordDo);

    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {


        if(userModel == null){
            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

      /* if(StringUtils.isEmpty(userModel.getName())
               ||userModel.getGender()==null
               ||userModel.getAge()==null
               ||StringUtils.isEmpty(userModel.getTelphone())){
           throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);

       }*/
          ValidationResult result=   validator.validate(userModel);
          if(result.isHsaErrors()){
              throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
          }



       //实现model》》dataobject
UserDo userDo = convertfromModel(userModel);

//在插入时。mysql已经设置好了唯一索引，所以当输入相同手机号时，可能会报唯一索引的异常。
try{
    userDoMapper.insertSelective(userDo);
}catch (DuplicateKeyException ex){
    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号码已经注册");
}
        userModel.setId(userDo.getId());
System.out.println("runtime="+userModel.getId());
UserPasswordDo userPasswordDo = convertPassWordfromModel(userModel);
userPasswordDoMapper.insertSelective(userPasswordDo);
return;

    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {

        //通过用户的手机获取信息
UserDo userDo =userDoMapper.selectByTelphone(telphone);

if (userDo == null){
    throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
}
UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDo.getId());

UserModel userModel = convertFormDataObject(userDo,userPasswordDo);

        //比对用户信息内加密的密码是否和传输进来的密码相同
if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){

    throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
}
return userModel;
    }


    private  UserDo convertfromModel(UserModel userModel){
  if(userModel==null){
      return  null;
  }
  UserDo userDo = new UserDo();
  BeanUtils.copyProperties(userModel,userDo);

  return  userDo;
    }


    private  UserPasswordDo convertPassWordfromModel(UserModel userModel){
        if(userModel==null){
            return  null;
        }
        UserPasswordDo userPasswordDo = new UserPasswordDo();

        userPasswordDo.setEncrptPassword(userModel.getEncrptPassword());
userPasswordDo.setUserId(userModel.getId());

        return  userPasswordDo;
    }


    private UserModel convertFormDataObject(UserDo userDo, UserPasswordDo userPasseordDo){
     if(userDo == null){
      return  null;

        }

        UserModel userModel = new UserModel();
        /*
        具有很多相同属性的两个类，使用copy
         */
        BeanUtils.copyProperties(userDo,userModel);
        /*
        得到passwordDo的值set到Usermodel
         */

        if(userPasseordDo != null){
            userModel.setEncrptPassword(userPasseordDo.getEncrptPassword());
        }

 return  userModel;
    }

}
