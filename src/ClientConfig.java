import java.io.FileInputStream;
import java.util.Properties;

public class ClientConfig {

	public static String serverServiceName;
	public static String clientServiceName;
	
	public static String rmiRegistryHost;
	public static int rmiRegistryPort;
	
	public static String enteringMessage;
	public static String leavingMessage;
	public static String quitCommand;

	private static Properties config;
	
	public static void load() {
		FileInputStream file = ConfigHelper.openConfigFile();
		config = ConfigHelper.loadConfigFromFile(file);
		
		serverServiceName = config.getProperty("serverServiceName");
		clientServiceName = config.getProperty("clientServiceName");
		
		rmiRegistryHost = config.getProperty("rmiRegistryHost");
		rmiRegistryPort = ConfigHelper.getIntProperty(config, "rmiRegistryPort");
		
		enteringMessage = config.getProperty("enteringMessage");
		leavingMessage = config.getProperty("leavingMessage");
		quitCommand = config.getProperty("quitCommand");
	}
	
}
