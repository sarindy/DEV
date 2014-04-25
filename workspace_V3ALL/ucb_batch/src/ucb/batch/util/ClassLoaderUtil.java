package ucb.batch.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderUtil {

	private static final String prop = "java.class.path";

	private static final Class<?> CLASS_LOADER = URLClassLoader.class;

	private static final Class[] PARAMETERS = new Class[] { URL.class };

	public static void addJarFile(String... jarFiles) {
		try {

			if (jarFiles.length == 0)
				return;

			String path = getSystemProperty(prop);

			for (String jarFile : jarFiles) {
				File file = null;
				try {
					if ((file = existJar(jarFile)) != null) {
						add(file.toURL());
						if (path.length() > 0) {
							path += File.pathSeparator;
						}
						path += jarFile;
					}

				} catch (Exception ie) {
					ie.printStackTrace();
				}
			}
			System.setProperty(prop, path);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static File existJar(String jarFile) {
		File file = null;
		try {
			file = new File(jarFile);
			if(!(file.exists() && file.getAbsolutePath().endsWith(".jar"))){
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	public static String getSystemProperty(String prop) {
		String property = "";
		try {
			property = System.getProperty(prop);
			if (property == null) {
				property = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return property;
	}

	public static void add(URL url) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method method = CLASS_LOADER.getDeclaredMethod("addURL", PARAMETERS);
		method.setAccessible(true);
		method.invoke(getClassLoader(), new Object[] { url });
	}

	private static URLClassLoader getClassLoader() {
		return (URLClassLoader) ClassLoader.getSystemClassLoader();
	}

	public static void main(String args[]) {
		System.out.println(System.getProperty("java.class.path"));
		System.out.println(System.getProperty("java.class.path"));
	}
}
