package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Game implements ActionListener {

    private ClientWindow clientWindow;
    private String opponentId;
    private boolean starting;
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
        this.starting = starting;

        for(int i = 0; i < 9; i++){
            buttons[i] = new JButton("");
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
        if(starting) {
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
    public void actionPerformed(ActionEvent e) {

    }

    private void updateGameState(){

        try(ObjectInputStream ois = new ObjectInputStream(Client.socket.getInputStream())
        ){
            gameState = (String[]) ois.readObject();
            System.out.println(gameState);
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }



    }
}
