package com.kahramani.crawler.snmp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
/**
 * Created by kahramani on 11/22/2016.
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * to read content of a file
     * @param filePath property file key which points the path of the fi
     * @param readLineByLine read file line by line or char by char
     * @param excludePattern a regex pattern wanted to be replaced with whitespace
     * @param encoding encoding
     * @return a StringBuilder which has content of the file
     * @throws IOException if input/output stream operation exception occurs
     */
    public static StringBuilder readFileContent(String filePath,
                                                boolean readLineByLine,
                                                String excludePattern,
                                                String encoding)
            throws IOException {
        Assert.hasText(filePath, "'filePath' cannot be null or empty");
        StringBuilder sb = null;
        BufferedReader br = null;
        InputStreamReader in = null;
        try {
            if(!StringUtils.hasText(encoding))
                encoding = DEFAULT_ENCODING;
            in = new InputStreamReader(FileUtils.class.getClassLoader().getResourceAsStream(filePath), encoding);
            br = new BufferedReader(in);
            int r;
            String str;
            sb = new StringBuilder("");
            if(readLineByLine) {
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
            } else {
                while ((r = br.read()) != -1) {
                    sb.append((char) r);
                }
            }
            if(sb != null && StringUtils.hasText(excludePattern))
                sb.toString().replaceAll(excludePattern, "");
        } finally {
            br.close();
            in.close();
        }
        return sb;
    }
}
