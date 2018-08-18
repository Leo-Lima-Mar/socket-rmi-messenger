
import java.rmi.Remote; 
import java.rmi.RemoteException; 
    
public interface Messenger extends Remote {

    public boolean send(Message multicastMessage) throws RemoteException;

}
