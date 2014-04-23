/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package threes.listener;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import threes.game.Direction;
import threes.game.GameBoardPanel;
import threes.game.NextBlockPanel;
import threes.main.ThreesFrame;
import threes.module.ThreesBlock;

/**
 *
 * @author bloodyparadise
 */
public class DragHandler {
    private boolean isPressed;
    private ThreesFrame mainFrame;
    private int sX;                                                             //start X point
    private int sY;                                                             //start Y point
    private int direction = Direction.NONE;
    private ThreesBlock[][] tempBlocks;
    
    public DragHandler(ThreesFrame mainFrame){
        this.mainFrame = mainFrame;
        this.isPressed = false;
        this.tempBlocks = new ThreesBlock[6][6];
    }
    
    private GameBoardPanel getGameBoard(){
        return mainFrame.getGameBoardPanel();
    }
    
    private NextBlockPanel getNextBlockPanel(){
        return mainFrame.getNextBlockPanel();
    }
    
    private JLabel getScorePanel(){
        return mainFrame.getScorePanel();
    }
    
    public void mousePress(MouseEvent e){
        if(getGameBoard().isGameStarted() && !getGameBoard().getStatus().isGameOver()){
            sX = e.getX();
            sY = e.getY();
            isPressed = true;
            for(int i=1;i<5;i++){
                for(int j=1;j<5;j++){
                    ThreesBlock blocks = getGameBoard().getStatus().getBlock(i, j);
                    blocks.setOriginX(blocks.getX());
                    blocks.setOriginY(blocks.getY());
                }
            }
        }
    }
    
    public void mouseRelease(MouseEvent e){
        if(getGameBoard().isGameStarted() && !getGameBoard().getStatus().isGameOver()){
            isPressed = false;
            if(direction != Direction.NONE){
                //blocks refresh
                getGameBoard().getStatus().setBlocks(tempBlocks);
                this.tempBlocks = new ThreesBlock[6][6];
                //next block refresh
                getGameBoard().getStatus().nextBlockEnter(direction);
                //reset status
                resetAllBlocksPosition();
                resetAllBlocksMovable();
                //reset direction
                direction = Direction.NONE;
            }else{
                resetAllBlocksPosition();
                resetAllBlocksMovable();
            }
            if(isOver()){
                getGameBoard().getStatus().setGameOver(true);
                int score = getGameBoard().getStatus().computeAllScore();
                getScorePanel().setText(""+score);
                // setHighScore(score);
            }
            getGameBoard().setPaintDirection(Direction.NONE);
            getGameBoard().repaint();
            getNextBlockPanel().repaint();
        }
    }

//    private void setHighScore(int score) {
//        String s = mainFrame.getHighscorePanel().getText();
//        int highscore = Integer.parseInt(s);
//        if(score > highscore){
//            mainFrame.getHighscorePanel().setText(""+score);
//            File highscoreFile = new File("highscore");
//            OutputStream out;
//            try {
//                out = new FileOutputStream(highscoreFile); //如果文件不存在会自动创建
//                String str=""+score;
//                byte[] b=str.getBytes();
//                out.write(b);//因为是字节流，所以要转化成字节数组进行输出
//                out.close();
//            } catch (FileNotFoundException ex) {
//                Logger.getLogger(ThreesFrame.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(ThreesFrame.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
    
    private boolean isOver(){
        copyToTempBlocks();
        if(isBlocksMovable(Direction.NORTH)){
            resetAllTempBlocks();
            return false;
        }else if(isBlocksMovable(Direction.SOUTH)){
            resetAllTempBlocks();
            return false;
        }else if(isBlocksMovable(Direction.WEST)){
            resetAllTempBlocks();
            return false;
        }else if(isBlocksMovable(Direction.EAST)){
            resetAllTempBlocks();
            return false;
        }
        resetAllTempBlocks();
        return true;
    }
    
    private void resetAllBlocksPosition(){
        for(int i=1;i<5;i++){
            for(int j=1;j<5;j++){
                ThreesBlock blocks = getGameBoard().getStatus().getBlock(i, j);
                blocks.setX(blocks.getOriginX());
                blocks.setY(blocks.getOriginY());
            }
        }
    }
    
    private void resetAllBlocksMovable(){
        for(int i=1;i<5;i++){
            for(int j=1;j<5;j++){
                ThreesBlock blocks = getGameBoard().getStatus().getBlock(i, j);
                blocks.setMovable(false);
            }
        }
    }
    
    private void resetAllTempBlocks(){
        this.tempBlocks = new ThreesBlock[6][6];
    }
    
    private void copyToTempBlocks(){
        for(int i = 0 ; i < 6 ; i++){
            for(int j = 0 ; j < 6 ; j++){
                ThreesBlock oldBlock = getGameBoard().getStatus().getBlock(i, j);
                this.tempBlocks[i][j] = new ThreesBlock(oldBlock.getNumber());
                this.tempBlocks[i][j].setOriginX(oldBlock.getOriginX());
                this.tempBlocks[i][j].setOriginY(oldBlock.getOriginY());
                this.tempBlocks[i][j].setX(oldBlock.getX());
                this.tempBlocks[i][j].setY(oldBlock.getY());
            }
        }
    }
    
    private int judgeDirection(MouseEvent e){
        int nowX = e.getX();
        int nowY = e.getY();
        int moveX = nowX - sX;
        int moveY = nowY - sY;
        int absX = Math.abs(moveX);
        int absY = Math.abs(moveY);
        
        int thresholdX = GameBoardPanel.MAX_WIDTH/8;
        int thresholdY = GameBoardPanel.MAX_WIDTH/8;
        if(absX > thresholdX && absY <= thresholdY){
            return moveX > 0 ? Direction.EAST : Direction.WEST;
        }
        if(absY > thresholdY && absX <= thresholdX){
            return moveY > 0 ? Direction.SOUTH : Direction.NORTH;
        }
        return Direction.NONE;
    }
    
    public void mouseDragged(MouseEvent e){
        if(getGameBoard().isGameStarted() && !getGameBoard().getStatus().isGameOver()){
            if(isPressed){
                int newDirection = judgeDirection(e);
                if(newDirection == Direction.NONE){
                    direction = Direction.NONE;
                    resetAllBlocksPosition();
                    resetAllBlocksMovable();
                    this.tempBlocks = new ThreesBlock[6][6];
                }else{
                    boolean isMovable = false;
                    if(direction == Direction.NONE){
                        //first move in some direction
                        copyToTempBlocks();
                        if(isBlocksMovable(newDirection)){
                            direction = newDirection;
                            computeTempBlocks();
                            isMovable = true;
                        }
                    }else if(direction == newDirection){
                        isMovable = true;
                    }
                    if(isMovable){
                        int nowX = e.getX();
                        int nowY = e.getY();
                        int moveX = 0;
                        int moveY = 0;
                        if(direction == Direction.EAST || direction == Direction.WEST){
                            //move east or west, only change x
                            moveX = nowX - sX;
                        }else if(direction == Direction.NORTH || direction == Direction.SOUTH){
                            //move south or north, only change y
                            moveY = nowY - sY;
                        }
                        moveBlocks(moveX,moveY,direction);
                        
                    }
                }
                getGameBoard().setPaintDirection(newDirection);
                getGameBoard().repaint();
            }
        }
    }
    
    private boolean isBlocksMovable(int direction){
        boolean isMovable = true;
        switch(direction){
            case Direction.NORTH:
                isMovable = isBlocksMovableNorth();
                break;
            case Direction.SOUTH:
                isMovable = isBlocksMovableSouth();
                break;
            case Direction.WEST:
                isMovable = isBlocksMovableWest();
                break;
            case Direction.EAST:
                isMovable = isBlocksMovableEast();
                break;
            default:
                
        }
        return isMovable;
    }
    
    private boolean isBlocksMovableNorth(){
        for(int j = 1 ; j < 5 ; j++){
            for(int i = 1 ; i < 5 ; i++){
                ThreesBlock block = tempBlocks[i][j];
                if(block.getNumber()!=0){
                    ThreesBlock collisionBlock = tempBlocks[i][j-1];
                    if(collisionBlock.getNumber()==0){
                        return true;
                    }else if((block.getNumber()==1 && collisionBlock.getNumber()==2) ||
                            (block.getNumber()==2 && collisionBlock.getNumber()==1)){
                        return true;
                    }else if(block.getNumber()==collisionBlock.getNumber() 
                            && block.getNumber()!=-1 && block.getNumber()!=1 && block.getNumber()!=2){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isBlocksMovableSouth(){
        for(int j = 4 ; j > 0 ; j--){
            for(int i = 1 ; i < 5 ; i++){
                ThreesBlock block = tempBlocks[i][j];
                if(block.getNumber()!=0){
                    ThreesBlock collisionBlock = tempBlocks[i][j+1];
                    if(collisionBlock.getNumber()==0){
                        return true;
                    }else if((block.getNumber()==1 && collisionBlock.getNumber()==2) ||
                            (block.getNumber()==2 && collisionBlock.getNumber()==1)){
                        return true;
                    }else if(block.getNumber()==collisionBlock.getNumber() 
                            && block.getNumber()!=-1 && block.getNumber()!=1 && block.getNumber()!=2){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isBlocksMovableWest(){
        for(int i = 1 ; i < 5 ; i++){
            for(int j = 1 ; j < 5 ; j++){
                ThreesBlock block = tempBlocks[i][j];
                if(block.getNumber()!=0){
                    ThreesBlock collisionBlock = tempBlocks[i-1][j];
                    if(collisionBlock.getNumber()==0){
                        return true;
                    }else if((block.getNumber()==1 && collisionBlock.getNumber()==2) ||
                            (block.getNumber()==2 && collisionBlock.getNumber()==1)){
                        return true;
                    }else if(block.getNumber()==collisionBlock.getNumber() 
                            && block.getNumber()!=-1 && block.getNumber()!=1 && block.getNumber()!=2){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isBlocksMovableEast(){
        for(int i = 4 ; i > 0 ; i--){
            for(int j = 1 ; j < 5 ; j++){
                ThreesBlock block = tempBlocks[i][j];
                if(block.getNumber()!=0){
                    ThreesBlock collisionBlock = tempBlocks[i+1][j];
                    if(collisionBlock.getNumber()==0){
                        return true;
                    }else if((block.getNumber()==1 && collisionBlock.getNumber()==2) ||
                            (block.getNumber()==2 && collisionBlock.getNumber()==1)){
                        return true;
                    }else if(block.getNumber()==collisionBlock.getNumber() 
                            && block.getNumber()!=-1 && block.getNumber()!=1 && block.getNumber()!=2){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void computeTempBlocks() {
        switch(direction){
            case Direction.NORTH:
                computeTempBlocksNorth();
                break;
            case Direction.SOUTH:
                computeTempBlocksSouth();
                break;
            case Direction.WEST:
                computeTempBlocksWest();
                break;
            case Direction.EAST:
                computeTempBlocksEast();
                break;
            default:
                
        }
    }
    
    private void computeTempBlocksNorth(){
        for(int j = 1 ; j < 5 ; j++){
            for(int i = 1 ; i < 5 ; i++){
                ThreesBlock block = tempBlocks[i][j];
                if(block.getNumber()!=0){
                    ThreesBlock collisionBlock = tempBlocks[i][j-1];
                    ThreesBlock originBlock = getGameBoard().getStatus().getBlock(i, j);
                    if(collisionBlock.getNumber()==0){
                        tempBlocks[i][j-1].setNumber(tempBlocks[i][j].getNumber());
                        tempBlocks[i][j].setNumber(0);
                        originBlock.setMovable(true);
                    }else if((block.getNumber()==1 && collisionBlock.getNumber()==2) ||
                            (block.getNumber()==2 && collisionBlock.getNumber()==1)){
                        tempBlocks[i][j-1].setNumber(3);
                        tempBlocks[i][j].setNumber(0);
                        originBlock.setMovable(true);
                    }else if(block.getNumber()==collisionBlock.getNumber() 
                            && block.getNumber()!=-1 && block.getNumber()!=1 && block.getNumber()!=2){
                        tempBlocks[i][j-1].setNumber(block.getNumber()*2);
                        tempBlocks[i][j].setNumber(0);
                        originBlock.setMovable(true);
                    }
                }
            }
        }
    }
    
    private void computeTempBlocksSouth(){
        for(int j = 4 ; j > 0 ; j--){
            for(int i = 1 ; i < 5 ; i++){
                ThreesBlock block = tempBlocks[i][j];
                if(block.getNumber()!=0){
                    ThreesBlock collisionBlock = tempBlocks[i][j+1];
                    ThreesBlock originBlock = getGameBoard().getStatus().getBlock(i, j);
                    if(collisionBlock.getNumber()==0){
                        tempBlocks[i][j+1].setNumber(tempBlocks[i][j].getNumber());
                        tempBlocks[i][j].setNumber(0);
                        originBlock.setMovable(true);
                    }else if((block.getNumber()==1 && collisionBlock.getNumber()==2) ||
                            (block.getNumber()==2 && collisionBlock.getNumber()==1)){
                        tempBlocks[i][j+1].setNumber(3);
                        tempBlocks[i][j].setNumber(0);
                        originBlock.setMovable(true);
                    }else if(block.getNumber()==collisionBlock.getNumber() 
                            && block.getNumber()!=-1 && block.getNumber()!=1 && block.getNumber()!=2){
                        tempBlocks[i][j+1].setNumber(block.getNumber()*2);
                        tempBlocks[i][j].setNumber(0);
                        originBlock.setMovable(true);
                    }
                }
            }
        }
    }
    
    private void computeTempBlocksWest(){
        for(int i = 1 ; i < 5 ; i++){
            for(int j = 1 ; j < 5 ; j++){
                ThreesBlock block = tempBlocks[i][j];
                if(block.getNumber()!=0){
                    ThreesBlock collisionBlock = tempBlocks[i-1][j];
                    ThreesBlock originBlock = getGameBoard().getStatus().getBlock(i, j);
                    if(collisionBlock.getNumber()==0){
                        tempBlocks[i-1][j].setNumber(tempBlocks[i][j].getNumber());
                        tempBlocks[i][j].setNumber(0);
                        originBlock.setMovable(true);
                    }else if((block.getNumber()==1 && collisionBlock.getNumber()==2) ||
                            (block.getNumber()==2 && collisionBlock.getNumber()==1)){
                        tempBlocks[i-1][j].setNumber(3);
                        tempBlocks[i][j].setNumber(0);
                        originBlock.setMovable(true);
                    }else if(block.getNumber()==collisionBlock.getNumber() 
                            && block.getNumber()!=-1 && block.getNumber()!=1 && block.getNumber()!=2){
                        tempBlocks[i-1][j].setNumber(block.getNumber()*2);
                        tempBlocks[i][j].setNumber(0);
                        originBlock.setMovable(true);
                    }
                }
            }
        }
    }
    
    private void computeTempBlocksEast(){
        for(int i = 4 ; i > 0 ; i--){
            for(int j = 1 ; j < 5 ; j++){
                ThreesBlock block = tempBlocks[i][j];
                if(block.getNumber()!=0){
                    ThreesBlock collisionBlock = tempBlocks[i+1][j];
                    ThreesBlock originBlock = getGameBoard().getStatus().getBlock(i, j);
                    if(collisionBlock.getNumber()==0){
                        tempBlocks[i+1][j].setNumber(tempBlocks[i][j].getNumber());
                        tempBlocks[i][j].setNumber(0);
                        originBlock.setMovable(true);
                    }else if((block.getNumber()==1 && collisionBlock.getNumber()==2) ||
                            (block.getNumber()==2 && collisionBlock.getNumber()==1)){
                        tempBlocks[i+1][j].setNumber(3);
                        tempBlocks[i][j].setNumber(0);
                        originBlock.setMovable(true);
                    }else if(block.getNumber()==collisionBlock.getNumber() 
                            && block.getNumber()!=-1 && block.getNumber()!=1 && block.getNumber()!=2){
                        tempBlocks[i+1][j].setNumber(block.getNumber()*2);
                        tempBlocks[i][j].setNumber(0);
                        originBlock.setMovable(true);
                    }
                }
            }
        }
    }

    private void moveBlocks(int moveX, int moveY, int direction) {
        if(direction!=Direction.NONE){
            //set index X and Y
            switch(direction){
                case Direction.NORTH:
                    moveNorth(moveX, moveY);
                    break;
                case Direction.SOUTH:
                    moveSouth(moveX, moveY);
                    break;
                case Direction.WEST:
                    moveWest(moveX, moveY);
                    break;
                case Direction.EAST:
                    moveEast(moveX, moveY);
                    break;
                default:
//                    copyToTempBlocks();
                    break;
            }
        }
    }
    
    private void moveOneBlock(ThreesBlock block, int moveX, int moveY){
        int newX = block.getOriginX() + moveX;
        int newY = block.getOriginY() + moveY;
        block.setX(newX);
        block.setY(newY);
    }

    private void moveNorth(int moveX, int moveY) {
        int blockWidth = GameBoardPanel.MAX_WIDTH/4;
        int blockHeight = GameBoardPanel.MAX_HEIGHT/4;
        if(moveY <= -blockHeight){
            moveY = -blockHeight;
        }
        for(int j = 1 ; j < 5 ; j++){
            for(int i = 1 ; i < 5 ; i++){
                ThreesBlock block = getGameBoard().getStatus().getBlock(i, j);
                if(block.isMovable()){
                    moveOneBlock(getGameBoard().getStatus().getBlock(i, j), moveX, moveY);
                }
            }
        }
    }
    
    private void moveSouth(int moveX, int moveY) {
        int blockWidth = GameBoardPanel.MAX_WIDTH/4;
        int blockHeight = GameBoardPanel.MAX_HEIGHT/4;
        if(moveY >= blockHeight){
            moveY = blockHeight;
        }
        for(int j = 4 ; j > 0 ; j--){
            for(int i = 1 ; i < 5 ; i++){
                ThreesBlock block = getGameBoard().getStatus().getBlock(i, j);
                if(block.isMovable()){
                    moveOneBlock(getGameBoard().getStatus().getBlock(i, j), moveX, moveY);
                }
            }
        }
    }
    
    private void moveWest(int moveX, int moveY) {
        int blockWidth = GameBoardPanel.MAX_WIDTH/4;
        int blockHeight = GameBoardPanel.MAX_HEIGHT/4;
        if(moveX <= -blockWidth){
            moveX = -blockWidth;
        }
        for(int i = 1 ; i < 5 ; i++){
            for(int j = 1 ; j < 5 ; j++){
                ThreesBlock block = getGameBoard().getStatus().getBlock(i, j);
                if(block.isMovable()){
                    moveOneBlock(getGameBoard().getStatus().getBlock(i, j), moveX, moveY);
                }
            }
        }
    }
    
    private void moveEast(int moveX, int moveY) {
        int blockWidth = GameBoardPanel.MAX_WIDTH/4;
        int blockHeight = GameBoardPanel.MAX_HEIGHT/4;
        if(moveX >= blockWidth){
            moveX = blockWidth;
        }
        for(int i = 4 ; i > 0 ; i--){
            for(int j = 1 ; j < 5 ; j++){
                ThreesBlock block = getGameBoard().getStatus().getBlock(i, j);
                if(block.isMovable()){
                    moveOneBlock(getGameBoard().getStatus().getBlock(i, j), moveX, moveY);
                }
            }
        }
    }
    
    private void printBlocks(ThreesBlock[][] blocks){
        for(int j = 1 ; j < 5 ; j++){
            for(int i = 1 ; i < 5 ; i++){
                ThreesBlock block = blocks[i][j];
                System.out.print(block.getY()+"\t");
            }
            System.out.println();
        }
        System.out.println();
    }
}