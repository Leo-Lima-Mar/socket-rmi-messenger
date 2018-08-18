
import java.io.IOException;
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject; 
    
public class MessengerImp extends UnicastRemoteObject implements Messenger {

	private static final long serialVersionUID = 1L;

	private MulticastPublisher publisher = null;
	
	protected MessengerImp() throws RemoteException {
        super();
		try {
			this.publisher = new MulticastPublisher();
		} catch (IOException e) {
			System.out.println("[MessengerImp] Erro ao instanciar publisher:");
			System.out.println(e.getMessage());
		}
    }

    public boolean send(Message multicastMessage) throws RemoteException {
    	try {
			this.publisher.multicast(multicastMessage);
		} catch (IOException e) {
			System.out.println("[MessengerImp] Erro ao enviar mensagem multicast:");
			System.out.println(e.getMessage());
			return false;
		}
    	return true;
    }

}