package com.company.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    public Properties getPropertiesFile() throws IOException {
        FileInputStream fis = new FileInputStream("resources/application.properties");
        ;

        Properties prop = new Properties();
        prop.load(fis);

        return prop;
    }
}
