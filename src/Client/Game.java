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
    private String xo;
    private String[] gameState;
    private volatile boolean gameOver = false;
    private volatile boolean firstInit = true;

//    GUI components
    JButton[] buttons = new JButton[9];
    JLabel turn = new JLabel();

    public Game(ClientWindow clientWindow, String opponentId, boolean yourTurn) {
        this.opponentId = opponentId;
        this.clientWindow = clientWindow;
        this.yourTurn = yourTurn;
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
            turn = new JLabel("Your turn");
        }else {
            turn = new JLabel("Your opponent's turn");
        }
        JPanel turnPanel = new JPanel();
        turnPanel.add(turn);
        clientWindow.mainPanel.add(turnPanel);
        gameLoop();

        SwingUtilities.updateComponentTreeUI(clientWindow);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();


        for(int i = 0; i < 9; i++){
            if(source == buttons[i] && yourTurn){
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
            SwingUtilities.updateComponentTreeUI(clientWindow);

        }else{
            turn.setText("Your opponent's turn");
            SwingUtilities.updateComponentTreeUI(clientWindow);
        }
    }
}
