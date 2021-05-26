package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

// shop class
// purpose: Let user purchase magic items using their tokens
public class Shop {
    // initialize magic items array
    public static int[] MagicItems = new int[3];
    // initialize global variables
    JFrame shopFrame;
    // call initshop function
    public Shop() {
        initShop();
    }
    JLabel tokenDisplay;
    JLabel item1Count;
    JLabel item2Count;
    JLabel item3Count;
    // initShop: Create the shop frame and add all the components to the shop frame
    // @Param : none
    // @return: void
        private void initShop(){
            // initialize shop frame
            shopFrame = new JFrame("Magic 2048 - Shop");
            shopFrame.setSize(480,600);
            shopFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            shopFrame.setResizable(false);
            shopFrame.setLocationRelativeTo(null);

            // initialize shop panel with gradient background
            JPanel shopPanel = new JPanel(){
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
            // set gridbag layout and constraints
            shopPanel.setLayout(new GridBagLayout());
            //https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
            GridBagConstraints c = new GridBagConstraints();

            // set constraints for username label
            c.insets = new Insets(5,10,5, 10);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy=0;
            c.weightx=1;

            // initialize username label
            JLabel userName = new JLabel("User: "+Login.username);
            userName.setPreferredSize(new Dimension(150,75));
            userName.setForeground(Color.WHITE);
            userName.setFont(new Font("Open Sans",1,22));
            shopPanel.add(userName,c);

            // set constraints for token label
            c.gridx = 2;
            c.gridy=0;
            // initialize token label
            tokenDisplay = new JLabel("Tokens: "+Home.tokenCount);
            tokenDisplay.setForeground(Color.WHITE);
            tokenDisplay.setFont(new Font("Open Sans",1,22));
            shopPanel.add(tokenDisplay,c);

            // set constraints for home button
            c.ipady = 40;
            c.gridx = 0;
            c.gridy= 1;
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
                    // call home class
                    new Home();
                    // dispose shop frame
                    shopFrame.dispose();
                }
            });
            shopPanel.add(homeButton,c);

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
                        JOptionPane.showMessageDialog(shopFrame, "The Help webpage has been opened in your default browser, please check your default browser", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        // let the user know if there is an exception
                        JOptionPane.showMessageDialog(shopFrame, "There is an error opening the help webpage", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (URISyntaxException ex) {
                        // let the user know if there is an exception
                        JOptionPane.showMessageDialog(shopFrame, "There is an error opening the help webpage", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            shopPanel.add(helpButton,c);

            // set constraints for magic items label
            c.insets = new Insets(5,0,5, 0);
            c.ipady = 40;
            c.gridx = 0;
            c.gridy= 2;
            c.gridwidth = 3;
            // initialize magic item label
            JLabel title = new JLabel("Magic Items");
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Open Sans",1,50));
            shopPanel.add(title,c);

            // set constraints and initialize item display panel
            c.gridx = 0;
            c.gridy= 3;
            JPanel itemDisplay = new JPanel();
            itemDisplay.setLayout(new GridLayout(1,3));

            // initialize item 1 panel with gradient background
            JPanel item1 = new JPanel(){
                @Override
                protected void paintComponent(Graphics graphics) {
                    super.paintComponent(graphics);
                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    java.awt.Color c01 = new java.awt.Color(48,207,208);
                    java.awt.Color c02 = new java.awt.Color(51,8,103);
                    GradientPaint gp = new GradientPaint(0, 0, c01, 0, getHeight(), c02);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            item1.setBackground(new Color(92,165,208));
            // set layout to box layout
            item1.setLayout(new BoxLayout(item1,BoxLayout.Y_AXIS));
            //https://examples.javacodegeeks.com/desktop-java/swing/java-swing-boxlayout-example/

            // initialize item 1 info label
            JLabel item1Info = new JLabel("The Lightning");
            item1Info.setFont(new Font("Open Sans",Font.PLAIN,20));
            item1Info.setForeground(Color.WHITE);
            item1Info.setAlignmentX(Component.CENTER_ALIGNMENT);
            item1.add(item1Info);
            // create vertical glue between components
            item1.add(Box.createVerticalGlue());

            // initialize item 1 count label
            item1Count = new JLabel("You have: "+MagicItems[0]);
            item1Count.setFont(new Font("Open Sans",Font.PLAIN,20));
            item1Count.setForeground(Color.WHITE);
            item1Count.setAlignmentX(Component.CENTER_ALIGNMENT);
            item1.add(item1Count);
            // create vertical glue between components
            item1.add(Box.createVerticalGlue());

            // initialize buy item1 button
            JButton buyItem1 = new JButton("10 Tokens");
            buyItem1.setFont(new Font("Open Sans",Font.PLAIN,20));
            buyItem1.setForeground(Color.WHITE);
            buyItem1.setBackground(new Color(44,187,195));
            buyItem1.setOpaque(true);
            buyItem1.setBorderPainted(false);
            buyItem1.setAlignmentX(Component.CENTER_ALIGNMENT);
            buyItem1.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    // if user have enough tokens
                   if(Home.tokenCount>=10){
                       // add one magic item
                       MagicItems[0]+=1;
                       // deduct tokens
                       Home.tokenCount-=10;
                       // update label displays
                       update();
                   }
                   else {
                       // let user know that he/she does not have enough tokens to buy this magic item
                       JOptionPane.showMessageDialog(shopFrame, "Error, you do not have enough tokens to purchase this magic item", "Error", JOptionPane.ERROR_MESSAGE);
                   }
                }
            });
            item1.add(buyItem1);

            // initialize item 2 panel with gradient background
            JPanel item2 = new JPanel(){
                @Override
                protected void paintComponent(Graphics graphics) {
                    super.paintComponent(graphics);
                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    java.awt.Color c01 = new java.awt.Color(48,207,208);
                    java.awt.Color c02 = new java.awt.Color(51,8,103);
                    GradientPaint gp = new GradientPaint(0, 0, c01, 0, getHeight(), c02);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            item2.setBackground(new Color(92,165,208));
            // set box layout
            item2.setLayout(new BoxLayout(item2,BoxLayout.Y_AXIS));
            //https://examples.javacodegeeks.com/desktop-java/swing/java-swing-boxlayout-example/

            // initialize item 2 info label
            JLabel item2Info = new JLabel("Book Of Double");
            item2Info.setFont(new Font("Open Sans",Font.PLAIN,20));
            item2Info.setForeground(Color.WHITE);
            item2Info.setAlignmentX(Component.CENTER_ALIGNMENT);
            item2.add(item2Info);
            item2.add(Box.createVerticalGlue());

            // initialize item 2 count label
            item2Count = new JLabel("You have: "+MagicItems[1]);
            item2Count.setFont(new Font("Open Sans",Font.PLAIN,20));
            item2Count.setForeground(Color.WHITE);
            item2Count.setAlignmentX(Component.CENTER_ALIGNMENT);
            item2.add(item2Count);
            item2.add(Box.createVerticalGlue());

            // initialize buy item 2 button
            JButton buyItem2 = new JButton("30 Tokens");
            buyItem2.setFont(new Font("Open Sans",Font.PLAIN,20));
            buyItem2.setForeground(Color.WHITE);
            buyItem2.setBackground(new Color(44,187,195));
            buyItem2.setAlignmentX(Component.CENTER_ALIGNMENT);
            buyItem2.setOpaque(true);
            buyItem2.setBorderPainted(false);
            buyItem2.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    // if user have enough token
                    if(Home.tokenCount>=30){
                        // add one magic item
                        MagicItems[1]+=1;
                        // deduct tokens
                        Home.tokenCount-=30;
                        // update label display
                        update();
                    }  else {
                        // let user know he/she does not have enough token to buy this magic item
                        JOptionPane.showMessageDialog(shopFrame, "Error, you do not have enough tokens to purchase this magic item", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            });
            item2.add(buyItem2);

            // initialize item 3 panel with gradient background
            JPanel item3 = new JPanel(){
                @Override
                protected void paintComponent(Graphics graphics) {
                    super.paintComponent(graphics);
                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    java.awt.Color c01 = new java.awt.Color(48,207,208);
                    java.awt.Color c02 = new java.awt.Color(51,8,103);
                    GradientPaint gp = new GradientPaint(0, 0, c01, 0, getHeight(), c02);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            item3.setBackground(new Color(92,165,208));
            item3.setLayout(new BoxLayout(item3,BoxLayout.Y_AXIS));
            //https://examples.javacodegeeks.com/desktop-java/swing/java-swing-boxlayout-example/

            // initialize item 3 info label
            JLabel item3Info = new JLabel("Control Z");
            item3Info.setFont(new Font("Open Sans",Font.PLAIN,20));
            item3Info.setForeground(Color.WHITE);
            item3Info.setAlignmentX(Component.CENTER_ALIGNMENT);
            item3.add(item3Info);
            item3.add(Box.createVerticalGlue());

            // initialize item 3 count label
            item3Count = new JLabel("You have: "+MagicItems[2]);
            item3Count.setFont(new Font("Open Sans",Font.PLAIN,20));
            item3Count.setForeground(Color.WHITE);
            item3Count.setAlignmentX(Component.CENTER_ALIGNMENT);
            item3.add(item3Count);
            item3.add(Box.createVerticalGlue());

            // initialize buy item 3 label
            JButton buyItem3 = new JButton("15 Tokens");
            buyItem3.setFont(new Font("Open Sans",Font.PLAIN,20));
            buyItem3.setForeground(Color.WHITE);
            buyItem3.setBackground(new Color(44,187,195));
            buyItem3.setAlignmentX(Component.CENTER_ALIGNMENT);
            buyItem3.setOpaque(true);
            buyItem3.setBorderPainted(false);
            buyItem3.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    // if user has enough tokens
                    if(Home.tokenCount>=15){
                        // add magic item
                        MagicItems[2]+=1;
                        // deduct token
                        Home.tokenCount-=15;
                        // update label display
                        update();
                    }
                    else {
                        JOptionPane.showMessageDialog(shopFrame, "Error, you do not have enough tokens to purchase this magic item", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            item3.add(buyItem3);

            // add items to item display
            itemDisplay.add(item1);
            itemDisplay.add(item2);
            itemDisplay.add(item3);
            // add to shop panel
            shopPanel.add(itemDisplay,c);
            // add to shop frame
            shopFrame.add(shopPanel);
            // set visible
            shopFrame.setVisible(true);
        }
    // update: update the item count label and token label when user purchased a magic item
    // @Param : none
    // @return: void
        private void update(){
            // update count labels and token labels
            tokenDisplay.setText("Tokens: "+Home.tokenCount);
            item1Count.setText("You have: "+MagicItems[0]);
            item2Count.setText("You have: "+MagicItems[1]);
            item3Count.setText("You have: "+MagicItems[2]);
        }
    }

