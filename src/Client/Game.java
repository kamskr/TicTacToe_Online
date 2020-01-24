package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Game implements ActionListener {

    private ClientWindow clientWindow;
    private String opponentId;
    private boolean yourTurn;
    private String xo;
    private String[] gameState;
//    GUI components
    JButton[] buttons = new JButton[9];

    JButton button1 = new JButton("");
    JButton button2 = new JButton("");
    JButton button3 = new JButton("");
    JButton button4 = new JButton("");
    JButton button5 = new JButton("");
    JButton button6 = new JButton("");
    JButton button7 = new JButton("");
    JButton button8 = new JButton("");
    JButton button9 = new JButton("");

    JLabel turn = new JLabel();

    public Game(ClientWindow clientWindow, String opponentId, boolean starting) {
        this.opponentId = opponentId;
        this.clientWindow = clientWindow;
        this.yourTurn = yourTurn;

        for(int i = 0; i < 9; i++){
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 40));
        }
        initGame();

    }
    private void initGame(){
        clientWindow.gamePanel.removeAll();
        clientWindow.gamePanel.setLayout(new GridLayout(3,3));
        clientWindow.gamePanel.add(button1);
        clientWindow.gamePanel.add(button2);
        clientWindow.gamePanel.add(button3);
        clientWindow.gamePanel.add(button4);
        clientWindow.gamePanel.add(button5);
        clientWindow.gamePanel.add(button6);
        clientWindow.gamePanel.add(button7);
        clientWindow.gamePanel.add(button8);
        clientWindow.gamePanel.add(button9);
        if(yourTurn) {
            turn = new JLabel("Your turn");
        }else {
            turn = new JLabel("Your opponent's turn");
        }
        JPanel turnPanel = new JPanel();
        turnPanel.add(turn);
        clientWindow.mainPanel.add(turnPanel);
        updateGameState();

        SwingUtilities.updateComponentTreeUI(clientWindow);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if(source == buttons[0]){
            gameState[0] = xo;
        }
    }

    private void updateGameState(){
        try{
            while (!clientWindow.in.ready()){
                System.out.println("INFO: Waiting for current state of the game");
                TimeUnit.MILLISECONDS.sleep(200);
            }
            gameState = clientWindow.in.readLine().split(";");

            System.out.println(gameState.toString());
        }catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
