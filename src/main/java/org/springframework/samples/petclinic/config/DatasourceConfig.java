package org.springframework.samples.petclinic.config;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class DatasourceConfig {

    /**
     * DataSource configuration for the tomcat jdbc connection pool
     * See here for more details on commons-dbcp versus tomcat-jdbc:
     * http://blog.ippon.fr/2013/03/13/improving-the-performance-of-the-spring-petclinic-sample-application-part-3-of-5
     *
     * @param driverClassName
     * @param url
     * @param username
     * @param password
     * @return
     */
    @Bean
    public DataSource dataSource(
            @Value("${jdbc.driverClassName}") String driverClassName,
            @Value("${jdbc.url}") String url,
            @Value("${jdbc.username}") String username,
            @Value("${jdbc.password}") String password) {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * Database initializer. If any of the script fails, the initialization stops.
     * As an alternative, for embedded databases see <jdbc:embedded-database/>.
     * @param dataSource
     * @param initLocation
     * @param dataLocation
     * @return
     */
    @Bean
    public DatabasePopulator databasePopulator(DataSource dataSource,
            @Value("${jdbc.initLocation}") String initLocation,
            @Value("${jdbc.dataLocation}") String dataLocation,
            ApplicationContext ac) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setContinueOnError(true);
        populator.setIgnoreFailedDrops(true);
        populator.addScript(ac.getResource(initLocation));
        populator.addScript(ac.getResource(dataLocation));
        try {
            populator.populate(dataSource.getConnection());
        } catch (SQLException ignored) {
        }
        return populator;
    }

    @Profile("javaee")
    @Configuration
    public static class JavaeeConfig {

        /**
         * JNDI DataSource for JEE environments
         * @return
         * @throws Exception
         */
        @Bean
        public DataSource dataSource() throws Exception {
            Context ctx = new InitialContext();
            return (DataSource) ctx.lookup("java:comp/env/jdbc/petclinic");
        }
    }
}
