import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject; 
    
public class PrinterImp extends UnicastRemoteObject implements Printer {

	private static final long serialVersionUID = 1L;

	protected PrinterImp() throws RemoteException {
        super();
    }

    public void print(Message receivedMessage) throws RemoteException {
    	System.out.println(receivedMessage);
    }

}