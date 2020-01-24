package Server;

import java.io.BufferedReader;
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
    public volatile boolean lookingForTheGame = false;
    public volatile  boolean currentlyPlaying = false;
    public volatile Duel duel;
    public volatile String opponentId;
    public volatile boolean ready = false;

    public Player(Socket socket, String id, String ipAddress, int port) {
        this.socket = socket;
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        playerThread();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getPort() {
        return port;
    }

    public String getId() {
        return id;
    }

    private void playerThread(){
        new Thread(()->{
            try(
                    BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                    PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true)
            ){
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
                        GameHandler.startDuel(this);
                        while (!currentlyPlaying){
                            //wait
                        }
                        System.out.println("starting for player " + id);
                        out.println(opponentId);
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
