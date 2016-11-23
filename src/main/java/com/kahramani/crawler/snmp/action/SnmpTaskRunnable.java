package com.kahramani.crawler.snmp.action;

import com.kahramani.crawler.snmp.models.NetworkElement;

import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
public interface SnmpTaskRunnable extends Runnable {
    <T extends NetworkElement> void setList(List<T> neList);
}