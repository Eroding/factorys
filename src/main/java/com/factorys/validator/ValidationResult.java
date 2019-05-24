package com.factorys.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {

    //校验结果是否有错
    private  boolean hsaErrors =false;

    //存放信息错误的map

    private Map<String,String> errorMsgMap=new HashMap<>();

    public boolean isHsaErrors() {
        return hsaErrors;
    }

    public void setHsaErrors(boolean hsaErrors) {
        this.hsaErrors = hsaErrors;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }
//实现通用的  通过格式化字符串信息获取错误结果的msg方法

    public String getErrMsg(){
    return StringUtils.join(errorMsgMap.values().toArray(),",");
    }

}
