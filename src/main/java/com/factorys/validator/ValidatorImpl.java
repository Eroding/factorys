package com.factorys.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import javax.validation.Validator;
import java.util.Set;

/*

component 属于spring的bean，扫描的时候会扫描到这个bean
 */
@Component
public class ValidatorImpl implements InitializingBean {
    //真正javax定义的接口实现的工具
    private Validator validator;

    //实现校验方法并返回校验结果
    public ValidationResult validate(Object bean) {
        ValidationResult validationResult = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);

        if (constraintViolationSet.size() > 0) {
            //有错误,设置成true
            validationResult.setHsaErrors(true);
            //遍历错误


          /*  constraintViolationSet.forEach(constraintViolation->{
                String eerrMsg = constraintViolation.getMessage();
            });*/
            for (ConstraintViolation<Object> constraintViolation : constraintViolationSet) {
                String errMsg = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();
                validationResult.getErrorMsgMap().put(propertyName, errMsg);
            }

        }
    return  validationResult;
    }


//当spring bean初始化完成之后会回调Vali...中的after...方法
        @Override
        public void afterPropertiesSet() throws Exception{
            //将hibernate  validator通过工厂的初始化方式使其实例化
            this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        }
    }

