package Client;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
//    ipconfig getifaddr en0

    String ipAddress;
    int port;
    Socket socket;
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
//        try {
//            out.println("Kamilkora");
//            out.println("s18586");
//            out.println();
//
//            String line;
//            while((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (IOException var10) {
//            System.out.println("Error during communication");
//            System.exit(-1);
//        }
//
//        try {
//            socket.close();
//        } catch (IOException var7) {
//            System.out.println("Cannot close the socket");
//            System.exit(-1);
//        }
    }

    public static void main(String[] args) {
        String ipAddress = "192.168.0.118";
        int port = 4444;

        new Client(ipAddress, port);
    }
}
