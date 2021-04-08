/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import enums.BoardStatus;

/**
 *
 * @author arthur
 */
public class ResponseMovementDTO {
    private BoardStatus status;
    private String[][] board;
    private int countFlags;

    public ResponseMovementDTO(BoardStatus status, String[][] board, int countFlags) {
        this.status = status;
        this.board = board;
        this.countFlags = countFlags;
    }
   
    public BoardStatus getStatus() {
        return status;
    }

    public void setStatus(BoardStatus status) {
        this.status = status;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }
    
    public int getCountFlags() {
        return countFlags;
    }

    public void setCountFlags(int countFlags) {
        this.countFlags = countFlags;
    }
    
    
}
