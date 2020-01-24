package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class GameServer {

    static PlayerList playerList;
    boolean serverStarted = false;
    ServerSocket server = null;
    Socket client = null;
    Scanner in = new Scanner(System.in);
    String uniqueID;

    public void initServer(int port) {
        playerList = new PlayerList();
        try {
            server = new ServerSocket(port);
            serverStarted = true;
        } catch (Exception e) {
            System.out.println("ERR: Couldn't start the server");
            e.printStackTrace();
        }

        listen();

    }

    private void listen(){
        System.out.println("INFO: Server listens on port: " + server.getLocalPort());
        while (true) {
            try {
                client = server.accept();
                uniqueID = UUID.randomUUID().toString();

                System.out.println("INFO: New connection, player id: " + uniqueID);

            } catch (IOException var4) {
                System.out.println("Accept failed");
                System.exit(-1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            playerList.add(uniqueID, new Player(client,uniqueID,client.getLocalAddress().getHostAddress(), client.getLocalPort()));
        }
    }

    public static void main(String[] args) {
            new GameServer().initServer(4444);

    }
}
