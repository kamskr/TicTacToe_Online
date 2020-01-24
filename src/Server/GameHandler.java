package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameHandler {
    static List<Duel> duels = new ArrayList<>();



    static synchronized void startDuel(Player player){
        Optional<Duel> nDuel = duels.stream().parallel().filter(duel -> !duel.isStarted()).findAny();
        if(!nDuel.isPresent() && player.lookingForTheGame){
            duels.add(new Duel(player));
        }else if(player.lookingForTheGame){
            nDuel.get().setPlayer2(player);
        }
    }

}
