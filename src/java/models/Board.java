package models;

import enums.BoardStatus;
import java.util.Random;

public final class Board {
    private final int[][] minas;
    private final String[][] board, flagsBoard;
    private int countFlags;
    Random random = new Random();
    private final int colsTotal, linesTotal, bombsTotal;
    private int codigo;
    private BoardStatus status = BoardStatus.INGAME;
    
    public Board(int colsTotal, int linesTotal, int bombsTotal){
        this.colsTotal = colsTotal;
        this.linesTotal = linesTotal;
        this.bombsTotal = bombsTotal;
        this.countFlags = bombsTotal;
        minas = new int[colsTotal][linesTotal];
        board = new String[colsTotal][linesTotal];
        flagsBoard = new String[colsTotal][linesTotal];
        initBombsBoard();
        randomBombs(bombsTotal);
        preencheDicas();
        initBoard();
        initFlags();
        
    }
    
    public boolean win(){
        
        int countRigthFlags = 0;
        int countCleanSpaces=0;
        
        if(this.countFlags == 0){
            for(int line = 0 ; line < this.linesTotal ; line++)
                for(int column = 0 ; column < this.colsTotal ; column++)
                    if("F".equals(flagsBoard[column][line]) && minas[column][line] == -1)
                        countRigthFlags++;
            
        }
        
        
        for(int line = 0 ; line < this.linesTotal ; line++)
            for(int column = 0 ; column < this.colsTotal ; column++)
                if("_".equals(board[column][line]))
                    countCleanSpaces++;
        
        return countCleanSpaces == this.bombsTotal || countRigthFlags == this.bombsTotal;                
    }
    
    public void checkNeighbors(int line, int col){
        
        boolean notMine = minas[col][line] != -1;
        boolean blank = board[col][line].equals("_");
        
        if( notMine && blank){
            board[col][line] = Integer.toString(minas[col][line]);
            
            if(flagsBoard[col][line].equals("F")){
                this.countFlags++;
            }
        }else{
            return;
        }
        
        if(minas[col][line] != 0){
            return;
        }
        
        for(int j=-1 ; j<2 ; j++)
            for(int i=-1 ; i<2 ; i++)
                if((line + j != -1 && line + j != this.linesTotal &&  col + i != -1 && col + i != this.colsTotal ) ){
                    checkNeighbors(line+j, col+i);
                }       
    }
    
    public int getPosition(int line, int col){
        return minas[col][line];
    }
    
    public BoardStatus move(int line, int col){     
        if(getPosition(line, col)== -1){
            this.status = BoardStatus.LOSE;
            this.countFlags = this.bombsTotal;
            this.revel();
        }
        
        this.checkNeighbors(line, col);
        
        if(this.win()){
            this.status = BoardStatus.WIN;
            this.countFlags = this.bombsTotal;
            this.revel();
        }
        
        return this.status;    
    }
    
    public void preencheDicas(){
        for(int col= 0 ; col < this.colsTotal ; col++){
            for(int line= 0 ; line < this.linesTotal ; line++)
                for(int i=-1 ; i<=1 ; i++)
                    for(int j=-1 ; j<=1 ; j++)
                        if(minas[col][line] != -1)
                            if((line + j != -1 && line + j != this.linesTotal &&  col + i != -1 && col + i != this.colsTotal ) )
                                if(minas[col+i][line+j] == -1)
                                    minas[col][line]++;
                
            }
            
    }
    
    public void initBoard(){
        for(int i=0 ; i< this.colsTotal ; i++)
            for(int j=0 ; j< this.linesTotal ; j++)
                board[i][j]= "_";
    }
    
    public void initBombsBoard(){
        for(int i=0 ; i<this.colsTotal ; i++)
            for(int j=0 ; j<this.linesTotal ; j++)
                minas[i][j]=0;
    }
    
    public void randomBombs(int bombs){
        boolean sorted;
        int line, col;
        for(int i=0 ; i < bombs ; i++){
            
            do{
                line = random.nextInt(this.linesTotal  -1);
                col = random.nextInt(this.colsTotal  -1);
                
                if(minas[col][line] == -1)
                    sorted=true;
                else
                    sorted = false;
            }while(sorted);
            
            minas[col][line] = -1;
        }
    }

    public String[][] getBoard() {
        
        
        if(this.status == BoardStatus.INGAME){
            String[][] returnBoard = new String[this.colsTotal][this.linesTotal];
            for(int i=0 ; i<this.colsTotal ; i++){
               for(int j=0 ; j<this.linesTotal ; j++){
                   if(this.board[i][j].equals("_")){
                       returnBoard[i][j] = this.flagsBoard[i][j];
                   }else{
                       returnBoard[i][j] = this.board[i][j];
                   }
               }
            }
            
            return returnBoard;
        }
        return board;
    }
    
    public String[][] revel(){
        for(int i=0 ; i<this.colsTotal ; i++){
           for(int j=0 ; j<this.linesTotal ; j++){
               if(minas[i][j] == -1){
                   this.board[i][j] = "*";
               }else{
                   this.board[i][j] = Integer.toString(minas[i][j]);
               }
           }
        }
        return board;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public BoardStatus getStatus() {
        return status;
    }

    public void setStatus(BoardStatus status) {
        this.status = status;
    }

    public void initFlags(){
        for(int i=0 ; i< this.colsTotal ; i++)
            for(int j=0 ; j< this.linesTotal ; j++)
                flagsBoard[i][j]= "_";
    }
    
    public BoardStatus switchFlag(int col, int line){
        if("_".equals(flagsBoard[col][line]) && countFlags > 0){
            flagsBoard[col][line] = "F";
            countFlags--;
        }else if("F".equals(flagsBoard[col][line])){
            flagsBoard[col][line] = "_";
            countFlags++;
        }
        
        if(this.win()){
            this.status = BoardStatus.WIN;
            this.revel();
        }
        
        return this.status;

    }

    public int getCountFlags() {
        return countFlags;
    }    
}