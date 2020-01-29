package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameHandler {
    static List<Duel> duels = new ArrayList<>();

    static synchronized int startDuel(Player player){
        Optional<Duel> nDuel = duels.stream().parallel().filter(duel -> !duel.isStarted()).findAny();
        if(!nDuel.isPresent() && player.lookingForTheGame){
            Duel eDuel = new Duel(player,player.out, player.in);
            duels.add(eDuel);
            return eDuel.port;
        }else if(player.lookingForTheGame){
            nDuel.get().setPlayer2(player, player.out, player.in);
            return nDuel.get().port;
        }
        return 0;
    }

}
