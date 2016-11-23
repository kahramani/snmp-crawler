package com.kahramani.crawler.snmp.utils;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
public class ListUtils {

    /**
     * to split a list into partitionCount number of lists
     * @param list wanted to split
     * @param partitionCount how many partition created at the end
     * @return aList which holds number of the given partitionCount of ArrayList
     */
    public static List<List<?>> splitListByPartitionCount(List<?> list, int partitionCount) {
        Assert.notEmpty(list, "'list' cannot be null or empty");
        if(partitionCount <= 0)
            throw new IllegalArgumentException("'partitionCount' cannot be 0 or lower");

        int listSize = list.size();
        int partitionSize = (int) Math.ceil((double) listSize / partitionCount);

        return splitListByPartitionSize(list, partitionSize);
    }

    /**
     * to split a list into lists with equally sized
     * @param list wanted to split
     * @param partitionSize size of every partition created from the list
     * @return a List which holds ArrayLists with size of the given partitionSize
     */
    public static List<List<?>> splitListByPartitionSize(List<?> list, int partitionSize) {
        List<List<?>> partitionList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += partitionSize) {
            List subList = new ArrayList(list.subList(i, Math.min(i + partitionSize, list.size())));
            partitionList.add(subList);
        }

        return partitionList;
    }
}
