
import java.rmi.Naming;
    
public class MessengerServer {
    
    public static void main(String[] args) {
		ServerConfig.load();
        try { 
        	Messenger c = new MessengerImp();
            Naming.rebind(getServerServiceName(), c);
            System.out.println("[MessengerServer] Escutando em " + getServerServiceName() + ".");
        } catch (Exception e) {
			System.out.println("[MessengerServer] Erro ao iniciar servidor RMI:");
			System.out.println(e.getMessage());
        }
        MulticastReceiver receiver = new MulticastReceiver();
		receiver.start();
    }

	private static String getServerServiceName() {
		return "rmi://" + ServerConfig.rmiRegistryHost + ":" + ServerConfig.rmiRegistryPort
			+ "/" + ServerConfig.serverServiceName;
	}

}
