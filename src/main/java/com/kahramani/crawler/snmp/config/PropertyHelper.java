package com.kahramani.crawler.snmp.config;

import com.kahramani.crawler.snmp.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by kahramani on 11/22/2016.
 */
@Configuration
@PropertySource(value = {"classpath:snmp.properties"})
public class PropertyHelper {

    private static final Logger logger = LoggerFactory.getLogger(PropertyHelper.class);

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
        return parseNumber(this.getString(key), Integer.class, defaultValue);
    }

    /**
     * to get long value and to handle exceptions
     * @param key property key whose value wanted to get
     * @param defaultValue default value to set if in case that property can not be found
     * @return a long which is the property value of the given key
     */
    public long getLong(String key, long defaultValue) {
        return parseNumber(this.getString(key), Long.class, defaultValue);
    }

    /**
     * to get double value and to handle exceptions
     * @param key property key whose value wanted to get
     * @param defaultValue default value to set if in case that property can not be found
     * @return a double which is the property value of the given key
     */
    public double getDouble(String key, double defaultValue) {
        return parseNumber(this.getString(key), Double.class, defaultValue);
    }

    /**
     * to get float value and to handle exceptions
     * @param key property key whose value wanted to get
     * @param defaultValue default value to set if in case that property can not be found
     * @return a float which is the property value of the given key
     */
    public float getFloat(String key, float defaultValue) {
        return parseNumber(this.getString(key), Float.class, defaultValue);
    }

    /**
     * to read sql query from file
     * @param filePathKey property file key which points the path of the file whose content wanted to read
     * @param readLineByLine read file line by line or char by char
     * @param encoding encoding
     * @return a StringBuilder which is the query
     */
    public StringBuilder getSqlQueryFromFile(String filePathKey, boolean readLineByLine, String encoding) {
        StringBuilder sb = null;
        String filePath = this.getString(filePathKey);
        try {
            sb = FileUtils.readFileContent(filePath, readLineByLine, null, encoding);
        } catch (IOException e) {
            logger.error("Failed to get sql query from file " + filePath, e);
        }

        return sb;
    }

    /**
     * to parse number from property
     * @param value property value
     * @param targetClass target class
     * @param defaultValue default value if cannot be parsed
     * @param <T> any child class of Number
     * @return a T type which is the parsed number from the given value
     */
    private <T extends Number> T parseNumber(String value, Class targetClass, T defaultValue) {
        try {
            return (T) NumberUtils.parseNumber(value, targetClass);
        } catch (Exception e) {
            logger.error("Failed to parse value of " + value + " to class of " + targetClass.toString(), e);
            return defaultValue;
        }
    }
}
