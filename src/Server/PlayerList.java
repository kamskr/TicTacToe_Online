package Server;

import java.util.HashMap;
import java.util.Map;

class PlayerList {

    private Map<String, Player> activePlayers = new HashMap<>();

    void add(String id, Player player){
        activePlayers.put(id, player);
    }

    synchronized void remove(String id){
        activePlayers.remove(id);
    }

    synchronized String listAllActivePlayers(){
        StringBuilder list = new StringBuilder("Active players: ;");
        for (Map.Entry<String, Player> entry : activePlayers.entrySet()) {

            list.append("ID: ").append(entry.getKey()).append(" || IP address: ").append(entry.getValue().getIpAddress()).append(" || port: ").append(entry.getValue().getPort()).append(";");
        }
        return list.toString();
    }
}
