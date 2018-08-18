
import java.rmi.Remote; 
import java.rmi.RemoteException; 
    
public interface Printer extends Remote {

    public void print(Message receivedMessage) throws RemoteException;

}
