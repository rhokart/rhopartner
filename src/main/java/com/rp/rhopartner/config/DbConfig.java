package com.rp.rhopartner.config;

import com.rp.rhopartner.util.DbMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@MapperScan(value = "com.rp.rhopartner.repository",
        annotationClass = DbMapper.class,
        sqlSessionFactoryRef = "DBSessionFactory")

@Configuration
public class DbConfig {

    @Value("${spring.datasource.profilemgmt.driverClassName}")
    private String driver;

    @Value("${spring.datasource.profilemgmt.url}")
    private String url;

    @Value("${spring.datasource.profilemgmt.username}")
    private String username;

    @Value("${spring.datasource.profilemgmt.password}")
    private String password;

    @Bean(name = "DBSource")
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driver);
        return dataSource;
    }

    @Bean(name = "DBSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("DBSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        return factoryBean.getObject();
    }

    @Bean(name = "DBSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("DBSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}