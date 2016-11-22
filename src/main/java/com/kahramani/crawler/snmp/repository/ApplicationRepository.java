package com.kahramani.crawler.snmp.repository;

import com.kahramani.crawler.snmp.models.OltOntData;
import com.kahramani.crawler.snmp.models.SwitchPortData;
import com.kahramani.crawler.snmp.utils.DateUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
@Repository
public class ApplicationRepository {

    private JdbcTemplate jdbcTemplate;

    /**
     * to insert switch port data list to application db via batching
     * @param sqlQuery template sql query wanted to run
     * @param portDataList list to insert
     * @return an int array with length of inserted count
     */
    public int[] insertSwitchPortDataList(String sqlQuery, final List<SwitchPortData> portDataList) {
        return jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                int columnIndex = 1;
                SwitchPortData portData = portDataList.get(i);
                preparedStatement.setString(columnIndex++, portData.getSw().getHostName());
                preparedStatement.setString(columnIndex++, portData.getSw().getIpAddress());
                preparedStatement.setString(columnIndex++, portData.getSw().getDeviceModel().getVendor());
                preparedStatement.setString(columnIndex++, portData.getVlan());
                preparedStatement.setString(columnIndex++, portData.getPort());
                preparedStatement.setString(columnIndex++, portData.getMacAddress());
                preparedStatement.setLong(columnIndex++, DateUtils.getDateTimeAsLong());
            }

            @Override
            public int getBatchSize() {
                return portDataList.size(); // to call setValues method "size()" times
            }
        });
    }

    /**
     * to insert olt ont data list to application db via batching
     * @param sqlQuery template sql query wanted to run
     * @param ontDataList list to insert
     * @return an int array with length of inserted count
     */
    public int[] insertOltOntDataList(String sqlQuery, final List<OltOntData> ontDataList) {
        return jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                int columnIndex = 1;
                OltOntData ontData = ontDataList.get(i);
                preparedStatement.setString(columnIndex++, ontData.getOlt().getHostName());
                preparedStatement.setString(columnIndex++, ontData.getOlt().getIpAddress());
                preparedStatement.setString(columnIndex++, ontData.getOlt().getDeviceModel().getVendor());
                preparedStatement.setBoolean(columnIndex++, ontData.getOlt().isReachable());
                preparedStatement.setString(columnIndex++, ontData.getSlot());
                preparedStatement.setString(columnIndex++, ontData.getPort());
                preparedStatement.setString(columnIndex++, ontData.getOntNo());
                preparedStatement.setString(columnIndex++, ontData.getSerialNumber());
                preparedStatement.setLong(columnIndex++, DateUtils.getDateTimeAsLong());
            }

            @Override
            public int getBatchSize() {
                return ontDataList.size(); // to call setValues method "size()" times
            }
        });
    }
}
