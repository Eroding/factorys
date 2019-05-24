package com.factorys.service.model;

import org.hibernate.validator.constraints.NotBlank;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemModel {

private Integer id;

//商品名称
    @NotBlank(message = "商品名称不能为空")
private  String title;

//商品价格（假设电商平台不允许有免费的商品）
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0,message = "商品价格必须大于0")
private BigDecimal price;

//商品库存
    @NotNull(message = "库存不能不填")
private  Integer stock;

//商品描述
@NotBlank(message = "商品描述信息不能为空")
private String description;

//商品销量
private  Integer sales;


//商品描述图片的ul
@NotBlank(message = "商品图片信息不能为空")
private  String imgUrl;

//内部类。（当一个类可以被另外一个类完全使用，也就是A类的属性刚好符合B类的属性。把A当做B类的内部类）
//这里所用的目的是当这个promoModel不为空的时候，则表示其还拥有没有结束的秒杀项目（包括未开始和正在进行中）
private PromoModel promoModel;


    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
