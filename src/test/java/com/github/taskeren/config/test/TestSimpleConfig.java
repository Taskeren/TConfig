package com.github.taskeren.config.test;

import com.github.taskeren.config.Configuration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;

public class TestSimpleConfig {

	Logger logger = LoggerFactory.getLogger(TestSimpleConfig.class);

	@Test
	public void testConfiguration() throws Exception {
		File cfgFile = new File("run/general.cfg");

		var c = new Configuration(cfgFile);

		var intProp = c.get("general", "int", 0);

		var intVal = c.getInt("int", "general", 0, 0, Integer.MAX_VALUE, "This is the comment");

		intProp.set(999);

		var intVal1 = c.getInt("int", "general", 0, 0, Integer.MAX_VALUE, "This is the comment");

		logger.info("was {}, after set {}; prop {}", intVal, intVal1, intProp);

		c.save();

		logger.info("File content:\n{}", Files.readString(cfgFile.toPath()));
	}

}
