/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package threes.game;

import java.util.LinkedList;
import java.util.Random;
// import threes.main.GameBoardPanel;
import threes.module.ThreesBlock;
/**
 *
 * @author Legend
 */
public class GameStatus {
    private Random ran = new Random();
    private ThreesBlock[][] blocks;
    private ThreesBlock nextBlock;
    private boolean gameOver;
    
    public GameStatus(){
        gameOver = false;
        //next block
        ThreesBlock[][] tempblocks = createNewGameBoard();
        setBlocks(tempblocks);
        generateNextBlock();
    }
    
    private ThreesBlock[][] createNewGameBoard(){
        ThreesBlock[][] tempblocks = new ThreesBlock[6][6];
        
        int blockWidth = GameBoardPanel.MAX_WIDTH/4;
        int blockHeight = GameBoardPanel.MAX_HEIGHT/4;
        //border blocks, invisible
        for(int i = 0 ; i < 6 ; i++){
            tempblocks[i][0] = new ThreesBlock(-1);
            tempblocks[i][0].setX((i-1)*blockWidth);
            tempblocks[i][0].setY((0-1)*blockHeight);
            tempblocks[i][5] = new ThreesBlock(-1);
            tempblocks[i][5].setX((i-1)*blockWidth);
            tempblocks[i][5].setY((5-1)*blockHeight);
        }
        
        for(int j = 0 ; j < 6 ; j++){
            tempblocks[0][j] = new ThreesBlock(-1);
            tempblocks[0][j].setX((0-1)*blockWidth);
            tempblocks[0][j].setY((j-1)*blockHeight);
            tempblocks[5][j] = new ThreesBlock(-1);
            tempblocks[5][j].setX((5-1)*blockWidth);
            tempblocks[5][j].setY((j-1)*blockHeight);
        }
        //initialize visible blocks, 3 blue, 3 red, 3 white
        for(int i = 1 ; i < 5 ; i++){
            for(int j = 1 ; j < 5 ; j++){
                tempblocks[i][j] = new ThreesBlock(0);
                tempblocks[i][j].setX((i-1)*blockWidth);
                tempblocks[i][j].setY((j-1)*blockHeight);
            }
        }
        
        //randomize
        int count = 3;
        while(count>0){
            int i = 1+ran.nextInt(4);
            int j = 1+ran.nextInt(4);
            if(tempblocks[i][j].getNumber()==0){
                tempblocks[i][j].setNumber(1);
                count--;
            }
        }
        
        count = 3;
        while(count>0){
            int i = 1+ran.nextInt(4);
            int j = 1+ran.nextInt(4);
            if(tempblocks[i][j].getNumber()==0){
                tempblocks[i][j].setNumber(2);
                count--;
            }
        }
        
        count = 3;
        while(count>0){
            int i = 1+ran.nextInt(4);
            int j = 1+ran.nextInt(4);
            if(tempblocks[i][j].getNumber()==0){
                tempblocks[i][j].setNumber(3);
                count--;
            }
        }
        
        return tempblocks;
    }
    
    public void generateNextBlock(){
        int redCount = 0;
        int blueCount = 0;
        int whiteCount = 0;
        int maxNumber = 0;
        for(int i=1 ; i<5 ; i++){
            for(int j=1 ; j<5 ; j++){
                ThreesBlock block = getBlock(i,j);
                if(block.getNumber()==1){
                    blueCount ++;
                }else if(block.getNumber()==2){
                    redCount ++;
                }else if(block.getNumber()!=0){
                    if(maxNumber < block.getNumber()){
                        maxNumber = block.getNumber();
                    }
                    whiteCount ++;
                }
            }
        }
        int number = decideNextBlockColor(nextBlock,whiteCount,blueCount,redCount,maxNumber);
        nextBlock = new ThreesBlock(number);
        nextBlock.setX(0);
        nextBlock.setY(0);
    }
    
    private int decideNextBlockColor(ThreesBlock nowBlock,int whiteCount,int blueCount,int redCount,int maxNumber) {
        int nowNumber = -1;
        if(nowBlock != null){
            nowNumber = nowBlock.getNumber();
        }
        int[] weights = {5, 5, 5};    //weights for white, blue and red, default for equals.
        for(int i=0;i<3;i++){
            if(i!=nowNumber){
                weights[i] = weights[i] + 15;
            }
        }
        if(redCount==blueCount){
            
        }else if(blueCount > redCount){
            weights[2] = weights[2] + 15*(blueCount - redCount);
        }else{
            weights[1] = weights[1] + 15*(redCount - blueCount);
        }
//        System.out.println(weights[0]+" "+weights[1]+" "+weights[2]);
        
        int newNumber = 0;
        int random = ran.nextInt(weights[0]+weights[1]+weights[2]);
        if(random < weights[0]){
            //white
            int bigNumber = ran.nextInt(100);
            if(bigNumber<10){
                //generate a big number
                if(maxNumber/8 < 3){
                    newNumber = 3;
                }else{
                    newNumber = maxNumber/8;
                }
            }else{
                newNumber = 3;
            }
        }else if(random < weights[0]+weights[1]){
            //blue
            newNumber = 1;
        }else{
            //red
            newNumber = 2;
        }
        
        return newNumber;
    }
    
    public void nextBlockEnter(int direction){
        LinkedList<Integer> emptyBlocks = new LinkedList<>();
        int index = -1;
        switch(direction){
            case Direction.NORTH:
                for(int i=1;i<5;i++){
                    ThreesBlock block = getBlock(i, 4);
                    if(block.getNumber()==0){
                        emptyBlocks.add(i);
                    }
                }
                index = ran.nextInt(emptyBlocks.size());
                blocks[emptyBlocks.get(index)][4].setNumber(nextBlock.getNumber());
                generateNextBlock();
                break;
            case Direction.SOUTH:
                for(int i=1;i<5;i++){
                    ThreesBlock block = getBlock(i, 1);
                    if(block.getNumber()==0){
                        emptyBlocks.add(i);
                    }
                }
                index = ran.nextInt(emptyBlocks.size());
                blocks[emptyBlocks.get(index)][1].setNumber(nextBlock.getNumber());
                generateNextBlock();
                break;
            case Direction.WEST:
                for(int j=1;j<5;j++){
                    ThreesBlock block = getBlock(4, j);
                    if(block.getNumber()==0){
                        emptyBlocks.add(j);
                    }
                }
                index = ran.nextInt(emptyBlocks.size());
                blocks[4][emptyBlocks.get(index)].setNumber(nextBlock.getNumber());
                generateNextBlock();
                break;
            case Direction.EAST:
                 for(int j=1;j<5;j++){
                    ThreesBlock block = getBlock(1, j);
                    if(block.getNumber()==0){
                        emptyBlocks.add(j);
                    }
                }
                index = ran.nextInt(emptyBlocks.size());
                blocks[1][emptyBlocks.get(index)].setNumber(nextBlock.getNumber());
                generateNextBlock();
                break;
            default:
        }
    }

    public ThreesBlock[][] getBlocks() {
        return blocks;
    }

    public void setBlocks(ThreesBlock[][] tempBlocks) {
        this.blocks = tempBlocks;
    }
    
    public ThreesBlock getBlock(int i, int j){
        return this.blocks[i][j];
    }

    public ThreesBlock getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(ThreesBlock nextBlock) {
        this.nextBlock = nextBlock;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    public int computeAllScore(){
        int count = 0;
        for(int i = 1 ; i < 5 ; i++){
            for(int j = 1 ; j < 5 ; j++){
                count = count + blocks[i][j].computeScore();
            }
        }
        return count;
    }
}
