/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package threes.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import threes.module.ThreesBlock;

/**
 *
 * @author Legend
 */
public class GameBoardPanel extends javax.swing.JPanel{
    private GameStatus status = null;
    public static final int MAX_WIDTH = 400;
    public static final int MAX_HEIGHT = 600;
    private int paintDirection = Direction.NONE;
    /**
     * Creates new form GameBoardPanel
     */
    public GameBoardPanel() {
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

    public int getPaintDirection() {
        return paintDirection;
    }

    public void setPaintDirection(int drawDirection) {
        this.paintDirection = drawDirection;
    }

    public boolean isGameStarted(){
        return status != null;
    }
     
    private void paintGameStatus(Graphics2D g2d) {
        Color bgColor = new Color(187,217,217);
        //g2d.setColor(bgColor);
//        setLayout(null);
//        setVisible(true);
//        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        setBackground(bgColor);
        if(isGameStarted()){
            paintMainBoard(g2d);
        }
    }
    
    private void paintMainBoard(Graphics2D g2d){
        switch(paintDirection){
            case Direction.NORTH:
                for(int j=1;j<5;j++){
                    for(int i=1;i<5;i++){
                    ThreesBlock block = status.getBlock(i, j);
                    paintBlock(g2d, block);
                    }
                }
                break;
            case Direction.SOUTH:
                for(int j=4;j>0;j--){
                    for(int i=1;i<5;i++){
                    ThreesBlock block = status.getBlock(i, j);
                    paintBlock(g2d, block);
                    }
                }
                break;
            case Direction.WEST:
                for(int i=1;i<5;i++){
                    for(int j=1;j<5;j++){
                    ThreesBlock block = status.getBlock(i, j);
                    paintBlock(g2d, block);
                    }
                }
                break;
            case Direction.EAST:
                for(int i=4;i>0;i--){
                    for(int j=1;j<5;j++){
                    ThreesBlock block = status.getBlock(i, j);
                    paintBlock(g2d, block);
                    }
                }
                break;
            default:
                for(int j=1;j<5;j++){
                    for(int i=1;i<5;i++){
                    ThreesBlock block = status.getBlock(i, j);
                    paintBlock(g2d, block);
                    }
                }
        }
    }

    public void drawString(Graphics g, String str, int xPos, int yPos) {
            int strWidth = g.getFontMetrics().stringWidth(str);
            g.drawString(str, xPos + strWidth / 2, yPos);
    }


    
    private void paintBlock(Graphics2D g2d, ThreesBlock block){
        int blockWidth = MAX_WIDTH/4; 
        int blockHeight = MAX_HEIGHT/4; 
//        Image redTwoImage = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("E:\\javaProject\\Threes\\src\\threes\\2.png"));
//        Image blueOneImage = new ImageIcon("E:\\javaProject\\Threes\\src\\threes\\1.jpg").getImage();
//        Image blankBlockImage = new ImageIcon(".\\blank.png").getImage();
                
        //draw backgroud
        if(block.getNumber()==0){
            g2d.setColor(new Color(255,255,255));
        }else if(block.getNumber()==1){
            g2d.setColor(new Color(95,169,242));
            g2d.fillRoundRect(block.getX(), block.getY(), blockWidth-2, blockHeight-2, 20, 20);
            g2d.setColor(new Color(102,203,255));
            g2d.fillRoundRect(block.getX(), block.getY(), blockWidth-2, blockHeight-12, 20, 20);
            //g2d.drawImage(blueOneImage, 0, 0, 100, 150, null);
        }else if(block.getNumber()==2){
            g2d.setColor(new Color(204,82,123));
            g2d.fillRoundRect(block.getX(), block.getY(), blockWidth-2, blockHeight-2, 20, 20);
            g2d.setColor(new Color(255,102,128));
            g2d.fillRoundRect(block.getX(), block.getY(), blockWidth-2, blockHeight-12, 20, 20);
            //g2d.drawImage(redTwoImage, 0, 0, null);
        }else{
            g2d.setColor(new Color(255,204,102));
            g2d.fillRoundRect(block.getX(), block.getY(), blockWidth-2, blockHeight-2, 20, 20);
            g2d.setColor(new Color(254,255,255));
            g2d.fillRoundRect(block.getX(), block.getY(), blockWidth-2, blockHeight-12, 20, 20);
        }
//        if(block.getNumber()!=0){
//            g2d.fillRoundRect(block.getX(), block.getY(), blockWidth-2, blockHeight-2, 10, 10);
//        }
        
        //draw border
//        if(block.getNumber()!=0){
//            g2d.setColor(new Color(0,0,0));
//            g2d.drawRoundRect(block.getX(), block.getY(), blockWidth-2, blockHeight-2, 10, 10);
//        }
        
        //draw number
        if(block.getNumber()==0){
            g2d.setColor(new Color(255,255,255));
        }else if(block.getNumber()==1){
            g2d.setColor(new Color(255,255,255));
        }else if(block.getNumber()==2){
            g2d.setColor(new Color(255,255,255));
        }else{
            g2d.setColor(new Color(0,0,0));
        }
        Font font = new Font("Comic Sans MS",Font.BOLD,36);
        if(block.getNumber()!=0){
            g2d.setFont(font);
            String num = ""+block.getNumber();
            int numWidth = g2d.getFontMetrics().stringWidth(num);
            g2d.drawString(num, block.getX()+blockWidth/2-numWidth/2, block.getY()+blockHeight/2+5); // 居中对齐文本绘制公式
        }
        
        //game over, draw scores
        if(status.isGameOver()){
            int score = block.computeScore();
            if(score > 0){
                g2d.setColor(new Color(255,102,128));
                font = new Font("Comic Sans MS",Font.BOLD,15);
                g2d.setFont(font);
                String blockScore = "+"+score;
                int scoreWidth = g2d.getFontMetrics().stringWidth(blockScore);
                g2d.drawString(blockScore, block.getX()+blockWidth/2-scoreWidth/2, block.getY()+blockHeight/2+25);
            }
        }
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintGameStatus(g2d);
    }
}
