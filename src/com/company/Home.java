package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
// Home Class
// Purpose: Direct User to Different Pages and Display important information
public class Home {
    // initialize global variable
    // tokenCount of the user
    public static int tokenCount = 0;
    JFrame homeFrame;
    // call the initHome function
    public Home(){
        initHome();
    }
    // initHome: Create the home frame and add all the components to the home frame
    // @Param : none
    // @return: void
    private void initHome(){
        // initialize home frame
        homeFrame = new JFrame("Magic 2048 - Home");
        homeFrame.setSize(400,600);
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.setResizable(false);
        homeFrame.setLocationRelativeTo(null);

        // initialize home panel with gradient background
        JPanel homePanel = new JPanel(){
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

        // set layout to gridBag layout
        homePanel.setLayout(new GridBagLayout());
        //https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
        // initialize gridBag constraints
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,0,5, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        // set constraints for username label
        c.gridx = 0;
        c.gridy=0;

        // initialize JLabel displays username
        JLabel userName = new JLabel("User: "+Login.username);
        userName.setForeground(Color.WHITE);
        userName.setFont(new Font("Open Sans",1,22));
        homePanel.add(userName,c);

        // set constraints for token label
        c.gridx = 1;
        c.gridy=0;
        c.gridwidth = 2;
        // initialize JLabel displays user's token count
        JLabel tokenDisplay = new JLabel("Tokens: "+tokenCount);
        tokenDisplay.setForeground(Color.WHITE);
        tokenDisplay.setFont(new Font("Open Sans",1,22));
        homePanel.add(tokenDisplay,c);

        // set constraints for leaderboard button
        c.insets = new Insets(5,5,5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.ipady = 25;
        c.gridx = 0;
        c.gridy = 1;
        // initialize leaderboard button
        JButton leaderboard = new JButton ("Leaderboard");
        leaderboard.setFont(new Font("Open Sans",Font.PLAIN,20));
        leaderboard.setBackground(new Color(92,165,208));
        leaderboard.setOpaque(true);
        leaderboard.setBorderPainted(false);
        leaderboard.setForeground(Color.WHITE);
        leaderboard.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                // try and catch IOException from the leaderboard class
                try {
                    // call Leaderboard class
                    new Leaderboard();
                }
                catch (IOException ioException) {
                    // let the user know that the file's path is wrong
                    JOptionPane.showMessageDialog(homeFrame, "The Leaderboard File is not founded according to the Path", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                // dispose the home frame
                homeFrame.dispose();
            }
        });
        homePanel.add(leaderboard,c);

        // set constraints for shop button
        c.gridx = 1;
        c.gridy= 1;
        // initialize shop button
        JButton shopButton = new JButton ("Shop");
        shopButton.setFont(new Font("Open Sans",Font.PLAIN,20));
        shopButton.setBackground(new Color(44,187,195));
        shopButton.setOpaque(true);
        shopButton.setBorderPainted(false);
        shopButton.setForeground(Color.WHITE);
        shopButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //Call Shop class
                new Shop();
                // dispose the home frame
                homeFrame.dispose();
            }
        });
        homePanel.add(shopButton, c);

        // set constraints for help button
        c.gridx = 2;
        c.gridy= 1;
        // initialize help button
        JButton helpButton = new JButton ("?");
        helpButton.setFont(new Font("Open Sans",Font.PLAIN,20));
        helpButton.setBackground(new Color(255,114,111));
        helpButton.setOpaque(true);
        helpButton.setBorderPainted(false);
        helpButton.setForeground(Color.WHITE);
        helpButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //led to help webpage
                Desktop desk = Desktop.getDesktop();
                try {
                    // let the default browser open the URL
                    desk.browse(new URI("https://jerryzhang0920.github.io/Magic2048/"));
                    // let the user know that the help webpage is opened in his/her default browser
                    JOptionPane.showMessageDialog(homeFrame, "The Help webpage has been opened in your default browser, please check your default browser", "Notice", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                   // let the user know if there is an exception
                    JOptionPane.showMessageDialog(homeFrame, "There is an error opening the help webpage", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (URISyntaxException ex) {
                    // let the user know if there is an exception
                    JOptionPane.showMessageDialog(homeFrame, "There is an error opening the help webpage", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        homePanel.add(helpButton,c);

        // set the constraints of title label
        c.insets = new Insets(5,0,5, 0);
        c.ipady = 40;
        c.gridx = 0;
        c.gridy= 2;
        c.gridwidth = 3;
        // initialize title label
        JLabel title = new JLabel("Magic 2048");
        // aligned to the centre
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Open Sans",Font.BOLD,50));
        homePanel.add(title,c);

        // set the constraints of the play button
        c.insets = new Insets(5,0,5, 0);
        c.gridx = 0;
        c.gridy= 3;
        // initialize play button
        JButton playButton = new JButton ("Start Game");
        playButton.setFont(new Font("Open Sans",Font.PLAIN,35));
        playButton.setBackground(new Color(253,172,83));
        playButton.setOpaque(true);
        playButton.setBorderPainted(false);
        playButton.setForeground(Color.WHITE);
        playButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                // call game2048 class
                new Game2048();
                // dispose home frame
                homeFrame.dispose();
            }
        });
        homePanel.add(playButton,c);
        // add home panel to home frame
        homeFrame.add(homePanel);
        // set visible
        homeFrame.setVisible(true);
    }
}


