package services;

import DTOs.RequestMovementDTO;
import DTOs.ResponseMovementDTO;
import DTOs.ResponseNewGameDTO;
import enums.BoardStatus;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import models.Board;
import repositories.BoardFacade;

@Path("")
public class CampoMinado {
    BoardFacade boardFacade = BoardFacade.getInstance();
 
    
    @GET
    @Path("new-game")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseNewGameDTO newGame(@QueryParam("cols") int cols, @QueryParam("lines") int lines, @QueryParam("bombs") int bombs ) {
        Board board = this.boardFacade.inserir(new Board(cols, lines, bombs));
        return new ResponseNewGameDTO(board.getCodigo(), board.getBoard(), bombs);
    }
    
    @POST
    @Path("movement/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMovementDTO movement(RequestMovementDTO movement, @PathParam("id") int id){
        Board board = this.getBoard(id);
        
        BoardStatus status = board.move(movement.getLine(), movement.getCol());
        
        if(status == BoardStatus.LOSE|| status == BoardStatus.WIN){
            boardFacade.remover(id);
        }
        
        return new ResponseMovementDTO(status, board.getBoard(), board.getCountFlags());
    }
    
    @PUT
    @Path("switchflag/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMovementDTO switchFlag(RequestMovementDTO movement, @PathParam("id") int id){
        Board board = this.getBoard(id);
        
        BoardStatus status = board.switchFlag(movement.getCol(), movement.getLine());
        
        if(status == BoardStatus.LOSE|| status == BoardStatus.WIN){
            boardFacade.remover(id);
        }
        
        return new ResponseMovementDTO(status, board.getBoard(), board.getCountFlags());
    }
    
    
    
    private Board getBoard(int id){
        Board board = boardFacade.procurarPorCodigo(id);
        
        if(board == null){
            throw new BadRequestException();
        }  
        
        return board;
    }
}
