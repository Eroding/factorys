package com.factorys.service;

import com.factorys.service.model.PromoModel;

public interface PromoService {

//取得当前的商品id获取即将进行的或者正在进行的秒杀活动
PromoModel getPromoByItemId(Integer itemId);


}
