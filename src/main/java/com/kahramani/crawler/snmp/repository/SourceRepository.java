package com.kahramani.crawler.snmp.repository;

import com.kahramani.crawler.snmp.models.NetworkElement;
import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
public interface SourceRepository {
    <T extends NetworkElement> List<T> getList(String sqlQuery);
}
