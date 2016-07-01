package com.martin.daoexamplegenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by martin on 16/6/7.
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.martin.district.db");

        addDistrict(schema);
        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    private static void addDistrict(Schema schema) {
        Entity entity = schema.addEntity("DistrictInfo");
        entity.setTableName("district");
        entity.addLongProperty("adCode").notNull().primaryKey();
        entity.addStringProperty("name").notNull();
        entity.addIntProperty("cityCode").notNull();
    }

}
