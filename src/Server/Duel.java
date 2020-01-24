package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Duel implements Runnable{
    private boolean started = false;
    private Player player1;
    private Player player2;
    private Socket player1Socket;
    private Socket player2Socket;
    private String[] gameState = new String[9];
    private volatile boolean finished = false;

    public Duel(Player player1) {
        this.player1 = player1;

    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {

        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
        startGame();
    }

    private void startGame(){

        player1.opponentId = player2.getId();
        player2.opponentId = player1.getId();

        player1Socket = player1.getSocket();
        player2Socket = player2.getSocket();

        player1.currentlyPlaying = true;
        player2.currentlyPlaying = true;
        player1.duel = this;
        player2.duel = this;

        for(int i = 0; i < 9; i++){
            gameState[i] = "";
        }

        started = true;
        System.out.println("INFO: Game started");
        new Thread(this).start();
    }

    private void finishGame(){
        player1.currentlyPlaying = false;
        player2.currentlyPlaying = false;
        player1.duel = null;
        player2.duel = null;
        GameHandler.duels.remove(this);
        finished = false;
    }

    @Override
    public void run() {
        while(!finished){

            if(player1.ready && player2.ready) sendGameState();

        }
    }

    private void sendGameState(){
        try(
                ObjectOutputStream os1 = new ObjectOutputStream(player1Socket.getOutputStream());
                ObjectOutputStream os2 = new ObjectOutputStream(player2Socket.getOutputStream())
        ){
            os1.writeObject(gameState);
            os1.flush();
            os2.writeObject(gameState);
            os2.flush();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
