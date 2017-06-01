package dl.chatty.config;

import javax.sql.DataSource;

import org.h2.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatasourceConfig {

    @Bean
    public DataSource dataSource(Environment env) {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(env.getRequiredProperty("db.url"));
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setUsername(env.getRequiredProperty("db.username"));
        dataSource.setPassword(env.getRequiredProperty("db.password"));
        dataSource.setMaximumPoolSize(env.getRequiredProperty("db.maxPoolSize", Integer.class));
        dataSource.setInitializationFailFast(true);

        return dataSource;
    }

}
