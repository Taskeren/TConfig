
import java.io.File;

import TConfig.Configuration;

public class Example {

	private static final Configuration c = new Configuration(new File("config.cfg"));
	
	static {
		refresh();
	}
	
	public static final String CATEGORY_GENERAL = "general";
	
	public static String s;
	public static int i;
	public static boolean b;
	public static float f;
	public static double d;
	public static String[] sa;
	
	public static void refresh() {
		
		s = c.getString("string", CATEGORY_GENERAL, "I'm not a string!", "What you said?");
		i = c.getInt("integer", CATEGORY_GENERAL, 114514, Integer.MIN_VALUE, Integer.MAX_VALUE, "1145141919810");
		b = c.getBoolean("boolean", CATEGORY_GENERAL, true, "Is that 野兽先辈 ?");
		f = c.getFloat("float", CATEGORY_GENERAL, 1919.0f, Float.MIN_VALUE, Float.MAX_VALUE, "1919810!");
		d = c.getDouble("double", CATEGORY_GENERAL, 810.0, Double.MIN_VALUE, Double.MAX_VALUE, "Double is not a good choice!");
		sa = c.getStringList("string array", CATEGORY_GENERAL, new String[] {"Deep", "Dark", "Fantasies"}, "SPACE is a nice charactor!");
		
		// DO NOT FORGET TO SAVE IT !!!!!!!!!!!!!!!!
		c.save();
		
	}
	
}
