package com.factorys.controller;


import com.factorys.controller.viewobject.ItemVo;
import com.factorys.error.BusinessException;
import com.factorys.response.CommonReturnType;
import com.factorys.service.ItemService;
import com.factorys.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("item")
@RequestMapping("/item")
//接受跨域请求中的ajax数据，可以在http通信中的seesion中共享数据
@CrossOrigin(allowCredentials="true",allowedHeaders="*")

public class ItemController extends  BaseController {


    @Autowired
    private ItemService itemService;


    //创建商品过程
    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes ={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType creatItem(@RequestParam(name="title")String title,
                                      @RequestParam(name="description")String description,
                                      @RequestParam(name="price") BigDecimal price,
                                      @RequestParam(name="stock")Integer stock,
                                      @RequestParam(name="imgUrl")String imgUrl) throws BusinessException {

//封装service请求来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setStock(stock);
        itemModel.setPrice(price);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModel1forReturn = itemService.creatItem(itemModel);

        ItemVo itemVo = convertVoFromModel(itemModel1forReturn);
        System.out.println("此时的ID="+itemModel.getId());
return   CommonReturnType.creat(itemVo);
    }

    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    @ResponseBody
public CommonReturnType getItem(@RequestParam(name="id")Integer id){
        ItemModel itemModel = itemService.getItemById(id);
        ItemVo itemVo = convertVoFromModel(itemModel);
        System.out.println("aaa");
        return  CommonReturnType.creat(itemVo);


    }



    private ItemVo convertVoFromModel(ItemModel itemModel){
        if(itemModel ==null){
            return null;
        }
        ItemVo itemVo = new ItemVo();
        BeanUtils.copyProperties(itemModel,itemVo);

if(itemModel.getPromoModel()!=null){
    //有正在进行或者即将进行的秒杀项目
    itemVo.setPromostatus(itemModel.getPromoModel().getStatus());
    itemVo.setPromoId(itemModel.getPromoModel().getId());
    itemVo.setStartTime(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
    itemVo.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());


}else {
    itemVo.setPromostatus(0);
}





        return  itemVo;
    }

//商品列表页面浏览
    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem(){
        List<ItemModel> itemModelList = itemService.listItem();
        //使用stream api将list内的itemmodel转化为itemvo
        List<ItemVo> itemVoList = itemModelList.stream().map(itemModel -> {
            ItemVo itemVo = this.convertVoFromModel(itemModel);
            return  itemVo;

        }).collect(Collectors.toList());
        return CommonReturnType.creat(itemVoList);
    }

}
