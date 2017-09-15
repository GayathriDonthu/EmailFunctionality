package com.process;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.h2.store.fs.Recorder;

public class LoadProperties {
	
	public static Properties prop = null; 
	public static long SLEEP_TIME;
	public static int RECORD_LIMIT;
	public static int PERCENT;
	public static String STP_STATUS;
	public static String REA_STATUS;
	

	public static void loadProperties() {
		prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			SLEEP_TIME = Long.parseLong(prop.getProperty("SLEEP_TIME"));
			RECORD_LIMIT = Integer.parseInt(prop.getProperty("RECORD_LIMIT"));
			PERCENT = Integer.parseInt(prop.getProperty("PERCENT"));
			STP_STATUS = prop.getProperty("STP_STATUS");
			REA_STATUS = prop.getProperty("REA_STATUS");
		} catch(IOException io) {
			io.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
