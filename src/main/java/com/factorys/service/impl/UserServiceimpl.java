package com.factorys.service.impl;

import com.factorys.dao.UserDoMapper;
import com.factorys.dao.UserPasswordDoMapper;
import com.factorys.dataobject.UserDo;
import com.factorys.dataobject.UserPasswordDo;
import com.factorys.error.BusinessException;
import com.factorys.error.EmBusinessError;
import com.factorys.service.UserService;
import com.factorys.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceimpl implements UserService {


    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private UserPasswordDoMapper userPasswordDoMapper;


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

       if(StringUtils.isEmpty(userModel.getName())
               ||userModel.getGender()==null
               ||userModel.getAge()==null
               ||StringUtils.isEmpty(userModel.getTelphone())){
           throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);

       }




       //实现model》》dataobject
UserDo userDo = convertfromModel(userModel);
userDoMapper.insertSelective(userDo);


UserPasswordDo userPasswordDo = convertPassWordfromModel(userModel);
userPasswordDoMapper.insertSelective(userPasswordDo);


return;
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
userPasswordDo.getUserId(userModel.getId());

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
