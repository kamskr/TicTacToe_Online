package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Player {
    private Socket socket;
    private String id;
    private String ipAddress;
    private int port;
    public boolean wantsToPlay = false;
    public boolean currentlyPlaying = false;

    public Player(Socket socket, String id, String ipAddress, int port) {
        this.socket = socket;
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        playerThread();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
                        System.out.println("Player (" + id + ") looking for the game");
                        wantsToPlay = true;
                        GameServer.gameHandler.startDuel(this);
                    }
                }
                System.out.println("INFO: Player with id: " + id + " logged out");
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
                ", wantsToPlay=" + wantsToPlay +
                ", currentlyPlaying=" + currentlyPlaying +
                '}';
    }
}
