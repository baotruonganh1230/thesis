package com.example.thesis.config;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public abstract class BaseJpaConfig {
    private static Logger A = LoggerFactory.getLogger(BaseJpaConfig.class);

    public BaseJpaConfig() {
    }

    public abstract LocalContainerEntityManagerFactoryBean entityManagerFactory();

    public abstract JpaVendorAdapter jpaAdapter();

    public Map<String, String> hibernateJpaPropertyMap() {
        HashMap var1 = new HashMap();
        var1.put("hibernate.format_sql", "true");
        var1.put("hibernate.use_sql_comments", "true");
        var1.put("hibernate.generate_statistics", "true");
        return var1;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager var1 = new JpaTransactionManager();
        var1.setEntityManagerFactory(this.entityManagerFactory().getObject());
        HashMap var2 = new HashMap();
        var2.put("transactionTimeout", "43200");
        var1.setJpaPropertyMap(var2);
        return var1;
    }
}
