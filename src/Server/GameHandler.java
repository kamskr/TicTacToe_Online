package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameHandler {
    private PlayerList playerList;
    private List<Duel> duels = new ArrayList<>();

    public GameHandler(PlayerList playerList) {
        this.playerList = playerList;
    }

    public PlayerList getPlayerList() {
        return playerList;
    }

    public void startDuel(Player player){
        Optional<Duel> nDuel = duels.stream().parallel().filter(duel -> !duel.isStarted()).findAny();
        if(!nDuel.isPresent()){
            duels.add(new Duel(player));
        }else{
            nDuel.get().setPlayer2(player);
            System.out.println("Game started");
        }


    }

}
