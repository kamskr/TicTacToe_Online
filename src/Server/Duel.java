package Server;

import java.io.*;
import java.net.Socket;

public class Duel implements Runnable{
    private boolean started = false;
    private Player player1;
    private Player player2;
    private Socket player1Socket;
    private Socket player2Socket;
    private String[] gameState = new String[9];
    private volatile boolean finished = false;
    private BufferedReader in1;
    private BufferedReader in2;
    private PrintWriter out1;
    private PrintWriter out2;

    public Duel(Player player1,PrintWriter out1,BufferedReader in1) {
        this.player1 = player1;
        this.out1 = out1;
        this.in1 = in1;

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

    public void setPlayer2(Player player2, PrintWriter out2,BufferedReader in2) {
        this.player2 = player2;
        this.out2 = out2;
        this.in2 = in2;

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

        int random = (int)Math.round(Math.random());

        if(random == 0) {
            player1.hisTurn = true;
            player2.hisTurn = false;
        }else{
            player2.hisTurn = true;
            player1.hisTurn = false;
        }

        for(int i = 0; i < 9; i++){
            gameState[i] = " ";
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

            if(player1.ready && player2.ready) {
                sendGameState();
                System.out.println("INFO: Sent game state");
                if (player1.hisTurn) {
                    receiveStateFromPlayer(in1);

                } else {
                    receiveStateFromPlayer(in2);
                }
                player1.hisTurn = !player1.hisTurn;
                player2.hisTurn = !player2.hisTurn;
            }
        }
    }

    private synchronized void receiveStateFromPlayer(BufferedReader in){
        try {
            System.out.println("INFO: Waiting for the state from players");
            while (!in.ready()) {}

            String temp = in.readLine();
            System.out.println(temp);
            gameState = temp.split(";");
//            gameState = in.readLine().split(";");

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private synchronized void sendGameState(){

        String state ="";
        for(int i = 0; i < 9; i++){
            if(i<8) {
                state += gameState[i] + ";";
            }else{
                state += gameState[i];
            }
        }
        System.out.println("INFO: Sending state: " + state);
        out1.println(state);
        out2.println(state);


    }
}
