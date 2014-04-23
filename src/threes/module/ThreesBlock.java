/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package threes.module;

/**
 *
 * @author Legend
 */
public class ThreesBlock {
    private int number;
    private int x;
    private int y;
    private int originX;
    private int originY;
    private boolean movable = false;
    
    public ThreesBlock(int number){
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getOriginX() {
        return originX;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }
    
    public int computeScore(){
        if(this.number<3){
            return 0;
        }else{
            int count = 1;
            int temp = this.number;
            while(temp!=3){
                temp = temp / 2;
                count ++;
            }
            return (int)Math.pow(3, count);
        }
    }
 
    @Override
    public ThreesBlock clone(){
        ThreesBlock newBlock = new ThreesBlock(number);
        newBlock.setOriginX(originX);
        newBlock.setOriginY(originY);
        newBlock.setX(x);
        newBlock.setY(y);
        return newBlock;
    }
}
