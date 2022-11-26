package com.example.exe1_sub;

public class Player extends Characters {
    private boolean isMovingRight;


    public Player(int maxRow,int maxCol) {
        super(maxRow,maxCol);
        row=maxRow-1;
    }

    @Override
    public Characters setRow(int row) {
        return super.setRow(super.maxRow-1);
    }
    public void move(eMoving move){
        if(move==eMoving.right){
            if(col>=(maxCol-1)){
                return;
            }
            col++;
        }
        else{
            if(col<=0){
                return;
            }
            col--;
        }
    }
}
