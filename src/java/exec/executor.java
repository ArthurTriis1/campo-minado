/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exec;

import models.Board;

/**
 *
 * @author arthur
 */
public class executor {
    public static void main(String[] args) {
        Board board = new Board(10, 30, 40);
        prA(board.getBoard());
        board.move(0, 0);
        prA(board.getBoard());
        board.switchFlag(9, 9);
        prA(board.getBoard());
        board.switchFlag(9, 9);
        prA(board.getBoard());
        prA(board.revel());
    }
    
    private static void prA(String[][] a){		
    for(int i=0; i<a.length; i++){			
        for(int j=0; j<a[i].length; j++){
	    System.out.print(a[i][j] + " ");	
	}
	System.out.println();
    }
    
    System.out.println();
    System.out.println();
}
}
