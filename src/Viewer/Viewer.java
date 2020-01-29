package Viewer;

import javax.swing.*;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class Viewer extends JFrame {
    private MulticastSocket socket;
    private String player1;
    private String player1xo;
    private String player2;
    private String player2xo;
    private String state;
    private boolean viewStarted = false;
    String[] gameState;
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private boolean player1won = false;
    private boolean player2won = false;
    private boolean gameDrawed = false;


//    GUI elements
    private JPanel mainPanel;
    private JTextArea players;
    private JPanel gamePanel;
    JButton[] buttons = new JButton[9];

    public Viewer(int port) {
        super("Tic-Tac-Toe Viewer");
        viewerInit();
        try {
            this.socket = new MulticastSocket(port);
            InetAddress group = InetAddress.getByName("230.0.0.0");
            socket.joinGroup(group);

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Viewer listens on port: " + port);
        listen();
    }

    private void viewerInit(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        players = new JTextArea("Waiting for game state");


        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3,3));

        for(int i = 0; i < 9; i++){
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 40));
            gamePanel.add(buttons[i]);
        }

        mainPanel.add(players, BorderLayout.NORTH);
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        add(mainPanel);

        setVisible(true);
    }

    private void listen(){
        while (true){
            try {
                byte[] buf = new byte[1000];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());

                System.out.println(message);
                updateGameState(message);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private void updateGameState(String nMessage){
        String[] messageArr = nMessage.split("#");
        player1 = messageArr[0];
        player1xo = messageArr[1];
        player2 = messageArr[2];
        player2xo = messageArr[3];
        state = messageArr[4];


        String whoPlays = "Game between: " + "\n" + "Player1: " + player1 + "(" + player1xo + ")" + "\n"
                + "AND" + "\n" + "Player2: " + player2 + "(" + player2xo + ")";

        System.out.println("Recieved: " + nMessage);
        System.out.println("Game between:" );
        System.out.println("Player1: " + player1 + "(" + player1xo + ")");
        System.out.println("and");
        System.out.println("Player2: " + player2 + "(" + player2xo + ")");
        System.out.println("State: " + state);


        gameState = state.split(";");
        for(int i = 0; i <9; i++){
            buttons[i].setText(gameState[i]);
        }

        checkWin();

        if(player1won){
            whoPlays += "\n" + "Player 1 WON!";
        }else if(player2won){
            whoPlays += "\n" + "Player 2 WON!";
        }else if(gameDrawed){
            whoPlays += "\n" + "Player 2 WON!";
        }else{
            //do nothing
        }

        players.setText(whoPlays);
        SwingUtilities.updateComponentTreeUI(this);

    }

    private synchronized boolean checkWin(){
        if(gameState[0].equals(gameState[4]) && gameState[4].equals(gameState[8]) && !gameState[0].equals(" ")){
            buttons[0].setForeground(Color.BLUE);
            buttons[4].setForeground(Color.BLUE);
            buttons[8].setForeground(Color.BLUE);
            if(gameState[0].equals(player1xo)){
                player1won = true;
            }else{
                player2won = true;
            }
            return true;
        }else if(gameState[0].equals(gameState[1]) && gameState[1].equals(gameState[2]) && !gameState[0].equals(" ")){
            buttons[0].setForeground(Color.BLUE);
            buttons[1].setForeground(Color.BLUE);
            buttons[2].setForeground(Color.BLUE);
            if(gameState[0].equals(player1xo)){
                player1won = true;
            }else{
                player2won = true;
            }
            return true;
        }else if(gameState[3].equals(gameState[4]) && gameState[4].equals(gameState[5]) && !gameState[3].equals(" ")){
            buttons[3].setForeground(Color.BLUE);
            buttons[4].setForeground(Color.BLUE);
            buttons[5].setForeground(Color.BLUE);
            if(gameState[3].equals(player1xo)){
                player1won = true;
            }else{
                player2won = true;
            }
            return true;
        }else if(gameState[6].equals(gameState[7]) && gameState[7].equals(gameState[8])  && !gameState[6].equals(" ")){
            buttons[6].setForeground(Color.BLUE);
            buttons[7].setForeground(Color.BLUE);
            buttons[8].setForeground(Color.BLUE);
            if(gameState[6].equals(player1xo)){
                player1won = true;
            }else{
                player2won = true;
            }
            return true;
        }else if(gameState[0].equals(gameState[3]) && gameState[3].equals(gameState[6]) && !gameState[0].equals(" ")){
            buttons[0].setForeground(Color.BLUE);
            buttons[3].setForeground(Color.BLUE);
            buttons[6].setForeground(Color.BLUE);
            if(gameState[0].equals(player1xo)){
                player1won = true;
            }else{
                player2won = true;
            }
            return true;
        }else if(gameState[2].equals(gameState[5]) && gameState[5].equals(gameState[8]) && !gameState[2].equals(" ")){
            buttons[2].setForeground(Color.BLUE);
            buttons[5].setForeground(Color.BLUE);
            buttons[8].setForeground(Color.BLUE);
            if(gameState[2].equals(player1xo)){
                player1won = true;
            }else{
                player2won = true;
            }
            return true;
        }else if(gameState[1].equals(gameState[4]) && gameState[4].equals(gameState[7]) && !gameState[1].equals(" ")){
            buttons[1].setForeground(Color.BLUE);
            buttons[4].setForeground(Color.BLUE);
            buttons[7].setForeground(Color.BLUE);
            if(gameState[1].equals(player1xo)){
                player1won = true;
            }else{
                player2won = true;
            }
            return true;
        }else if(gameState[6].equals(gameState[4]) && gameState[4].equals(gameState[2]) && !gameState[6].equals(" ")){
            buttons[6].setForeground(Color.BLUE);
            buttons[4].setForeground(Color.BLUE);
            buttons[2].setForeground(Color.BLUE);
            if(gameState[6].equals(player1xo)){
                player1won = true;
            }else{
                player2won = true;
            }

            return true;
        }else {
            checkDraw();
            return false;
        }
    }
    private void checkDraw() {
        gameDrawed = true;

        for (String s : gameState) {
            if (s.equals(" ")) {
                gameDrawed = false;
            }
        }
    }


    public static void main(String[] args) {
        int port = 49706;

        try {
            new Viewer(port);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
