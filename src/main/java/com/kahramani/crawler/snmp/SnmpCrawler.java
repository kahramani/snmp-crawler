package com.kahramani.crawler.snmp;

import com.kahramani.crawler.snmp.models.NetworkElement;

import java.util.List;

/**
 * Created by kahramani on 11/24/2016.
 */
public interface SnmpCrawler <T extends NetworkElement> {
    boolean createSession();
    boolean terminateSession();
    List<?> crawlOver(T ne);
    List<?> crawlAllOver(List<T> tList);
    StringBuilder address(T t);
}
