package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;

public class Player {
    private Socket socket;
    private String id;
    private String ipAddress;
    private int port;
    PrintWriter out;
    BufferedReader in;
    volatile boolean hisTurn = false;
    volatile boolean lookingForTheGame = false;
    volatile  boolean currentlyPlaying = false;
    volatile Duel duel;
    volatile String opponentId;
    volatile boolean ready = false;
    public String xo;

    Player(Socket socket, String id, String ipAddress, int port) {
        this.socket = socket;
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }catch (IOException e){
            e.printStackTrace();
        }
        playerThread();
    }

    String getIpAddress() {
        return ipAddress;
    }

    Socket getSocket() {
        return socket;
    }

    int getPort() {
        return port;
    }

    String getId() {
        return id;
    }

    private void playerThread(){
        new Thread(()->{
            try{
                out.println(id);
                String line;
                while ((line = in.readLine()) != null && !line.equals("LOGOUT")){
                    System.out.println(line);

                    if(line.equals("LIST")){
                        String list = GameServer.playerList.listAllActivePlayers();
                        out.println(list);
                    }

                    if(line.equals("PLAY")){
                        lookingForTheGame = true;
                        System.out.println("INFO: Player (" + id + ") looking for the game");
                        int viewerPort = GameHandler.startDuel(this);
                        while (!currentlyPlaying) {
                            Thread.onSpinWait();
                            //wait
                        }
                        System.out.println("Starting for player " + id);
                        out.println(opponentId + ";" + viewerPort);
                        if(hisTurn){
                            out.println("true");
                            xo = "o";
                        }else{
                            out.println("false");
                            xo = "x";
                        }
                        ready = true;
                        while (currentlyPlaying){
                            //wait
                        }
                        ready = false;

                    }
                }
                System.out.println("INFO: Player with id: " + id + " logged out");

//               Remove duel from search and close sockets
                Optional<Duel> nDuel = GameHandler.duels.stream().parallel().filter(duel -> !duel.isStarted()).findAny();
                if(nDuel.isPresent()){
                    GameHandler.duels.remove(nDuel.get());
                }
                GameServer.playerList.remove(this.id);
                in.close();
                out.close();
                socket.close();
            }catch (SocketException e){
                System.out.println("INFO: Player with id: " + id + " logged out");
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public String toString() {
        return "Player{" +
                "socket=" + socket +
                ", id='" + id + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                ", currentlyPlaying=" + currentlyPlaying +
                '}';
    }
}
