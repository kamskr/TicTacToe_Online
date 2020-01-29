package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Game implements ActionListener {

    private ClientWindow clientWindow;
    private String opponentId;
    private boolean yourTurn;
    private volatile boolean youWon;
    private String xo;
    private volatile String[] gameState;
    private volatile boolean gameOver = false;
    private volatile boolean firstInit = true;
    private volatile boolean draw = false;
    private int viewerPort;

//    GUI components
    JButton[] buttons = new JButton[9];
    JTextArea turn;
    JPanel turnPanel;
    JLabel viewPort = new JLabel();

    public Game(ClientWindow clientWindow, String opponentId, boolean yourTurn, int viewerPort) {
        this.opponentId = opponentId;
        this.clientWindow = clientWindow;
        this.yourTurn = yourTurn;
        this.viewerPort = viewerPort;
        if(yourTurn){
            this.xo = "o";
        }else{
            this.xo = "x";
        }

        for(int i = 0; i < 9; i++){
            buttons[i] = new JButton("");
            buttons[i].addActionListener(this);
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 40));
        }
        initGame();

    }

    private void gameLoop(){
        new Thread(()->{
            while (!gameOver) {
                updateGameState();
            }
        }).start();
    }

    private void initGame(){
        clientWindow.gamePanel.removeAll();
        clientWindow.gamePanel.setLayout(new GridLayout(3,3));
        for(int i = 0; i < 9; i++){
            clientWindow.gamePanel.add(buttons[i]);
        }

        if(yourTurn) {
            turn = new JTextArea("Your turn");
        }else {
            turn = new JTextArea("Your opponent's turn");
        }
        turn.append("\n\n\n\n\n" + "This game is being broadcasted on port: " + String.valueOf(viewerPort));
        turnPanel = new JPanel();
        turnPanel.setLayout(new BorderLayout());
        turnPanel.add(turn, BorderLayout.CENTER);
        turnPanel.add(viewPort, BorderLayout.SOUTH);
        clientWindow.mainPanel.add(turnPanel);
        gameLoop();

        SwingUtilities.updateComponentTreeUI(clientWindow);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        for(int i = 0; i < 9; i++){
            if(source == buttons[i] && yourTurn && !gameOver){
                System.out.println("ACTION!");
                gameState[i] = xo;
                buttons[i].setText(xo);
                buttons[i].removeActionListener(this);
                sendUpdatedState();
            }
        }
    }

    private void sendUpdatedState(){
        System.out.println("INFO: Sending updated state");
        String state ="";
        for(int i = 0; i < 9; i++){
            if(i<8) {
                state += gameState[i] + ";";
            }else{
                state += gameState[i];
            }
        }
        clientWindow.out.println(state);
    }

    private synchronized void updateGameState(){
        try{
            while (!clientWindow.in.ready()){}

            gameState = clientWindow.in.readLine().split(";");

            for(int i = 0; i <9; i++){
                buttons[i].setText(gameState[i]);
                if(!gameState[i].equals(" ")){
                    buttons[i].removeActionListener(this);
                }
            }

            if(checkWin()){
                SwingUtilities.updateComponentTreeUI(clientWindow);
                System.out.println("Finish game!!");
                finishGame();
                return;
            }


            if(!firstInit) {
                changeSides();
            }else {
                firstInit = false;
            }

            System.out.println("INFO: State updated");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private synchronized void changeSides(){
        yourTurn = !yourTurn;
        if(yourTurn){
            turn.setText("Your turn");
            turn.append("\n\n\n\n\n" + "This game is being broadcasted on port: " + String.valueOf(viewerPort));
            SwingUtilities.updateComponentTreeUI(clientWindow);

        }else{
            turn.setText("Your opponent's turn");
            turn.append("\n\n\n\n\n" + "This game is being broadcasted on port: " + String.valueOf(viewerPort));
            SwingUtilities.updateComponentTreeUI(clientWindow);
        }
    }

    private synchronized void finishGame(){
        if(!draw) {
            System.out.println(youWon);
            if (youWon) {
                turn.setText("You won!");
                turn.append("\n\n\n\n\n" + "This game is being broadcasted on port: " + String.valueOf(viewerPort));
            } else {
                turn.setText("You lost...");
                turn.append("\n\n\n\n\n" + "This game is being broadcasted on port: " + String.valueOf(viewerPort));
                clientWindow.out.println("FINISH");
            }
        }else{
            System.out.println("Draw");
            turn.setText("Draw!");
            turn.append("\n\n\n\n\n" + "This game is being broadcasted on port: " + String.valueOf(viewerPort));
            if (!yourTurn){
                clientWindow.out.println("FINISH");
            }
        }

        turnPanel.add(clientWindow.backButton, BorderLayout.SOUTH);


        SwingUtilities.updateComponentTreeUI(clientWindow);

    }

    private synchronized boolean checkWin(){
        if(gameState[0].equals(gameState[4]) && gameState[4].equals(gameState[8]) && !gameState[0].equals(" ")){
            buttons[0].setForeground(Color.BLUE);
            buttons[4].setForeground(Color.BLUE);
            buttons[8].setForeground(Color.BLUE);
            gameOver = true;
            if(gameState[0].equals(xo)){
                youWon = true;
            }else{
                youWon = false;
            }
            return true;
        }else if(gameState[0].equals(gameState[1]) && gameState[1].equals(gameState[2]) && !gameState[0].equals(" ")){
            buttons[0].setForeground(Color.BLUE);
            buttons[1].setForeground(Color.BLUE);
            buttons[2].setForeground(Color.BLUE);
            gameOver = true;
            if(gameState[0].equals(xo)){
                youWon = true;
            }else{
                youWon = false;
            }
            return true;
        }else if(gameState[3].equals(gameState[4]) && gameState[4].equals(gameState[5]) && !gameState[3].equals(" ")){
            buttons[3].setForeground(Color.BLUE);
            buttons[4].setForeground(Color.BLUE);
            buttons[5].setForeground(Color.BLUE);
            gameOver = true;
            if(gameState[3].equals(xo)){
                youWon = true;
            }else{
                youWon = false;
            }
            return true;
        }else if(gameState[6].equals(gameState[7]) && gameState[7].equals(gameState[8])  && !gameState[6].equals(" ")){
            buttons[6].setForeground(Color.BLUE);
            buttons[7].setForeground(Color.BLUE);
            buttons[8].setForeground(Color.BLUE);
            gameOver = true;
            if(gameState[6].equals(xo)){
                youWon = true;
            }else{
                youWon = false;
            }
            return true;
        }else if(gameState[0].equals(gameState[3]) && gameState[3].equals(gameState[6]) && !gameState[0].equals(" ")){
            buttons[0].setForeground(Color.BLUE);
            buttons[3].setForeground(Color.BLUE);
            buttons[6].setForeground(Color.BLUE);
            gameOver = true;
            if(gameState[0].equals(xo)){
                youWon = true;
            }else{
                youWon = false;
            }
            return true;
        }else if(gameState[2].equals(gameState[5]) && gameState[5].equals(gameState[8]) && !gameState[2].equals(" ")){
            buttons[2].setForeground(Color.BLUE);
            buttons[5].setForeground(Color.BLUE);
            buttons[8].setForeground(Color.BLUE);
            gameOver = true;
            if(gameState[2].equals(xo)){
                youWon = true;
            }else{
                youWon = false;
            }
            return true;
        }else if(gameState[1].equals(gameState[4]) && gameState[4].equals(gameState[7]) && !gameState[1].equals(" ")){
            buttons[1].setForeground(Color.BLUE);
            buttons[4].setForeground(Color.BLUE);
            buttons[7].setForeground(Color.BLUE);
            gameOver = true;
            if(gameState[1].equals(xo)){
                youWon = true;
            }else{
                youWon = false;
            }
            return true;
        }else if(gameState[6].equals(gameState[4]) && gameState[4].equals(gameState[2]) && !gameState[6].equals(" ")){
            buttons[6].setForeground(Color.BLUE);
            buttons[4].setForeground(Color.BLUE);
            buttons[2].setForeground(Color.BLUE);
            gameOver = true;
            if(gameState[6].equals(xo)){
                youWon = true;
            }else{
                youWon = false;
            }


            return true;
        }else if(checkDraw()){
            gameOver = true;
            draw = true;

            return true;
        } else {
            return false;
        }
    }

    private boolean checkDraw(){
        boolean gameDrawed = true;

        if(gameState.length != 9){
            return false;
        }

        for(String s : gameState){
            if(s.equals(" ")){
                gameDrawed = false;
            }
        }

        return gameDrawed;
    }
}
