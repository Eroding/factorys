package com.factorys.error;



//包装器业务异常类实现
public class BusinessException extends Exception implements CommonError {

//强关联
  private  CommonError commonError;


  //其实上面的CommonError就是EmBusinessError，这个构造方法目的是接收Em.. 的传参用于构造业务异常
  public  BusinessException(CommonError commonError){
      super();
      this.commonError = commonError;

  }

//接收自定义errMsg的方式构造业务异常

public  BusinessException(CommonError commonError,String errMsg){
      super();
      this.commonError = commonError;
      //二次改写errMsg
      this.commonError.setErrMsg(errMsg);

}



    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
       this.commonError.setErrMsg(errMsg);
        return this;
    }
}
