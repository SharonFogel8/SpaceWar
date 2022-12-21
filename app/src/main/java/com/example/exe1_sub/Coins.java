package com.example.exe1_sub;

public class Coins extends Characters{
    private boolean isLastRow;

    public Coins(int maxRow, int maxCol, int col) {
        super(maxRow,maxCol);
        isLastRow=false;
        super.row=0;
        super.setCol(col);
    }

    public boolean isLastRow() {
        return isLastRow;
    }


    public void move(){
        if(isLastRow){
            return;
        }
        else if(row==maxRow-1){
            isLastRow=true;
        }
        super.row++;
    }
}
