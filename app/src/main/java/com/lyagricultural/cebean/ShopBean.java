package com.lyagricultural.cebean;


/**
 * 作者Administrator on 2018/6/29 0029 10:45
 */
public class ShopBean {

    //    @Entity：告诉GreenDao该对象为实体，只有被@Entity注释的Bean类才能被dao类操作
//
//    @Id：对象的Id，使用Long类型作为EntityId，否则会报错。(autoincrement = true)表示主键会自增，如果false就会使用旧值
//
//    @Property：可以自定义字段名，注意外键不能使用该属性
//
//    @NotNull：属性不能为空
//
//    @Transient：使用该注释的属性不会被存入数据库的字段中
//
//    @Unique：该属性值必须在数据库中是唯一值
//
//    @Generated：编译后自动生成的构造函数、方法等的注释，提示构造函数、方法等不能被修改
//  商品id 不能用int

    private Long id;

    //  商品名称
    private String name;

    //  商品面积
    private String area;

    //  商品单价
    private String unit_price;

    //  商品购买数量
    private String num;

    //  商品价格
    private String money;

}
