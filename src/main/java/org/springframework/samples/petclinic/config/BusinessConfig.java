package org.springframework.samples.petclinic.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(DatasourceConfig.class)
@ComponentScan("org.springframework.samples.petclinic.service")
@PropertySource("classpath:spring/data-access.properties")
@EnableTransactionManagement
@Configuration
public class BusinessConfig {

    @Bean
    public static PropertyResourceConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        //pspc.setLocalOverride(true);
        return pspc;
    }

    @Profile({"jpa","spring-data-jpa"})
    @Configuration
    public static class JpaAndSpringDataJpaConfig{
        @Autowired
        private DataSource dataSource;
        @Autowired
        private HibernateJpaVendorAdapter hibernateJpaVendorAdapter;
        /**
         * JPA EntityManagerFactory
         * @param dataSource
         * @param hibernateJpaVendorAdapter
         * @return
         */
        @Bean
        public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() {
            LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
            /*  the 'database' parameter refers to the database dialect being used.
                By default, Hibernate will use a 'HSQL' dialect because 'jpa.database' has been set to 'HSQL'
                inside file spring/data-access.properties */
            factoryBean.setPersistenceUnitName("petclinic");
            factoryBean.setPackagesToScan("org.springframework.samples.petclinic");
            return factoryBean;
        }

        @Bean
        public HibernateJpaVendorAdapter hibernateJpaVendorAdapter(
                @Value("${jpa.database}") Database database,
                @Value("${jpa.showSql}") boolean showSql) {
            HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
            adapter.setDatabase(database);
            adapter.setShowSql(showSql);
            return adapter;
        }

        @Bean
        public JpaTransactionManager transactionManager() {
            JpaTransactionManager transactionManager = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean().getObject());
            return transactionManager;
        }

        /**
         *  Post-processor to perform exception translation on @Repository classes
         *  (from native exceptions such as JPA PersistenceExceptions to Spring's DataAccessException hierarchy).
         *
         * @return
         */
        @Bean
        public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
            return new PersistenceExceptionTranslationPostProcessor();
        }
    }
    @Profile("jdbc")
    @ComponentScan("org.springframework.samples.petclinic.repository.jdbc")
    @Configuration
    public static class JdbcConfig{

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate =
                    new NamedParameterJdbcTemplate(dataSource);
            return namedParameterJdbcTemplate;
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            return jdbcTemplate;
        }

        /**
         * Transaction manager for a single JDBC DataSource (alternative to JTA)
         * @param dataSource
         * @return
         */
        @Bean
        public DataSourceTransactionManager transactionManager(DataSource dataSource) {
            DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
            transactionManager.setDataSource(dataSource);
            return transactionManager;
        }
    }
    /**
     * Loads JPA beans
     * Will automatically be transactional due to @Transactional.
     * EntityManager will be auto-injected due to @PersistenceContext.
     * PersistenceExceptions will be auto-translated due to @Repository.
     * @author skplanet
     */
    @Profile("jpa")
    @ComponentScan("org.springframework.samples.petclinic.repository.jpa")
    @Configuration
    public static class JpaConfig {}

    @Profile("spring-data-jpa")
    @EnableJpaRepositories(
            basePackages = "org.springframework.samples.petclinic.repository.springdatajpa",
            transactionManagerRef="transactionManager",
            entityManagerFactoryRef="localContainerEntityManagerFactoryBean"
    )
    @Configuration
    public static class SpringDataJpaConfig {}
}
