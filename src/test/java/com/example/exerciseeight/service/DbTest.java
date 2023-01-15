package com.example.exerciseeight.service;

import com.example.exerciseeight.config.DataSourceDBUnitTest;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.junit.jupiter.api.Test;

import java.sql.Statement;


public class DbTest extends DataSourceDBUnitTest {


    @Test
    public void testGroup() throws Exception {
        IDataSet databaseDataSet = getDataSet();
        ITable expectedTable = databaseDataSet.getTable("METER_GROUP");
        System.out.println("<<<<<<<>______>>>>>>>" + expectedTable.getValue(0, "name"));

        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate("""
                create table if not exists meter_group
                (
                    id   bigint not null ,
                    name varchar(20) not null ,
                    primary key (id)
                );
                create table if not exists meter
                (
                    id             bigint not null ,
                    type           varchar(20) not null ,
                    meter_group_id bigint not null ,
                    primary key (id),
                    foreign key (meter_group_id) references meter_group (id)
                );
                INSERT INTO meter_group values (1,'room1'),(2,'room2'),(3,'room3');
                INSERT INTO meter VALUES (11,'elm11',1),(2,'elm22',1),(3,'elm33',2),(4,'elm44',2),(5,'elm55',3);
                                                  """);


        IDataSet actualDataSet = getConnection().createDataSet();
        ITable actualTable = actualDataSet.getTable("METER_GROUP");
        System.out.println("<<<<<<<<___________>>>>>>>>>>" + actualTable.getValue(0, "name"));

//        assertEquals(expectedTable, actualTable);
    }
}
