import java.io.FileInputStream;
import java.util.Properties;

public class ServerConfig {

	public static String serverServiceName;
	public static String clientServiceName;
	
	public static String rmiRegistryHost;
	public static int rmiRegistryPort;
	
	public static String udpSocketHost;
	public static int udpSocketPort;
	
	private static Properties config;
	
	public static void load() {
		FileInputStream file = ConfigHelper.openConfigFile();
		config = ConfigHelper.loadConfigFromFile(file);
		
		serverServiceName = config.getProperty("serverServiceName");
		clientServiceName = config.getProperty("clientServiceName");
		
		rmiRegistryHost = config.getProperty("rmiRegistryHost");
		rmiRegistryPort = ConfigHelper.getIntProperty(config, "rmiRegistryPort");

		udpSocketHost = config.getProperty("udpSocketHost");
		udpSocketPort = ConfigHelper.getIntProperty(config, "udpSocketPort");
	}

}
