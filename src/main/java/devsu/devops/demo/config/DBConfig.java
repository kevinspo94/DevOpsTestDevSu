package devsu.devops.demo.config;

import jakarta.annotation.PostConstruct;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class DBConfig {

    @Value("${app.db.validation}")
    boolean validate;

    private final JdbcTemplate jdbcTemplate;

    public DBConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void initializeDB() throws Exception{
        if (validate){
            String sqlCreteValidatioTable = "CREATE TABLE IF NOT EXISTS validation(val VARCHAR(55))";
            jdbcTemplate.execute(sqlCreteValidatioTable);
            List result = jdbcTemplate.query("SELECT * FROM validation", new SingleColumnRowMapper<>());
            if (CollectionUtils.isEmpty(result)){
                InputStream in = this.getClass().getClassLoader().getResourceAsStream("data.sql");
                String sql = IOUtils.toString(in, StandardCharsets.UTF_8);
                jdbcTemplate.execute(sql);
                jdbcTemplate.execute("INSERT INTO validation (val) VALUES ('INITIATED');");
            }
        }
    }
}
