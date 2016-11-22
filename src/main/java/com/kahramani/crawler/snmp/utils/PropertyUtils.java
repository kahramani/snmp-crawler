package com.kahramani.crawler.snmp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by kahramani on 11/22/2016.
 */
@Configuration
@PropertySource(value = {"classpath:snmp.properties"})
public class PropertyUtils {

    private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

    @Resource
    public Environment environment;

    /**
     * to get property value with key
     * @param key property key whose value wanted to get
     * @return a String which is the value of the given key
     */
    public String getString(String key) {
        Assert.hasText(key, "'key' cannot be null or empty to get property value");
        return this.environment.getProperty(key);
    }

    /**
     * to get boolean value and to handle exceptions
     * @param key property key whose value wanted to get
     * @param defaultValue default value to set if in case that property can not be found
     * @return a boolean which is the property value of the given key
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String s = this.getString(key);
        if(!StringUtils.hasText(s)) {
            return defaultValue;
        }

        return Boolean.parseBoolean(s.trim());
    }

    /**
     * to get int value and to handle exceptions
     * @param key property key whose value wanted to get
     * @param defaultValue default value to set if in case that property can not be found
     * @return an int which is the property value of the given key
     */
    public int getInt(String key, int defaultValue) {
        Number n = null;
        String prop = this.getString(key);
        try {
            n = this.parseNumber(prop);
        } catch (ParseException e) {
            logger.error("Failed to parse int value of " + prop, e);
        }
        if(n == null)
            return defaultValue;

        return n.intValue();
    }

    /**
     * to get long value and to handle exceptions
     * @param key property key whose value wanted to get
     * @param defaultValue default value to set if in case that property can not be found
     * @return a long which is the property value of the given key
     */
    public long getLong(String key, long defaultValue) {
        Number n = null;
        String prop = this.getString(key);
        try {
            n = this.parseNumber(prop);
        } catch (ParseException e) {
            logger.error("Failed to parse long value of " + prop, e);
        }
        if(n == null)
            return defaultValue;

        return n.longValue();
    }

    /**
     * to get double value and to handle exceptions
     * @param key property key whose value wanted to get
     * @param defaultValue default value to set if in case that property can not be found
     * @return a double which is the property value of the given key
     */
    public double getDouble(String key, double defaultValue) {
        Number n = null;
        String prop = this.getString(key);
        try {
            n = this.parseNumber(prop);
        } catch (ParseException e) {
            logger.error("Failed to parse double value of " + prop, e);
        }
        if(n == null)
            return defaultValue;

        return n.doubleValue();
    }

    /**
     * to get float value and to handle exceptions
     * @param key property key whose value wanted to get
     * @param defaultValue default value to set if in case that property can not be found
     * @return a float which is the property value of the given key
     */
    public float getFloat(String key, float defaultValue) {
        Number n = null;
        String prop = this.getString(key);
        try {
            n = this.parseNumber(prop);
        } catch (ParseException e) {
            logger.error("Failed to parse float value of " + prop, e);
        }
        if(n == null)
            return defaultValue;

        return n.floatValue();
    }

    /**
     * to read sql query from file
     * @param filePathKey property file key which points the path of the file whose content wanted to read
     * @param readLineByLine read file line by line or char by char
     * @return a StringBuilder which is the query
     */
    public StringBuilder getSqlQueryFromFile(String filePathKey, boolean readLineByLine) {
        StringBuilder sb = null;
        String filePath = this.getString(filePathKey);
        try {
            sb = FileUtils.readFileContent(filePath, readLineByLine, null);
        } catch (IOException e) {
            logger.error("Failed to get sql query from file " + filePath, e);
        }

        return sb;
    }

    /**
     * to parse number from property
     * @param value property value
     * @param <T> any child class of Number
     * @return a T type which is the parsed number from the given value
     * @throws ParseException if cannot parse the given value
     */
    private <T extends Number> T parseNumber(String value) throws ParseException {
        Assert.hasText(value, "'value' cannot be null or empty to parse as number");
        return (T) NumberFormat.getInstance().parse(value);
    }
}
