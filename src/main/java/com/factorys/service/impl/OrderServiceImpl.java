package com.factorys.service.impl;

import com.factorys.dao.OrderDoMapper;
import com.factorys.dao.SequenceDoMapper;
import com.factorys.dataobject.OrderDo;
import com.factorys.dataobject.SequenceDo;
import com.factorys.error.BusinessException;
import com.factorys.error.EmBusinessError;
import com.factorys.service.ItemService;
import com.factorys.service.OrderService;
import com.factorys.service.UserService;
import com.factorys.service.model.ItemModel;
import com.factorys.service.model.OrderModel;
import com.factorys.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
//同一个事务当中
@Transactional
public class OrderServiceImpl implements OrderService {


    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDoMapper orderDoMapper;

    @Autowired
    private SequenceDoMapper sequenceDoMapper;



    @Override
    public OrderModel creatOrder(Integer userId, Integer itemId,Integer promoId, Integer amount) throws BusinessException {

//校验下单状态，下单的商品是否存在，用户是够合法，购买数量是否正确


        ItemModel itemModel = itemService.getItemById(itemId);
if(itemModel == null){
    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品名不存在");
}

        UserModel userModel = userService.getUserById(userId);
if(userModel == null){
    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户不存在");
}

if(amount <=0||amount>99){
    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"下单数量不能超过99件");
}


        //落单减库存，支付减库存,当用户下单的时候，就要在商品的库存里扣除相应下单的数量
boolean result =itemService.decreaseStock(itemId,amount);
if(!result){
    throw  new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
}
//校验活动信息
        if(promoId!=null){
            //校验对应活动是否存在这个商品
            if(promoId.intValue()!=itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");
            }//校验活动是否正在进行，以免传进来的id还没有在正在进行的活动中，却享受了活动的价格
            else if(itemModel.getPromoModel().getStatus().intValue()!=2){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动还未开始");
            }


        }


        //订单入库
OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel .setAmount(amount);

        orderModel.setPromoId(promoId);

        if(promoId!=null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else {
            orderModel.setItemPrice(itemModel.getPrice());
        }

        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));


        //生成交易流水号
        orderModel.setId(generateOrderNo());
        OrderDo orderDo = convertFromOrderModel(orderModel);
orderDoMapper.insertSelective(orderDo);



//加上商品的销量

        itemService.increaseSales(itemId,amount);
        //返回前端


        return orderModel;
    }


    private OrderDo convertFromOrderModel(OrderModel orderModel){

        if(orderModel ==null){
            return  null;
        }
        OrderDo orderDo = new OrderDo();
        BeanUtils.copyProperties(orderModel,orderDo);
        orderDo.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDo.setOrderPrice(orderModel.getOrderPrice().doubleValue());
  return  orderDo;
    }




    //生成订单流水号方法
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String generateOrderNo(){
//订单号有16位
        StringBuilder stringBuilder = new StringBuilder();

        //前八位年月日
        LocalDateTime now = LocalDateTime.now();

         String nowDate =now.format(DateTimeFormatter.BASIC_ISO_DATE);
         stringBuilder.append(nowDate);



        //中间六位自增序列

        //获取当前sequence
        int sequence = 0;
            SequenceDo sequenceDo= sequenceDoMapper.getSequenceByName("order_info");

              sequence= sequenceDo.getCurrentValue();
              sequenceDo.setCurrentValue(sequenceDo.getCurrentValue()+sequenceDo.getStep());
              sequenceDoMapper.updateByPrimaryKeySelective(sequenceDo);
              String sequenceStr = String.valueOf(sequence);
            //补足六位
        for(int i =0;i<6-sequenceStr.length();i++){
            stringBuilder.append(0);
        }
stringBuilder.append(sequenceStr);



        //最后两位分库分表位,暂时写死
stringBuilder.append("00");

        return stringBuilder.toString();
    }

}
