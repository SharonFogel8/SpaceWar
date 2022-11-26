package com.example.exe1_sub;

public class Characters {
    protected int row;
    protected int col;
    protected int maxCol;
    protected int maxRow;
    protected int imgRes;

    public Characters(int maxRow,int maxCol) {
        this.maxCol=maxCol;
        this.maxRow=maxRow;

    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getMaxCol() {
        return maxCol;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public int getImgRes() {
        return imgRes;
    }

    public Characters setRow(int row) {
        if (row>=0 && row<maxCol){
            this.row = row;
            return this;
        }
        else
            return this;

    }

    public Characters setCol(int col) {
        if (col>=0 && row<maxCol){
            this.col = col;
            return this;
        }
        else
            return this;
    }

    public Characters setMaxCol(int maxCol) {
        this.maxCol = maxCol;
        return this;
    }

    public Characters setMaxRow(int maxRow) {
        this.maxRow = maxRow;
        return this;
    }

    public Characters setImgRes(int imgRes) {
        this.imgRes = imgRes;
        return this;
    }
}
