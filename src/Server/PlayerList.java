package Server;

import java.util.HashMap;
import java.util.Map;

public class PlayerList {

    private Map<String, Player> activePlayers = new HashMap<>();

    public synchronized void add(String id, Player player){
        activePlayers.put(id, player);
    }

    public synchronized void remove(String id){
        activePlayers.remove(id);
    }

    public synchronized String listAllActivePlayers(){
        String list = "Active players: \n";
        for (Map.Entry<String, Player> entry : activePlayers.entrySet()) {

            list += "ID: " + entry.getKey() + " || IP address: " + entry.getValue().getIpAddress() + " || port: " + entry.getValue().getPort() + "\n";
        }
        return list;

    }
}
