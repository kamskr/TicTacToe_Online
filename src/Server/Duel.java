package Server;

import java.io.*;
import java.net.DatagramSocket;


public class Duel implements Runnable{
    private boolean started = false;
    private Player player1;
    private Player player2;
    private String[] gameState = new String[9];
    private volatile boolean finished = false;
    private BufferedReader in1;
    private BufferedReader in2;
    private PrintWriter out1;
    private PrintWriter out2;
    public int port;
    private DatagramSocket publisherSocket;
    private MulticastPublisher publisher;



    public Duel(Player player1,PrintWriter out1,BufferedReader in1) {
        this.player1 = player1;
        this.out1 = out1;
        this.in1 = in1;
        try {
            this.publisherSocket = new DatagramSocket();
            this.port = publisherSocket.getLocalPort();
            publisherSocket.close();
            publisher = new MulticastPublisher(this.port);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isStarted() {
        return started;
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
        finished = true;
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

            String input = in.readLine();
            System.out.println(input);
            if (input.equals("FINISH")) finishGame();
            gameState = input.split(";");


        }catch (IOException e){
            System.out.println("INFO: Player logged out");;
        }
    }

    private synchronized void sendGameState(){

        String state ="";
        if(gameState.length == 1) return;
            for (int i = 0; i < 9; i++) {
                if (i < 8) {
                    state += gameState[i] + ";";
                } else {
                    state += gameState[i];
                }
            }

        System.out.println("INFO: Sending state: " + state);
        out1.println(state);
        out2.println(state);

        publisher.broadcast(player1.getId() + "#" + player1.xo + "#" + player2.getId() + "#" + player2.xo + "#"  + state);
    }
}
