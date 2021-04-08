/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import models.Board;

/**
 *
 * @author arthur
 */
public class ResponseNewGameDTO {
    private int id;
    private String[][] board;
    private int totalBombs;

    public int getTotalBombs() {
        return totalBombs;
    }

    public void setTotalBombs(int totalBombs) {
        this.totalBombs = totalBombs;
    }

    public ResponseNewGameDTO(int id, String[][] board, int totalBombs) {
        this.id = id;
        this.board = board;
        this.totalBombs = totalBombs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }
    
    
}
