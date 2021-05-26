package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

// Game2048 class
// Purpose: Includes the game screen and all necessary game functions
public class Game2048 {
    // initialize global variables
    // user move count
    public static int moveCount = 0;
    // max tile number
    public static int maxTile = 0;
    // magic item checked
    Boolean lightingChecked = false;
    Boolean bookDoubleChecked = false;
    Boolean controlZChecked = false;
    // grid panel
    JPanel gridPanel  = new JPanel();
    // stack to save grid after each move (for undo)
    Stack<int[]> savedGrids = new Stack<>();
    //store colors
    private static class Color {
        public Color(int fontColor, int backgroundColor) {
            foregroundColour = fontColor;//font color/foreground
            backgroundColour = backgroundColor;//background
        }
        public int foregroundColour; // foreground
        public int backgroundColour; // background
    }
    // initialize global components and variables
    // game frame
    JFrame gameFrame;
    // progress bar
    JProgressBar tileRoad;
    // tiles represented in JButtons
    JButton[] tiles;
    // display labels
    JLabel moveLabel = new JLabel();
    JLabel ligntningLabel;
    JLabel doubleLabel;
    JLabel undoLabel;
    // 1D array that store the value of each tile
    int[] gridData = new int[]{0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0};
    // temporary array use in move tile functions
    int[] temp = new int[4];
    // check if there is a merge in tiles
    int[] temp_merge = new int[16];
    // temporary list used when spawning new tiles and store empty tiles
    List emptyTiles = new ArrayList<Integer>(16);

    // hashmap use to store color corresponding to the tile value
    // color comes from Pantone Colors
    static HashMap<Integer, Color> colorMap = new HashMap<Integer, Color>() {{
        put(0, new Color(0xF1F2F1, 0xCECFD8));
        put(2, new Color(0xF1F2F1, 0x78838E));
        put(4, new Color(0xF1F2F1, 0x45B5AA));
        put(8, new Color(0xF1F2F1, 0x8f5c30));
        put(16, new Color(0xF1F2F1, 0xc800a1));
        put(32, new Color(0xF1F2F1, 0xffcd00));
        put(64, new Color(0xF1F2F1, 0x00b3de));
        put(128, new Color(0xF1F2F1, 0x007e3b));
        put(256, new Color(0xF1F2F1, 0xff7100));
        put(512, new Color(0xF1F2F1, 0xbad405));
        put(1024, new Color(0xF1F2F1, 0x002855));
        put(2048, new Color(0xF1F2F1, 0xef426f));
    }};

    // call functions to start the game
    public Game2048() {
        initGameFrame();
        initGame();
        update();
    }

    // initGame: randomly spawn 2 tiles of 2 and 1 tiles of 4 at the start of the game
    // @param: none
    //@return: void
    private void initGame() {
        for (int i = 0; i < 2; i++) {
            spawnTile(gridData, 2);
        }
        spawnTile(gridData, 4);
    }

    // randomTile: Randomly spawn tiles of 4 or 2
    // @param: int[]
    //@return: void
    private void randomTile(int[] grid) {
        int ran = (int) (Math.random() * 10);
        // there is a 50/50 chance of spawning a 2 or 4
        if (ran > 5) {
            spawnTile(grid, 4);
        } else {
            spawnTile(grid, 2);
        }
    }

    // spawnTile: Spawn new tiles in a random location with its value
    // @param: int[], int
    //@return: void
    private void spawnTile(int[] grid, int n) {
        emptyTiles.clear();
        // repopulate the emptyTiles list
        for (int i = 0; i < 16; i++) {
            if (grid[i] == 0) {
                emptyTiles.add(i);
            }
        }
        // if there is no empty tiles then no spawn
        int len = emptyTiles.size();
        if (len == 0) {
            return;
        }
        // calculate the place to spawn the tile
        int pos = (int) (Math.random() * 100) % len;
        grid[(int) emptyTiles.get(pos)] = n;
        update();
    }

    // gameJudge: judge the state of the game (win/lose) and process post-game functions
    // @param: int[]
    //@return: void
    private void gameJudge(int[] grid) throws IOException {
        // if user win
        if (checkWin(grid)) {
            // let the gamegrid lose focus
            gridPanel.setFocusable(false);
            // save max tiles and move count
            maxTile = getMax(gridData);
            Leaderboard.saveUserData(maxTile,moveCount);
            // sort leaderboard
            ArrayList<UserDatas> userRank = Leaderboard.read_leaderboard();
            Leaderboard.write_leaderboard(userRank);
            // give user token based on its progression
            Home.tokenCount += (tileRoad.getPercentComplete()*100);
            // let the user know
            JOptionPane.showMessageDialog(gameFrame, "Congratulations! You Have Won The Game!", "You Won!", JOptionPane.PLAIN_MESSAGE);
            // go to home screen and dispose game frame
            new Home();
            gameFrame.dispose();
        }
        // if user lose
        if (checkLose(grid)) {
            // let the game grid lose focus
            gridPanel.setFocusable(false);
            // save max tiles and move count
            maxTile = getMax(gridData);
            Leaderboard.saveUserData(maxTile,moveCount);
            // sort leaderboard
            ArrayList<UserDatas> userRank = Leaderboard.read_leaderboard();
            Leaderboard.write_leaderboard(userRank);
            // give user tokens based on its progressions
            Home.tokenCount += (tileRoad.getPercentComplete()*100);
            // let the user know
            JOptionPane.showMessageDialog(gameFrame, "Sorry, You Did Not Get The 2048 Tile, but your highest tile is: " + maxTile, "Game Over", JOptionPane.PLAIN_MESSAGE);
           // go to home screen and dispose game frame
            new Home();
            gameFrame.dispose();
        }
    }

    // checkWin: check if the user win the game. With a tile greater or equal than 2048 will win the game
    // @param: int[]
    //@return: boolean
    private boolean checkWin(int[] grid) {
        for (int i : grid) {
            if (i >= 2048) {
                return true;
            }
        }
        return false;
    }

    // checkLose: check if the game is over
    // @param: int[]
    //@return: boolean
    private boolean checkLose(int[] grid) {
        // check if moving in all directions cannot produce empty tiles, then return true means game over
        int[] tmp = new int[16];
        int isend = 0;

        System.arraycopy(grid, 0, tmp, 0, 16);
        left(tmp);
        if (isFull(tmp)) {
            isend++;
        }

        System.arraycopy(grid, 0, tmp, 0, 16);
        right(tmp);
        if (isFull(tmp)) {
            isend++;
        }

        System.arraycopy(grid, 0, tmp, 0, 16);
        up(tmp);
        if (isFull(tmp)) {
            isend++;
        }

        System.arraycopy(grid, 0, tmp, 0, 16);
        down(tmp);
        if (isFull(tmp)) {
            isend++;
        }

        if (isend == 4) {
            return true;
        } else {
            return false;
        }
    }

    // isFull: check if there is no empty tiles
    // @param: int[]
    //@return: boolean
    private boolean isFull(int[] grid) {
        // for each loop
        for (int i : grid) {
            if (i == 0) {
                return false;
            }
        }
        return true;
    }

    // getMax: get the value of the highest-value tile
    // @param: int[]
    //@return: int
    public static int getMax(int[] grid) {
        int max = grid[0];
        for (int i : grid) {
            if (i >= max) {
                max = i;
            }
        }
        return max;
    }

    // update: update the data each tile displays as well as values on each label.
    // @param: none
    //@return: void
    private void update() {
        // update labels and progress bar
        moveLabel.setText("Moves: "+String.valueOf(moveCount));
        ligntningLabel.setText("You Have: "+Shop.MagicItems[0]);
        doubleLabel.setText("You Have: "+Shop.MagicItems[1]);
        undoLabel.setText("You Have: "+Shop.MagicItems[2]);
        tileRoad.setValue(getMax(gridData));
        tileRoad.getPercentComplete();
        // update value displays on each tile
        JButton j;
        for (int i = 0; i < 16; i++) {
            int arr = gridData[i];
            j = tiles[i];
            if (arr == 0) {
                j.setText("");
            } else if (arr >= 1024) {
                j.setFont(new java.awt.Font("Open Sans", Font.PLAIN, 24));
                j.setText(String.valueOf(gridData[i]));
            } else {
                j.setFont(new java.awt.Font("Open Sans", Font.PLAIN, 30));
                j.setText(String.valueOf(arr));
            }
            // update colors corresponding to the value of each tile
            Color currentColor = colorMap.get(arr);
            j.setBackground(new java.awt.Color(currentColor.backgroundColour));
            j.setForeground(new java.awt.Color(currentColor.foregroundColour));
            // request focus on the grid panel
            gridPanel.requestFocusInWindow();
        }
    }
    // refreshSave: saves the grid after each move
    // @param: none
    //@return: void
    private void refreshSave(){
        saveGrid();
    }

    // initGameFrame: initialize the game frame and all the components on the game frame
    // @param: none
    //@return: void
    private void initGameFrame() {
        moveCount = 0;
        gameFrame = new JFrame("Magic 2048 - Game");
        gameFrame.setSize(500, 750);
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.getContentPane().setBackground(new java.awt.Color(0,114,181));

        JPanel gamePanel = new JPanel(){
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
        gamePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(5,0,5, 0);
        c.gridx = 0;
        c.gridy=0;
        JButton homeButton = new JButton();
        homeButton.setText("Home");
        homeButton.setBackground(new java.awt.Color(44,187,195));
        homeButton.setOpaque(true);
        homeButton.setBorderPainted(false);
        homeButton.setForeground(java.awt.Color.WHITE);
        homeButton.setFont(new java.awt.Font("Open Sans", Font.PLAIN, 35));
        homeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(!checkLose(gridData)){
                    String[] buttons = { "Continue", "Exit" };
                    int returnValue = JOptionPane.showOptionDialog(gameFrame, "Your game is not ended yet, do you wish to continue the game? \n If You Exit, your progress will not be saved to the Leaderboard.", "Warning",
                            JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[1]);
                    System.out.println(returnValue);
                    // https://stackoverflow.com/questions/8658576/joptionpane-with-multiple-buttons-on-each-line
                    if(returnValue == 1){
                        new Home();
                        gameFrame.dispose();
                    }
                }

            }
        });
        gamePanel.add(homeButton,c);

        c.gridx = 1;
        c.gridy=0;
        moveLabel.setText("Moves: "+String.valueOf(moveCount));
        moveLabel.setFont(new java.awt.Font("Open Sans", Font.PLAIN, 25));
        moveLabel.setForeground(java.awt.Color.WHITE);
        gamePanel.add(moveLabel,c);

        c.gridx = 0;
        c.gridy=1;
        c.gridwidth = 2;

        UIManager.put("ProgressBar.foreground", new java.awt.Color(253,172,83));
        UIManager.put("ProgressBar.background", new java.awt.Color(253,172,83));
        UIManager.put("ProgressBar.selectionBackground", java.awt.Color.black);
        UIManager.put("ProgressBar.selectionForeground", java.awt.Color.white);
        tileRoad = new JProgressBar();
        tileRoad.setValue(0);
        tileRoad.setStringPainted(true);
        tileRoad.setBorderPainted(false);
        tileRoad.setForeground(new java.awt.Color(253,172,83));
        tileRoad.setFont(new java.awt.Font("Open Sans", Font.PLAIN, 25));
        tileRoad.setMaximum(2048);
        gamePanel.add(tileRoad,c);
        c.gridx = 0;
        c.gridy=2;
        c.ipady = 25;
        c.gridwidth = 2;

        gridPanel.setLayout(new GridLayout(4, 4));

        gridPanel.setBackground(new java.awt.Color(179,182,194));
        gridPanel.setPreferredSize(new Dimension(425,425));
        gridPanel.setFocusable(true);
        gridPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

                System.arraycopy(gridData, 0, temp_merge, 0, 16);

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        up(gridData);
                        moveCount++;
                        refreshSave();
                        break;

                    case KeyEvent.VK_S:
                        down(gridData);
                        refreshSave();
                        moveCount++;
                        break;

                    case KeyEvent.VK_A:
                        left(gridData);
                        refreshSave();
                        moveCount++;
                        break;

                    case KeyEvent.VK_D:
                        right(gridData);
                        refreshSave();
                        moveCount++;
                        break;
                    case KeyEvent.VK_UP:
                        up(gridData);
                        moveCount++;
                        refreshSave();
                        break;

                    case KeyEvent.VK_DOWN:
                        down(gridData);
                        refreshSave();
                        moveCount++;
                        break;

                    case KeyEvent.VK_LEFT:
                        left(gridData);
                        refreshSave();
                        moveCount++;
                        break;

                    case KeyEvent.VK_RIGHT:
                        right(gridData);
                        refreshSave();
                        moveCount++;
                        break;

                }
                // if there is a merge after move, spawn new tiles
                if (!Arrays.equals(gridData, temp_merge)) {
                    randomTile(gridData);
                }

                update();
                try {
                    gameJudge(gridData);
                }
                catch (IOException ioException) {
                    JOptionPane.showMessageDialog(gameFrame, "An IOException has occurred, please check if the leaderboard file is in the correct path", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        });

        //set the default ui look and feel of the system
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(gameFrame, e.getMessage());
        }

        tiles = new JButton[16];
        JButton j;
        for (int i = 0; i < 16; i++) {
            tiles[i] = new JButton("0");
            j = tiles[i];
            j.setOpaque(true);
            // set the boarder and colors of borders
            j.setBorder(BorderFactory.createMatteBorder(6, 6, 6, 6, new java.awt.Color(179,182,194)));

            j.setFont(new java.awt.Font("Open Sans", 1, 52));
            j.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    if(lightingChecked){
                        for(int q=0;q<16;q++){
                            if(e.getSource()==tiles[q]){
                                // if the grid value is not null, strike the lightning
                                if(gridData[q]!=0){
                                    System.out.println(q);
                                    gridData[q]=0;
                                    lightingChecked=false;
                                    moveCount+=25;
                                    Shop.MagicItems[0]-=1;
                                    update();
                                }
                                else if(gridData[q]==0){
                                    lightingChecked=false;
                                    JOptionPane.showMessageDialog(gameFrame, "Warning! You cannot use Magic items on an empty tile", "Warning", JOptionPane.WARNING_MESSAGE);
                                    break;
                                }
                            }

                        }
                        gridPanel.requestFocusInWindow();
                    }
                    else if(bookDoubleChecked){
                        for(int q=0;q<16;q++){
                            if(e.getSource()==tiles[q]){
                                if(gridData[q]!=0){
                                    // if the grid value is not null, double the tile
                                    System.out.println(q);
                                    gridData[q]*=2;
                                    bookDoubleChecked=false;
                                    moveCount+=(gridData[q]/2);
                                    Shop.MagicItems[1]-=1;
                                    update();
                                }
                                else if(gridData[q]==0){
                                    bookDoubleChecked=false;
                                    JOptionPane.showMessageDialog(gameFrame, "Warning! You cannot use Magic items on an empty tile", "Warning", JOptionPane.WARNING_MESSAGE);
                                    break;
                                }
                            }

                        }
                        gridPanel.requestFocusInWindow();
                    }
                    else{
                        gridPanel.requestFocusInWindow();
                    }
                }
            });
            gridPanel.add(j);
        }
        gamePanel.add(gridPanel,c);
        c.gridx = 0;
        c.gridy=3;
        c.ipady = 0;
        JLabel magicLabel = new JLabel("Magic Items");
        magicLabel.setFont(new java.awt.Font("Open Sans", Font.PLAIN, 25));
        magicLabel.setForeground(java.awt.Color.WHITE);
        gamePanel.add(magicLabel, c);

        c.gridx = 0;
        c.gridy = 4;

        JPanel magicPanel = new JPanel(){
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
        magicPanel.setLayout(new GridLayout(3,3,5,5));
        JLabel lightning = new JLabel("The Lightning");
        lightning.setFont(new Font("Open Sans", Font.PLAIN, 17));
        lightning.setForeground(java.awt.Color.WHITE);
        lightning.setHorizontalAlignment(SwingConstants.CENTER);


        JButton lightningButton = new JButton("Strike");
        lightningButton.setFont(new Font("Open Sans", Font.PLAIN, 17));
        lightningButton.setForeground(new java.awt.Color(253,208,35));
        lightningButton.setBackground(new java.awt.Color(30,62,118));
        lightningButton.setOpaque(true);
        lightningButton.setBorderPainted(false);
        lightningButton.setHorizontalAlignment(SwingConstants.CENTER);
        lightningButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (Shop.MagicItems[0] >= 1) {
                    lightingChecked = true;
                }
                else{
                    JOptionPane.showMessageDialog(gameFrame, "Error, you do not have enough of this Magic item", "Error", JOptionPane.ERROR_MESSAGE);
                    gridPanel.requestFocusInWindow();
                }
            }
        });

        JLabel doubleInfo = new JLabel("Book Of Double");
        doubleInfo.setFont(new Font("Open Sans", Font.PLAIN, 17));
        doubleInfo.setForeground(java.awt.Color.WHITE);
        doubleInfo.setHorizontalAlignment(SwingConstants.CENTER);

        JButton doubleButton = new JButton("Use");
        doubleButton.setFont(new Font("Open Sans", Font.PLAIN, 17));
        doubleButton.setForeground(new java.awt.Color(253,208,35));
        doubleButton.setBackground(new java.awt.Color(30,62,118));
        doubleButton.setOpaque(true);
        doubleButton.setBorderPainted(false);
        doubleButton.setHorizontalAlignment(SwingConstants.CENTER);
        doubleButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (Shop.MagicItems[1] >= 1) {
                    bookDoubleChecked= true;
                }
                else{
                    JOptionPane.showMessageDialog(gameFrame, "Error, you do not have enough of this Magic item", "Error", JOptionPane.ERROR_MESSAGE);
                    gridPanel.requestFocusInWindow();
                }
            }
        });

        JLabel undoInfo = new JLabel("Control Z");
        undoInfo.setFont(new Font("Open Sans", Font.PLAIN, 17));
        undoInfo.setForeground(java.awt.Color.WHITE);
        undoInfo.setHorizontalAlignment(SwingConstants.CENTER);

        JButton undoButton = new JButton("Undo");
        undoButton.setHorizontalAlignment(SwingConstants.CENTER);
        undoButton.setFont(new Font("Open Sans", Font.PLAIN, 17));
        undoButton.setForeground(new java.awt.Color(253,208,35));
        undoButton.setBackground(new java.awt.Color(30,62,118));
        undoButton.setOpaque(true);
        undoButton.setBorderPainted(false);
        undoButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (Shop.MagicItems[2] >= 1) {
                    undo();
                    gridPanel.requestFocusInWindow();
                    update();
                }
                else{
                    JOptionPane.showMessageDialog(gameFrame, "Error, you do not have enough of this Magic item", "Error", JOptionPane.ERROR_MESSAGE);
                    gridPanel.requestFocusInWindow();
                }

            }
        });
        ligntningLabel = new JLabel ("You Have: "+Shop.MagicItems[0]);
        ligntningLabel.setFont(new java.awt.Font("Open Sans", Font.PLAIN, 17));
        ligntningLabel.setForeground(java.awt.Color.WHITE);
        ligntningLabel.setHorizontalAlignment(SwingConstants.CENTER);

        doubleLabel = new JLabel ("You Have: "+Shop.MagicItems[1]);
        doubleLabel.setFont(new java.awt.Font("Open Sans", Font.PLAIN, 17));
        doubleLabel.setForeground(java.awt.Color.WHITE);
        doubleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        undoLabel = new JLabel ("You Have: "+Shop.MagicItems[2]);
        undoLabel.setFont(new java.awt.Font("Open Sans", Font.PLAIN, 17));
        undoLabel.setForeground(java.awt.Color.WHITE);
        undoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        magicPanel.add(lightning);
        magicPanel.add(doubleInfo);
        magicPanel.add(undoInfo);
        magicPanel.add(ligntningLabel);
        magicPanel.add(doubleLabel);
        magicPanel.add(undoLabel);
        magicPanel.add(lightningButton);
        magicPanel.add(doubleButton);
        magicPanel.add(undoButton);
        gamePanel.add(magicPanel,c);
        gameFrame.add(gamePanel);
        gridPanel.requestFocusInWindow();
        gameFrame.setVisible(true);
    }
    // left: move the grid to the left
    // @param: int[]
    //@return: void
    private void left(int[] grid) {
        moveLeft(grid);
        mergeLeft(grid);
        // move left again due to the spaces created when merging
        moveLeft(grid);
    }

    // mergeLeft: merge the merge-able tiles to the left
    // @param: int[]
    //@return: void
    private void mergeLeft(int[] grid) {
        for (int l = 0; l < 4; l++) {
            for (int i = 0; i < 3; i++) {
                if ((grid[l * 4 + i] != 0 && grid[l * 4 + i + 1] != 0) && grid[l * 4 + i] == grid[l * 4 + i + 1]) {
                    //double the tile in the left
                    grid[l * 4 + i] *= 2;
                    // set the right tile to 0
                    grid[l * 4 + i + 1] = 0;
                }
            }
        }
    }

    // moveLeft: move the tiles to the left using temporary arrays
    // @param: int[]
    //@return: void
    private void moveLeft(int[] grid) {
        for (int l = 0; l < 4; l++) {

            int zero = 0, nZero = 0; // nZero stands for non zero
            for (int i = 0; i < 4; i++) {
                if (grid[l * 4 + i] == 0) {
                    zero++;
                } else {
                    temp[nZero] = grid[l * 4 + i];
                    nZero++;
                }
            }
            for (int i = nZero; i < 4; i++) {
                temp[i] = 0;
            }
            for (int j = 0; j < 4; j++) {
                grid[l * 4 + j] = temp[j];
            }
        }
    }
    // right: move the grid to the right
    // @param: int[]
    //@return: void
    private void right(int[] grid) {
        moveRight(grid);
        mergeRight(grid);
        moveRight(grid);
    }
    // mergeRight: merge the merge-able tiles to the right
    // @param: int[]
    //@return: void
    private void mergeRight(int[] grid) {
        for (int l = 0; l < 4; l++) {
            for (int i = 3; i > 0; i--) {
                if ((grid[l * 4 + i] != 0 && grid[l * 4 + i - 1] != 0) && grid[l * 4 + i] == grid[l * 4 + i - 1]) {
                    grid[l * 4 + i] *= 2;
                    grid[l * 4 + i - 1] = 0;
                }
            }
        }
    }
    // moveRight: move the tiles to the right using temporary array
    // @param: int[]
    //@return: void
    private void moveRight(int[] grid) {
        for (int l = 0; l < 4; l++) {

            int zero = 3, nZero = 3;
            for (int i = 3; i >= 0; i--) {
                if (grid[l * 4 + i] == 0) {
                    zero--;
                } else {
                    temp[nZero] = grid[l * 4 + i];
                    nZero--;
                }
            }
            for (int i = nZero; i >= 0; i--) {
                temp[i] = 0;
            }
            for (int j = 3; j >= 0; j--) {
                grid[l * 4 + j] = temp[j];
            }
        }
    }

    // up: move the grid upwards
    // @param: int[]
    //@return: void
    private void up(int[] grid) {
        moveUp(grid);
        mergeUp(grid);
        moveUp(grid);
    }

    // mergeUp: merge the merge-able tiles upward
    // @param: int[]
    //@return: void
    private void mergeUp(int[] grid) {
        for (int r = 0; r < 4; r++) {
            for (int i = 0; i < 3; i++) {
                if ((grid[r + 4 * i] != 0 && grid[r + 4 * (i + 1)] != 0) && grid[r + 4 * i] == grid[r + 4 * (i + 1)]) {
                    grid[r + 4 * i] *= 2;
                    grid[r + 4 * (i + 1)] = 0;
                }
            }
        }
    }

    // moveUp: move the tiles up
    // @param: int[]
    //@return: void
    private void moveUp(int[] grid) {
        for (int r = 0; r < 4; r++) {

            int zero = 0, nZero = 0;
            for (int i = 0; i < 4; i++) {
                if (grid[r + 4 * i] == 0) {
                    zero++;
                } else {
                    temp[nZero] = grid[r + 4 * i];
                    nZero++;
                }
            }
            for (int i = nZero; i < 4; i++) {
                temp[i] = 0;
            }
            for (int j = 0; j < 4; j++) {
                grid[r + 4 * j] = temp[j];
            }
        }
    }

    // down: move the grid down
    // @param: int[]
    //@return: void
    private void down(int[] grid) {
        moveDown(grid);
        mergeDown(grid);
        moveDown(grid);
    }

    // mergeDown: merge the merge-able tiles downward
    // @param: int[]
    //@return: void
    private void mergeDown(int[] grid) {
        for (int r = 0; r < 4; r++) {
            for (int i = 3; i > 0; i--) {
                if ((grid[r + 4 * i] != 0 && grid[r + 4 * (i - 1)] != 0) && grid[r + 4 * i] == grid[r + 4 * (i - 1)]) {
                    grid[r + 4 * i] *= 2;
                    grid[r + 4 * (i - 1)] = 0;
                }
            }
        }
    }

    // moveDown: move the tiles down
    // @param: int[]
    //@return: void
    private void moveDown(int[] grid) {
        for (int r = 0; r < 4; r++) {

            int zero = 3, nZero = 3;
            for (int i = 3; i >= 0; i--) {
                if (grid[r + 4 * i] == 0) {
                    zero--;
                } else {
                    temp[nZero] = grid[r + 4 * i];
                    nZero--;
                }
            }
            for (int i = nZero; i >= 0; i--) {
                temp[i] = 0;
            }
            for (int j = 3; j >= 0; j--) {
                grid[r + 4 * j] = temp[j];
            }
        }
    }

    // setGrid: set the grid values to the previous grid values
    // @param: int[]
    //@return: void
    private void setGrid(int[] grid){
        for(int i=0; i<16; i++){
            gridData[i]=grid[i];
        }
    }

    // saveGrid: save the current grid values into a stack
    // @param: none
    //@return: void
    private void saveGrid() {
       int[] temp_Save = new int[16];
       for(int i=0; i<16;i++){
           temp_Save[i] = gridData[i];
       }
       savedGrids.push(temp_Save);
    }

    // undo: set the current grid to the previous grid
    // @param: none
    //@return: void
    private void undo(){
        // idea from https://github.com/welchdante/2048/blob/master/src/com/company/game1024/NumberGame.java
        // if there is a previous saved grid
        if(savedGrids.size()>1){
            savedGrids.pop();
            int[] prevGrid = savedGrids.pop();

            setGrid(prevGrid);
            moveCount+=40;
            Shop.MagicItems[2]-=1;
        }
        else{
            JOptionPane.showMessageDialog(gameFrame, "Sorry, the game is unable to undo further", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
