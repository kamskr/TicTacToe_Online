package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastPublisher {
    private DatagramSocket socket;
    private InetAddress group;
    private int broadcastPort;

    public MulticastPublisher(int broadcastPort) {
    this.broadcastPort = broadcastPort;
        try {
            this.socket = new DatagramSocket();
            this.group = InetAddress.getByName("230.0.0.0");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void broadcast(String message){

        new Thread(()->{
            byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, broadcastPort);
            try {
                socket.send(packet);
            }catch (Exception e){
                e.printStackTrace();
            }

        }).start();
    }
}
