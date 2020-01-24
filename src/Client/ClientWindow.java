package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientWindow extends JFrame implements ActionListener {

    PrintWriter out;
    BufferedReader in;
    String playerId;

//    GUI elements
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    JPanel mainPanel;
    JPanel gamePanel;
    JButton playButton;
    JButton listButton;
    JButton logoutButton;
    JButton backButton;
    JTextArea textArea;
    JScrollPane scrollPane;


    public ClientWindow(PrintWriter out, BufferedReader in, String playerId) {
        super("Tic-Tac-Toe");
        this.out = out;
        this.in = in;
        this.playerId = playerId;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

//        Defining all needed GUI components
        playButton = new JButton("Play");
        listButton = new JButton("List");
        logoutButton = new JButton("Logout");
        playButton.addActionListener(this);
        listButton.addActionListener(this);
        logoutButton.addActionListener(this);
        backButton = new JButton("<- Back");
        backButton.addActionListener(this);

        textArea = new JTextArea(20, 20);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        gamePanel = new JPanel();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(new JLabel("Player ID: " + playerId));

        loadMenu();

        add(mainPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        try {
            if (source == playButton) {
                out.println("PLAY");
            } else if (source == listButton) {

                out.println("LIST");

                String input = in.readLine();
                in.readLine();
                listAction(input);

            } else if (source == logoutButton) {
                out.println("LOGOUT");
                System.exit(0);
            } else if (source == backButton){
                loadMenu();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void listAction(String list){
        gamePanel.removeAll();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.PAGE_AXIS));

        textArea.setText(list);

        gamePanel.add(backButton, BorderLayout.NORTH);
        gamePanel.add(scrollPane, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void loadMenu(){

        gamePanel.removeAll();
        gamePanel.setLayout(new GridLayout(3,1));
        gamePanel.add(playButton);
        gamePanel.add(listButton);
        gamePanel.add(logoutButton);
        mainPanel.add(gamePanel);
        SwingUtilities.updateComponentTreeUI(this);
    }
}
