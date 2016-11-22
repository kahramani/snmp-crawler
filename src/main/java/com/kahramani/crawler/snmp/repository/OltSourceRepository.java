package com.kahramani.crawler.snmp.repository;

import com.kahramani.crawler.snmp.models.Olt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
@Repository
public class OltSourceRepository implements SourceRepository {

    @Autowired
    @Qualifier("oltSourceJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * to get olt list from the source repository
     * @param sqlQuery sql query wanted to run
     * @return a List of Olt
     */
    @Override
    public List<Olt> getList(String sqlQuery) {
        return jdbcTemplate.query(sqlQuery, new RowMapper<Olt>() {
            @Override
            public Olt mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Olt(resultSet.getString("OLT_IP"), resultSet.getString("OLT_NAME"));
            }
        });
    }
}
