
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buffer = new byte[256];
 
    public void run() {
    	System.out.println("[MulticastReceiver] - Inicializando...");
    	this.createSocket();
        InetAddress group = this.findGroup();
		this.joinGroup(group);
		
		System.out.println("[MulticastReceiver] - Aguardando por mensagens...");
        while (true) {
            this.receiveMessage();
            Message receivedMessage = (Message) this.getObjectFromBytes();
            System.out.println(receivedMessage);
            this.sendMessageToAllClients(receivedMessage);
        }

    }
    
    private void createSocket() {
    	System.out.println("[MulticastReceiver] - Criando Socket na porta " + ServerConfig.udpSocketPort + ".");
        try {
			socket = new MulticastSocket(ServerConfig.udpSocketPort);
		} catch (IOException e) {
			System.out.println("[MulticastReceiver] Erro ao criar socket:");
			System.out.println(e.getMessage());
		}
    }
    
    private InetAddress findGroup() {
		System.out.println("[MulticastReceiver] - Obtendo grupo " + ServerConfig.udpSocketHost + ".");
		try {
			return InetAddress.getByName(ServerConfig.udpSocketHost);
		} catch (UnknownHostException e) {
			System.out.println("[MulticastReceiver] Erro ao tentar obter grupo:");
			System.out.println(e.getMessage());
		}
		return null;
    }
    
    private void joinGroup(InetAddress group) {
        System.out.println("[MulticastReceiver] - Entrando no grupo " + ServerConfig.udpSocketHost);
        try {
        	socket.joinGroup(group);
		} catch (IOException e) {
			System.out.println("[MulticastReceiver] Erro ao tentar entrar no grupo:");
			System.out.println(e.getMessage());
		}
    }
    
    private void receiveMessage() {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
			socket.receive(packet);
		} catch (IOException e) {
			System.out.println("[MulticastReceiver] Erro ao receber pacote:");
			System.out.println(e.getMessage());
		} 
    }
    
    private Object getObjectFromBytes() {
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
		} catch (IOException e) {
			System.out.println("[MulticastReceiver] " + e.getMessage());
		}
		
        try {
            return ois.readObject();
        } catch (Exception e) {
        	System.out.println("[MulticastReceiver] " + e.getMessage());
        }
        return null;
    }
   
    private void sendMessageToAllClients(Message message) {
    	String[] services = this.findRegisteredServices();
		for (int i = 0; i < services.length; i++) {
			if (services[i].contains(ServerConfig.clientServiceName)) {
				this.sendMessageToClient(message, services[i]);
			}
		}
    }
    
    private String[] findRegisteredServices() {
		try {
			return Naming.list(this.getRmiRegistryName());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
    }
    
	private String getRmiRegistryName() {
		return "rmi://" + ServerConfig.rmiRegistryHost + ":" + ServerConfig.rmiRegistryPort + "/";
	}
    
	private void sendMessageToClient(Message message, String client) {
		Printer service = this.getPrinter(client);
		try {
			service.print(message);
		} catch (RemoteException e) {
			System.out.println("[MulticastReceiver] Falha ao enviar mensagem para: " + client + ".");
			System.out.println("[MulticastReceiver] Removendo este client do RMI Registry.");
			this.unbindFromRegistry(client);
		}
	}
	
    private void unbindFromRegistry(String client) {
		try {
			Naming.unbind(client);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
	
    private Printer getPrinter(String client) {
        try {
        	return (Printer) Naming.lookup(client);
        } catch (Exception e) { 
			System.out.println("[MulticastReceiver] Erro ao iniciar cliente RMI do servidor:");
			System.out.println(e.getMessage());
        }
        return null;
    }

}