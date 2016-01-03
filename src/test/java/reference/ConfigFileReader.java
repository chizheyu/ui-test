package reference;

import java.io.*;
import java.util.Properties;

/**
 * Created by dugancaii on 8/1/2014.
 * 读取配置文件
 */
public class ConfigFileReader {
    private String profilepath="/config.properties";

    public ConfigFileReader(){

    }
    public ConfigFileReader(String filePath){
        profilepath= filePath;
    }
    public  String getValue(String key) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(ConfigFileReader.class.getResourceAsStream(profilepath));
            props.load(in);
            String value = props.getProperty(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void setProfilepath (String profilepath) {
        this.profilepath = profilepath;
    }

}
