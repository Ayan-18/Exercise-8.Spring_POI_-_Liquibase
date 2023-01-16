package com.example.exerciseeight.config;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.hsqldb.util.DatabaseManagerSwing;

import javax.sql.DataSource;

public class DataSourceDBUnitTest extends DataSourceBasedDBTestCase {


    @Override
    protected DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        dataSource.setUrl("jdbc:hsqldb:mem:java");
        dataSource.setUsername("");
        dataSource.setPassword("");
        dataSource.setSchema("PUBLIC");
        return dataSource;
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(getClass().getClassLoader()
                .getResourceAsStream("templates/actual.xml"));
    }

    @Override
    protected DatabaseOperation getSetUpOperation() {
        return DatabaseOperation.REFRESH;
    }
}
