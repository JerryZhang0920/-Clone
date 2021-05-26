package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Login Class
// Purpose: Let the user Login with their username entered
public class Login {

    // initialize global variables
    JFrame loginFrame;
    // username used across classes
    public static String username;
    // call the initLogin function
    public Login(){
        initLogin();
    }

    // initLogin: Create the login frame and add all the components to the login frame
    // @Param : none
    // @return: void
    private void initLogin(){
        // initialize login frame
        loginFrame = new JFrame("Magic 2048 - Login");
        loginFrame.setSize(400,400);
        // set default close operation
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setResizable(false);
        // set to centre of the screen
        loginFrame.setLocationRelativeTo(null);

        // initialize loginPanel with gradient background
        //https://stackoverflow.com/questions/14364291/jpanel-gradient-background
        JPanel loginPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                java.awt.Color c01 = new java.awt.Color(10,207,254);
                java.awt.Color c02 = new java.awt.Color(73,90,255);
                GradientPaint gp = new GradientPaint(0, 0, c01, 0, getHeight(), c02);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        // set the layout to GridBagLayout and initialize its constraints
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // set gap between components
        c.insets = new Insets(10,5,5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        // set the location to 0,0 and width to 2 for the welcome label
        c.gridx = 0;
        c.gridy=0;
        c.gridwidth=2;

        // welcome label
        JLabel welcome = new JLabel("Welcome to Magic 2048!");
        // set font size
        welcome.setFont(new Font("Open Sans",1,30));
        // set text color
        welcome.setForeground(Color.WHITE);
        // add to loginPanel with GridBagLayout
        loginPanel.add(welcome,c);

        // set the location to 0,1 and gridwidth to 1 for the username label
        c.gridx = 0;
        c.gridwidth=1;
        // ipady sets the height of the component at least its min Height + ipady*2 pixels
        c.ipady = 25;
        c.gridy=1;
        JLabel userName = new JLabel("Username:");
        userName.setFont(new Font("Open Sans",1,22));
        userName.setForeground(Color.WHITE);
        loginPanel.add(userName, c);

        c.gridx = 1;
        c.gridy=1;
        // initialize JTextfield
        JTextField userEnter = new JTextField("Please enter your username here");
        userEnter.setFont(new Font("Open Sans",Font.PLAIN,15));
        // set its preferredSize
        userEnter.setPreferredSize(new Dimension(150,50));
        // set background
        userEnter.setBackground(new Color(42,148,255));
        userEnter.setOpaque(false);
        userEnter.setForeground(Color.WHITE);
        userEnter.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                // set username to the text input in the JTextfield
                username = userEnter.getText();
            }
        });
        loginPanel.add(userEnter, c);

        c.gridx = 0;
        c.gridy=2;
        c.gridwidth=2;
        // initialize login button
        JButton loginButton = new JButton("Login");
        // set its preferredSize
        loginButton.setPreferredSize(new Dimension(100,50));
        loginButton.setFont(new Font("Open Sans", Font.BOLD,22));
        // set its background and opacity
        loginButton.setBackground(new Color(253,172,83));
        // needed for MacOS
        // set borderPainted to false
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    // if the username is longer than 7C
                     if(username.length()>=7){
                         // throw new exception
                        Exception UserNameLengthException = new Exception();
                        throw UserNameLengthException;
                     }
                     else if (!username.equals(null)) {
                         // if the username is not empty, then go to home frame
                        new Home();
                        // dispose the login frame
                        loginFrame.dispose();
                     }
                     // if there is nullPointerException, the user did not put anything on the JTextfield.
                }
                catch (NullPointerException ex){
                    // ask user to put his/her username or press Enter after he/she entered the username
                    JOptionPane.showMessageDialog(loginFrame, "Error, please input your username or press ENTER after you entered your username", "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception UserNameLengthException){
                    // if the username is way too long, ask user to reenter its user name
                    JOptionPane.showMessageDialog(loginFrame, "Error, the length of the username cannot be longer than 7", "Error", JOptionPane.ERROR_MESSAGE);
                    // clear the JTextfield
                    userEnter.setText("");
                }
            }
        });
        // add the button to panel
        loginPanel.add(loginButton,c);

        // add panel to Login Frame
        loginFrame.add(loginPanel);
        // set frame visible
        loginFrame.setVisible(true);
    }
}

