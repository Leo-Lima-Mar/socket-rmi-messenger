
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class MessengerClient {

    public static void main(String[] args) {
    	ClientConfig.load();
		BufferedReader inData = new BufferedReader(new InputStreamReader(System.in));
		
		String clientName = startClient(inData);

    	while (handleInput(inData, clientName));

    	System.out.println("[MessengerClient] Finalizando...");
    	System.exit(0);
    }

    private static String startClient(BufferedReader inData) {
    	String hostName = getHostName(), clientUsername = "", clientName = "";
    	do {
    		clientUsername = readClientUsername(inData);
    		if (hostName.length() > 0) {
    			clientName = hostName + '/' + clientUsername;
    		} else {
    			clientName = clientUsername;
    		}
    		
    	} while (isNameTaken(clientName));
		bindToRegistry(clientName);
		sendMessage(new Message(ClientConfig.enteringMessage, clientName));
		return clientName;
    }
    
    private static String getHostName() {
    	try {
    		return InetAddress.getLocalHost().getHostName();
    	} catch (UnknownHostException e) {
    		
    	}
    	return "";
    }
    
    private static String readClientUsername(BufferedReader inData) {
		System.out.println("[MessengerClient] Digite o nome deste cliente:");
		try {
			return inData.readLine();
		} catch (IOException e) {
			System.out.println("[MessengerClient] Erro ao ler nome do cliente no console:");
			System.out.println(e.getMessage());
		}
		return null;
    }
    
    private static void bindToRegistry(String clientName) {
        try { 
        	Printer c = new PrinterImp();
            Naming.rebind(getClientServiceName(clientName), c);
        } catch (Exception e) {
			System.out.println("[MessengerClient] Erro ao iniciar servidor RMI do cliente:");
			System.out.println(e.getMessage());
			System.exit(0);
        }
    }
    
    private static boolean isNameTaken(String clientName) {
    	String[] services = findRegisteredServices();
    	
		for (int i = 0; i < services.length; i++) {
			if (services[i].equals(getClientServiceName(clientName).substring(4))) {
				System.out.println("[MessengerClient] Nome ja em uso, escolha outro.");
				return true;
			}
		}
		return false;
    }
    
    private static String[] findRegisteredServices() {
		try {
			return Naming.list(getRmiRegistryName());
		} catch (Exception e) {
			System.out.println("[MessengerClient] Erro ao buscar do rmi registry.");
			System.out.println(e.getMessage());
		}
		return null;
    }
    
	private static String getRmiRegistryName() {
		return "rmi://" + ClientConfig.rmiRegistryHost + ":" + ClientConfig.rmiRegistryPort + "/";
	}
    
    
	private static boolean handleInput(BufferedReader inData, String clientName) {
		String input = readMessage(inData);	
		if (input.equals(ClientConfig.quitCommand)) {
			sendMessage(new Message(ClientConfig.leavingMessage, clientName));
			unbindFromRegistry(clientName);
			return false;
		}
		sendMessage(new Message(input, clientName));
		return true;
	}
	
    private static String readMessage(BufferedReader inData) {
		try {
			return inData.readLine();
		} catch (IOException e) {
			System.out.println("[MessengerClient] Erro ao ler mensagem do console:");
			System.out.println(e.getMessage());
		}
		return null;
    }
    
	private static void sendMessage(Message message) {
		Messenger service = getMessenger();
		boolean success = true;
		try {
			success = service.send(message);
		} catch (RemoteException e) {
			System.out.println("[MessengerClient] Falha ao enviar mensagem para o servidor:");
			System.out.println(e.getMessage());
		}
		
		if (!success) {
			System.out.println("[MessengerClient] O servidor nao conseguiu enviar a mensagem para os outros membros do grupo.");
		}
	}
    
    private static void unbindFromRegistry(String clientName) {
		try {
			Naming.unbind(getClientServiceName(clientName));
		} catch (Exception e) {
			System.out.println("[MessengerClient] Erro ao remover serviço RMI do RMI Registry.");
			System.out.println(e.getMessage());
		}
    }
    
    private static Messenger getMessenger() {
        try {
        	return (Messenger) Naming.lookup(getServerServiceName());
        } catch (Exception e) { 
			System.out.println("[MessengerClient] Erro ao iniciar client RMI:");
			System.out.println(e.getMessage());
        }
        return null;
    }
    
	private static String getClientServiceName(String clientName) {
		return getRmiRegistryName() + ClientConfig.clientServiceName + "-" + clientName;
	}
    
	private static String getServerServiceName() {
		return getRmiRegistryName() + ClientConfig.serverServiceName;
	}
    
}