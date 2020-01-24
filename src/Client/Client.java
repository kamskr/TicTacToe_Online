package Client;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
//    ipconfig getifaddr en0

    String ipAddress;
    int port;
    static Socket socket;
    PrintWriter out;
    BufferedReader in;

    public Client(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        initClient();
    }

    private void initClient(){
        try{
            socket = new Socket(ipAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String playerId = in.readLine();
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ClientWindow(out, in, playerId);
                }
            });

        } catch (UnknownHostException var8) {
            System.out.println("ERR: Unknown host");
            System.exit(-1);
        } catch (IOException var9) {
            System.out.println("ERR: Couldn't connect to the server");
            System.exit(-1);
        }

    }

    public static void main(String[] args) {
//        replace with ip of the server
        try {
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            int port = 4444;

            new Client(ipAddress, port);
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
    }
}
