package com.example.thesis.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.example.thesis.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import net.bull.javamelody.JdbcWrapper;

@Configuration
@EnableTransactionManagement
public class JpaConfig extends BaseJpaConfig
{
    private static Logger log = LoggerFactory.getLogger(JpaConfig.class);

    @Autowired
    Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaAdapter());
        entityManagerFactoryBean.setPersistenceUnitName("persistenceUnit");

        Map<String, String> propertyMap = this.hibernateJpaPropertyMap();

        entityManagerFactoryBean.setJpaPropertyMap(propertyMap);

        // find and register all @Entity classes within
        entityManagerFactoryBean.setPackagesToScan("com.example.thesis.entity");

        return entityManagerFactoryBean;
    }

    @Qualifier
    @Bean
    public DataSource dataSource()
    {
        String activeProfile = getActiveProfie();

        log.error("Datasource 'jdbc/SDEDB_" + activeProfile + "' not loaded from Server, now load from JDBC ...");

        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();

        String ip = System.getProperty("db.ip");
        String port = System.getProperty("db.port");
        String name = System.getProperty("db.name");
        String schema = System.getProperty("db.schema");
        String username = System.getProperty("db.username");
        String password = System.getProperty("db.password");

        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        if (StringUtil.hasValue(ip) && StringUtil.hasValue(port))
        {
            StringBuilder url = new StringBuilder("jdbc:mysql://" + ip + ":" + port + "/");
            ds.setUrl(setDataSourceURL(name, schema, url));
        }
        else
        {
            log.warn("Fallback to local pooled datasource");
            ds.setUrl("jdbc:mysql://localhost:5432/");
        }

        log.error("JDBC URL = '" + ds.getUrl() + "'");

        if (StringUtil.hasValue(username) && StringUtil.hasValue(password))
        {
            ds.setUsername(username);
            ds.setPassword(password);
        }
        else
        {
            ds.setUsername("root");
        }

        // extra tomcat jdbc properties
        ds.setInitialSize(1);
        ds.setMinIdle(1);
        ds.setMaxIdle(2);
        ds.setRemoveAbandonedTimeout(600000);
        ds.setRemoveAbandoned(true);
        ds.setDefaultTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
        ds.setValidationQuery("SELECT 1");
        ds.setTestOnBorrow(true); // To check connection using validation
        // query when getting connection from pool

        return JdbcWrapper.SINGLETON.createDataSourceProxy(ds);
    }

    @Bean
    public PlatformTransactionManager transactionManager()
    {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(this.entityManagerFactory().getObject());
        HashMap<String, String> jpaProperties = new HashMap<String, String>();
        jpaProperties.put("transactionTimeout", "43200");
        transactionManager.setJpaPropertyMap(jpaProperties);

        return transactionManager;
    }

    @Override
    public JpaVendorAdapter jpaAdapter()
    {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
        return adapter;
    }

    private String setDataSourceURL(String name, String schema, StringBuilder url)
    {
        if (StringUtil.hasValue(name))
        {
            url.append(name);
        }
        if (StringUtil.hasValue(name) && StringUtil.hasValue(schema))
        {
            url.append("?currentSchema=" + schema);
        }
        return url.toString();
    }

    private String getActiveProfie()
    {
        String[] activeProfiles = env.getActiveProfiles();

        for (String profile : activeProfiles)
        {
            if ("DEV".equals(profile) || "SIT".equals(profile) || "UAT".equals(profile) || "DR".equals(profile) || "PROD".equals(profile))
            {
                return profile;
            }
        }
        return null;
    }
}
