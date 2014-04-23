/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package threes.game;

/**
 *
 * @author Legend
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class NextBlockPanel extends javax.swing.JPanel{
    private GameStatus status = null;
    public static final int MAX_WIDTH = 60;
    public static final int MAX_HEIGHT = 75;
    
    public NextBlockPanel() {
        initComponents();
    }
    
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, MAX_WIDTH, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, MAX_HEIGHT, Short.MAX_VALUE)
        );
    }
    
    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }
    
    public boolean isGameStarted(){
        return status != null;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintNextBlock(g2d);
    }

    private void paintNextBlock(Graphics2D g2d) {
        int blockWidth = MAX_WIDTH; 
        int blockHeight = MAX_HEIGHT; 
        Color bgColor = new Color(249,249,249);
        Font font = new Font("Comic Sans MS",Font.BOLD,36);
        setBackground(bgColor);
        if(isGameStarted()){
            int nextNumber = status.getNextBlock().getNumber();
            
            switch (nextNumber){
                case 1:
                    g2d.setColor(new Color(95,169,242));
                    g2d.fillRoundRect(0, 0, blockWidth-2, blockHeight-2, 15, 15);
                    g2d.setColor(new Color(102,203,255));
                    g2d.fillRoundRect(0, 0, blockWidth-2, blockHeight-12, 15, 15);
                    break;
                case 2:
                    g2d.setColor(new Color(204,82,123));
                    g2d.fillRoundRect(0, 0, blockWidth-2, blockHeight-2, 15, 15);
                    g2d.setColor(new Color(255,102,128));
                    g2d.fillRoundRect(0, 0, blockWidth-2, blockHeight-12, 15, 15);
                    break;
                default:
                    g2d.setColor(new Color(255,204,102));
                    g2d.fillRoundRect(0, 0, blockWidth-2, blockHeight-2, 15, 15);
                    g2d.setColor(new Color(254,255,255));
                    g2d.fillRoundRect(0, 0, blockWidth-2, blockHeight-12, 15, 15);
                    if(nextNumber > 3) {
                        g2d.setFont(font);
                        g2d.setColor(new Color(0,0,0));
                        g2d.drawString("+", blockWidth/2-12, blockHeight/2+5);
                    }
            }
            //g2d.fillRoundRect(10, 10, blockWidth-18, blockHeight-18, 10, 10);
        }
    }
}