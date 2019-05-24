package com.factorys.service;

import com.factorys.error.BusinessException;
import com.factorys.service.model.OrderModel;

public interface OrderService {


    //通过前端url传过来的秒杀活动id，然后下单接口内校验对应的id属于是否对应的商品并且活动已经开始

    OrderModel creatOrder(Integer userId,Integer itemId,Integer promoId, Integer amount) throws BusinessException;


}
