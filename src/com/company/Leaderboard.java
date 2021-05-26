package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

// Leaderboard class
public class Leaderboard {
    // call init Leaderboard function
    public Leaderboard() throws IOException {
        initLeaderboard();
    }
    // initialize global variables
    JFrame rankFrame;

    /* Leaderboard file Path (MUST BE ABSOLUTE PATH when using .Jar)*/
    // Jar will not open Leaderboard screen with the wrong absolute leaderboard file path

    public static String leaderboardPath = "/Users/Jerry/IdeaProjects/Magic2048/out/artifacts/Magic2048_jar2/leaderboard.txt";

    // initialize display panel with gradient background
    JPanel displayPanel = new JPanel(){
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            java.awt.Color c01 = new java.awt.Color(32,156,255);
            java.awt.Color c02 = new java.awt.Color(104,224,207);
            GradientPaint gp = new GradientPaint(0, 0, c01, 0, getHeight(), c02);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    };
    // initialize rank panel with gradient background
    JPanel rankPanel = new JPanel(){
        @Override
        protected void paintComponent(Graphics graphics) {
           super.paintComponent(graphics);
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            java.awt.Color c01 = new java.awt.Color(0,122,223);
            java.awt.Color c02 = new java.awt.Color(0,236,188);
            GradientPaint gp = new GradientPaint(0, 0, c01, 0, getHeight(), c02);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    };
    // initLeaderboard: Create the rank frame and add all the components to the rank frame
    // @Param : none
    // @return: void
    public void initLeaderboard() throws IOException {
        // initialize rank frame
        rankFrame = new JFrame("Magic 2048 - Leaderboard");
        rankFrame.setSize(450,600);
        rankFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rankFrame.setResizable(false);
        rankFrame.setLocationRelativeTo(null);

        // set gridbag layout
        rankPanel.setLayout(new GridBagLayout());
        //https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(5,10,5, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy=0;
        // initialize home button
        JButton homeButton = new JButton ("Home");
        homeButton.setFont(new Font("Open Sans",Font.PLAIN,20));
        homeButton.setBackground(new Color(44,187,195));
        homeButton.setOpaque(true);
        homeButton.setBorderPainted(false);
        homeButton.setForeground(Color.WHITE);
        homeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                new Home();
                rankFrame.dispose();
            }
        });
        rankPanel.add(homeButton,c);

        c.ipady = 40;
        c.gridx = 0;
        c.gridy= 1;
        c.gridwidth = 4;
        // initialize title label
        JLabel title = new JLabel("Leaderboard");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Open Sans",1,50));
        rankPanel.add(title,c);

        c.gridx = 0;
        c.gridy= 2;
        // initialize rank label
        JLabel rankLabels = new JLabel("Rank   Username   Max Tile   Moves");
        rankLabels.setHorizontalAlignment(SwingConstants.CENTER);
        rankLabels.setForeground(Color.WHITE);
        rankLabels.setFont(new Font("Open Sans",1,20));
        rankPanel.add(rankLabels,c);

        c.gridx = 0;
        c.gridy= 3;
        c.gridwidth = 4;
        // call display Leaderboard function
        display_Leaderboard();
        rankPanel.add(displayPanel,c);
        // add to rank frame
        rankFrame.add(rankPanel);
        // set visible
        rankFrame.setVisible(true);
    }
    // saveUserData: save the user's username, maxTile & moves to the leaderboard file
    // @param: int, int
    //@return: void
    public static void saveUserData(int maxTile, int moves) throws IOException {
        // convert moves and max tile to string
        String userMoves = Integer.toString(moves);
        String userMaxTile = Integer.toString(maxTile);
        try{
            // if there is no file founded in the path
            File leaderboardFile = new File(leaderboardPath);
            // create file
            leaderboardFile.createNewFile();
        }
        catch(IOException FileNotFoundException){

        }
        // set the append mode to true
        BufferedWriter output = new BufferedWriter(new FileWriter(leaderboardPath, true));
        // write user's data
        output.append(Login.username).append(" ").append(userMaxTile).append(" ").append(userMoves);
        // new line
        output.newLine();
        // close the buffered writer
        output.close();
    }
    // read_leaderboard: read the leaderboard for sorting
    // @param: none
    //@return: ArrayList<UserDatas>
    public static ArrayList<UserDatas> read_leaderboard() throws IOException {
        // initialize filereader
        FileReader fr = new FileReader(leaderboardPath);
        BufferedReader in = new BufferedReader(fr);
        // initialize ArrayList and variables
        ArrayList<UserDatas> userRank = new ArrayList<UserDatas>();
        String currentLine = in.readLine();
        int userMaxTile = 0;
        int userMove = 0;
        String name = " ";
        // if the line is not null
        while (currentLine != null)
        {
            // split the user info into three sections
            String[] UsrDetail = currentLine.split(" ");

            name = UsrDetail[0];

            userMaxTile = Integer.valueOf(UsrDetail[1]);

            userMove = Integer.valueOf(UsrDetail[2]);

            //Creating userDatas object for every user and adding it to ArrayList

            userRank.add(new UserDatas(name, userMaxTile,userMove));

            currentLine = in.readLine();
        }

        //Sorting ArrayList based on user moves and max tile
        Collections.sort(userRank, new MoveComparator());
        Collections.sort(userRank, new MaxTileComparator());
        fr.close();
        return userRank;
    }
    // write_leaderboard: write the sorted leaderboard to the file
    // @param: ArrayList<UserDatas>
    //@return: void
    public static void write_leaderboard(ArrayList<UserDatas> Leaderboard) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(leaderboardPath));

        for (UserDatas user : Leaderboard)
        {
            writer.write(user.name);

            writer.write(" "+user.maxTile);

            writer.write(" "+user.moves);

            writer.newLine();
        }
        writer.close();
    }
    // display_Leaderboard: display the sorted leaderboard
    // @param: none
    //@return:  void
    public void display_Leaderboard() throws IOException {
        try{
            // if there is no file then create file
            File leaderboardFile = new File(leaderboardPath);
            leaderboardFile.createNewFile();
        }catch(IOException FileNotFoundException){
        }
        // initialize file reader and variables
        FileReader fr = new FileReader(leaderboardPath);
        BufferedReader in = new BufferedReader(fr);
        String line = in.readLine();
        int userMaxTile;
        int userMove;
        String name = " ";
        int r = 0;
        // initialize 2D array of Jlabels
        JLabel[][] displayLabel = new JLabel[5][4];
        // when the line is not null and line count is less than 5;
        while(line!=null&&r<5){

            String[] UsrDetail = line.split(" ");

            name = UsrDetail[0];

            userMaxTile = Integer.valueOf(UsrDetail[1]);

            userMove = Integer.valueOf(UsrDetail[2]);

            for(int c = 0; c<4;){
                // initialize Jlabel
                displayLabel[r][c] = new JLabel();
                displayLabel[r][c].setHorizontalAlignment(SwingConstants.CENTER);
                displayLabel[r][c].setForeground(Color.WHITE);
                displayLabel[r][c].setFont(new Font("Open Sans",1,20));
                if(c==0){
                    if(r==0){
                        // set to gold text
                        displayLabel[r][0].setForeground(new Color(254,225,1));
                    }
                    else if (r==1){
                        // silver
                        displayLabel[r][0].setForeground(new Color(215,215,215));
                    }
                    else if (r==2){
                        // brown
                        displayLabel[r][0].setForeground(new Color(167,112,68));
                    }
                    // set rank number
                    displayLabel[r][0].setText(String.valueOf(r+1));
                }
                else if(c==1){
                    // set user name
                    displayLabel[r][1].setText(name);
                }
                else if(c==2){
                    // set user max tile
                    displayLabel[r][2].setText(String.valueOf(userMaxTile));
                }
                else if(c==3){
                    // set user move
                    displayLabel[r][3].setText(String.valueOf(userMove));
                }
                // add to display panel
                displayPanel.add(displayLabel[r][c]);
                c++;
            }
            line = in.readLine();
            r++;
        }
        // set grid layout
        displayPanel.setLayout(new GridLayout(r,4,5,5));
        in.close();
    }
}
