package com.branch.webdriver;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

	private final Properties properties = new Properties();

	public PropertyUtil() {

		// Load config Properties
		try {
			this.properties.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));

		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to load config properties file");
		}

	}

	public String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}

}