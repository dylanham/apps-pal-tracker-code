package integrationtests;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

import static java.lang.String.format;

public class TestDataSourceFactory {

    public static DataSource create() {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setUrl(getEnv("SPRING_DATASOURCE_URL"));
        dataSource.setUser(getEnv("SPRING_DATASOURCE_USERNAME"));

        return dataSource;
    }


    private static String getEnv(String name) {
        String value = System.getenv(name);
        if (value == null) {
            throw new IllegalStateException(format("Missing %s env variable", name));
        }
        return value;
    }
}
