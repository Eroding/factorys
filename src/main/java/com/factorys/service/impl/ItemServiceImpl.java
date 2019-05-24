package com.factorys.service.impl;

import com.factorys.dao.ItemDoMapper;
import com.factorys.dao.ItemStockDoMapper;
import com.factorys.dataobject.ItemDo;
import com.factorys.dataobject.ItemStockDo;
import com.factorys.error.BusinessException;
import com.factorys.error.EmBusinessError;
import com.factorys.service.ItemService;
import com.factorys.service.PromoService;
import com.factorys.service.model.ItemModel;
import com.factorys.service.model.PromoModel;
import com.factorys.validator.ValidationResult;
import com.factorys.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

@Autowired
private ValidatorImpl validator;


@Autowired
private ItemDoMapper itemDoMapper;

    @Autowired
    private ItemStockDoMapper itemStockDoMapper;

    @Autowired
    private PromoService promoService;

private  ItemDo converItemDoFromItemModel(ItemModel itemModel){

    if(itemModel ==null){
        return  null;
    }
    ItemDo itemDo = new ItemDo();
    BeanUtils.copyProperties(itemModel,itemDo);
    itemDo.setPrice(itemModel.getPrice().doubleValue());

return  itemDo;
}


private ItemStockDo  convertItemStockDoFromItemModel(ItemModel itemModel){
if(itemModel ==null){
    return  null;
}
ItemStockDo itemStockDo = new ItemStockDo();
itemStockDo.setItemId(itemModel.getId());
itemStockDo.setStock(itemModel.getStock());
return  itemStockDo;

}



    @Override
    @Transactional
    public ItemModel creatItem(ItemModel itemModel) throws BusinessException {
        //校验入参

       ValidationResult validationResult= validator.validate(itemModel);
if(validationResult.isHsaErrors()){
    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
}

        //转化itemmodel ->dataobject
ItemDo itemDo = this.converItemDoFromItemModel(itemModel);
        System.out.println("888"+itemDo.getPrice());

        //写入数据库
itemDoMapper.insertSelective(itemDo);
System.out.println("impl="+itemDo.getId());
itemModel.setId(itemDo.getId());

ItemStockDo itemStockDo = this.convertItemStockDoFromItemModel(itemModel);


itemStockDoMapper.insertSelective(itemStockDo);
        //返回创建完成的对象

        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {

List<ItemDo> itemDoList = itemDoMapper.listItem();
 List<ItemModel> itemModelList = itemDoList.stream().map(itemDo ->{
    ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
    ItemModel itemModel = this.convertModelFromDataObject(itemDo,itemStockDo);
    return  itemModel;
}).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
       ItemDo itemDo = itemDoMapper.selectByPrimaryKey(id);

       if(itemDo == null){
           return null;
       }
//获得库存数量
ItemStockDo itemStockDo =itemStockDoMapper.selectByItemId(itemDo.getId());

//将dataobject->model

ItemModel itemModel = convertModelFromDataObject(itemDo,itemStockDo);


//获取活动商品的信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if(promoModel!=null&&promoModel.getStatus().intValue()!=3){
            itemModel.setPromoModel(promoModel);
        }


        return itemModel;
    }







    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {

    //返回的值为影响的条数
        int affectedRow = itemStockDoMapper.decreaseStock(itemId,amount);
       if(affectedRow> 0){
          //更新库存成功
           return true;
       }else {
           //更新库存失败
           return false;
       }


    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {

 itemDoMapper.increaseSales(itemId,amount);


    }


    private  ItemModel convertModelFromDataObject(ItemDo itemDo,ItemStockDo itemStockDo){
    ItemModel itemModel = new ItemModel();

    BeanUtils.copyProperties(itemDo,itemModel);

    itemModel.setPrice(new BigDecimal(itemDo.getPrice()));

itemModel.setStock(itemStockDo.getStock());

return  itemModel;



    }


}
