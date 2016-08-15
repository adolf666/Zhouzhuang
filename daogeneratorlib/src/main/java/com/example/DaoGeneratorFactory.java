package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DaoGeneratorFactory {
    public static void main(String[] args) throws Exception {

        int version=1;
        String defaultPackage="com.adolf.zhouzhuang";
        //创建模式对象，指定版本号和自动生成的bean对象的包名
        Schema schema=new Schema(version,defaultPackage);
        //指定自动生成的dao对象的包名,不指定则都DAO类生成在"test.greenDAO.bean"包中
//        schema.setDefaultJavaPackageDao("test.greenDAO.dao");

        //添加实体
        addEntity(schema);

        String outDir="C:/Users/adolf/AndroidStudioProjects/Zhouzhuang/app/src/main/java-gen";
        //调用DaoGenerator().generateAll方法自动生成代码到之前创建的java-gen目录下
        new DaoGenerator().generateAll(schema,outDir);

    }

    private static void addEntity(Schema schema) {
        //添加一个实体，则会自动生成实体Entity类
        Entity entity = schema.addEntity("Entity");
        //指定表名，如不指定，表名则为 Entity（即实体类名）
        entity.setTableName("student");
        //给实体类中添加属性（即给test表中添加字段）
        entity.addIdProperty().autoincrement();//添加Id,自增长
        entity.addStringProperty("name").notNull();//添加String类型的name,不能为空
        entity.addIntProperty("age");//添加Int类型的age
        entity.addDoubleProperty("score");//添加Double的score
    }

}
