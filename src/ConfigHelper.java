import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigHelper {
	
	public static FileInputStream openConfigFile() {
		try {
			return new FileInputStream( "./config.properties");
		} catch (FileNotFoundException e) {
			System.out.println("[ClientConfig] Arquivo de configuracoes nao encontrado.");
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static Properties loadConfigFromFile(FileInputStream file) {
		Properties config = new Properties();
		try {
			config.load(file);
		} catch (IOException e) {
			System.out.println("[ClientConfig] Erro ao carregar arquivo de configuracoes.");
			System.out.println(e.getMessage());
		}
		return config;
	}
	
	public static int getIntProperty(Properties config, String property) {
		try {
			return Integer.parseInt(config.getProperty(property));
		}
		catch (NumberFormatException e) {
			System.out.println("[ClientConfig] A propriedade " + property + " deve ser um numero inteiro.");
		}
		return 0;
	}

}
