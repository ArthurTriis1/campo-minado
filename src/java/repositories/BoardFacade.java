/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositories;

import java.util.Collection;
import models.Board;

/**
 *
 * @author arthur
 */
public class BoardFacade {
    private static final BoardFacade fachada;
    private final BoardRepository repositorio;

    static {
        fachada = new BoardFacade();
    }

    private BoardFacade() {
        repositorio = new BoardRepository();
    }

    public static BoardFacade getInstance() {
        return fachada;
    }

    public Board inserir(Board func) {
        return repositorio.inserir(func);
    }

    public Board remover(int id) {
        return repositorio.remover(id);
    }


    public Board procurarPorCodigo(int codigo) {
        return repositorio.procurarPorCodigo(codigo);
    }

    public Collection<Board> listarTodos() {
        return repositorio.listarTodos();
    }
}
