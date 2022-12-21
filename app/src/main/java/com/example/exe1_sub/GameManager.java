package com.example.exe1_sub;

public class GameManager {
    private Characters[][] bord;
    private Player player;
    private int cols;
    private int rows;
    private int life;

    public GameManager(int rows, int cols, int life) {
        this.cols = cols;
        this.rows = rows;
        this.life = life;
        player= (Player) new Player(rows,cols).setMaxRow(rows).setMaxCol(cols);
        bord= new Characters[rows][cols];
        startGame();
    }
    public int findEnemyCol(int row){
        for (int i = 0; i < cols; i++) {
            if (bord[row][i] instanceof Enemy){
                return i;
            }
        }
        return -1;
    }
    public int findCoinCol(int row){
        for (int i = 0; i < cols; i++) {
            if (bord[row][i] instanceof Coins){
                return i;
            }
        }
        return -1;
    }
    public void nextStep(){
        for (int i = rows-1; i >= 0; i--) {
            for (int j = cols-1; j >=0; j--) {
                if(bord[i][j] instanceof Enemy) {
                    ((Enemy) bord[i][j]).move();
                    if(i<rows-1) {
                        bord[i + 1][j] = bord[i][j];
                    }
                    bord[i][j]=null;
                }
                else if(bord[i][j] instanceof Coins){
                    ((Coins) bord[i][j]).move();
                    if(i<rows-1) {
                        bord[i + 1][j] = bord[i][j];
                    }
                    bord[i][j]=null;
                }
            }
        }
        newCoin();
        newEnemy();
    }
    public boolean isSucceededCoin(){
        int coinCol=findCoinCol(rows-1);
        if(coinCol>=0 && coinCol == player.col) {
            return true;
        }
        return false;
    }


    public boolean isCrashed() {
        int enemyCol=findEnemyCol(rows-1);
        if(enemyCol>=0 && enemyCol == player.col) {
           life--;
           return true;
       }
    return false;
    }

    public boolean isLose() {
        return life==0;
    }

    public void startGame() {
        newEnemy();
        bord[rows-1][cols/2]=player;

    }

    public void newEnemy() {
        int col= (int)(Math.random()*cols);
        bord[0][col]= new Enemy(rows,cols,col);
    }
    public void newCoin() {
        int temp = (int) (Math.random() * 3);
        if(temp!=1){
            return;
        }
        else {
            int col;
            do {
                col = (int) (Math.random() * cols);
            } while (bord[0][col] instanceof Enemy);

            bord[0][col] = new Coins(rows, cols, col);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int getLife() {
        return life;
    }

    public GameManager setCols(int cols) {
        this.cols = cols;
        return this;
    }

    public GameManager setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public GameManager setLife(int life) {
        this.life = life;
        return this;
    }
    public void move(eMoving move){
        bord[player.row][player.col]=null;
        player.move(move);
        bord[player.row][player.col]=player;
    }

}
