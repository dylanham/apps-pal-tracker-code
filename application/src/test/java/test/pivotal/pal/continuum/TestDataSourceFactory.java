package test.pivotal.pal.continuum;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;


public class TestDataSourceFactory {

    public static DataSource create() {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/continuum_test?useSSL=false&useTimezone=true&serverTimezone=UTC&useLegacyDatetimeCode=false");
        dataSource.setUser("continuum");

        return dataSource;
    }
}
