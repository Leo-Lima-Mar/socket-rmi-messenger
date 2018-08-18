
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastPublisher {

    private MulticastSocket socket;
    private InetAddress group;
    private byte[] buffer;
 
    public MulticastPublisher() throws IOException {
    	this.socket = new MulticastSocket(ServerConfig.udpSocketPort);
    	this.group = InetAddress.getByName(ServerConfig.udpSocketHost);
    }
    
    public void multicast(Message sendingMessage) throws IOException {
        buffer = this.getBytesOfObject(sendingMessage);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, ServerConfig.udpSocketPort);
        socket.send(packet);
    }
    
    private byte[] getBytesOfObject(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        return baos.toByteArray();
    }

}