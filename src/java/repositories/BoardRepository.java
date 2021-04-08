/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositories;

import java.util.ArrayList;
import java.util.Collection;
import models.Board;

/**
 *
 * @author arthur
 */
public class BoardRepository {
        private Collection<Board> repositorio = new ArrayList<Board>();
    private static int id = 0;

    public Board inserir(Board func) {
        func.setCodigo(id++);
        repositorio.add(func);
        return func;
    }

    public Board remover(int id) {
        Board func = this.procurarPorCodigo(id);
        
        repositorio.remove(func);
        
        return func;
    }



    public Board procurarPorCodigo(int codigo) {   
        return repositorio.stream().filter(board -> board.getCodigo() == codigo).findFirst().orElse(null);
    }

    public Collection<Board> listarTodos() {
        return repositorio;
    }
}
