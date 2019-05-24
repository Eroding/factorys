package com.factorys.service.impl;

import com.factorys.dao.PromoDoMapper;
import com.factorys.dataobject.PromoDo;
import com.factorys.service.PromoService;
import com.factorys.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoServiceImpl implements PromoService {


    @Autowired
    private PromoDoMapper promoDoMapper;






    @Override
    public PromoModel getPromoByItemId(Integer itemId) {

       //获取对应商品的秒杀活动信息
       PromoDo promoDo= promoDoMapper.selectByItemId(itemId);


//数据库查找出来的是...Do，需要转化为Model。最后转化为vo传回前端
PromoModel promoModel =convertFromDataObject(promoDo);

        if(promoModel == null){
            return  null;
        }
//判断当前时间是否秒杀活动即将开始或正在进行

DateTime now = new DateTime();
if(promoModel.getStartDate().isAfterNow()){
    promoModel.setStatus(1);
}else if(promoModel.getStartDate().isBeforeNow()){
    promoModel.setStatus(3);
}else {
    promoModel.setStatus(2);
}



   return promoModel;
    }


    private PromoModel convertFromDataObject(PromoDo promoDo){

        if(promoDo ==null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDo,promoModel);

promoModel.setPromoItemPrice(new BigDecimal(promoDo.getPromoItemPrice()));
promoModel.setStartDate(new DateTime(promoDo.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDo.getEndDate()));
        return promoModel;
    }
}
