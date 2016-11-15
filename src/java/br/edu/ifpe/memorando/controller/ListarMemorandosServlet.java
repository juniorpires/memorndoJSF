/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.memorando.controller;

import br.edu.ifpe.memorando.db.MemorandoDao;
import br.edu.ifpe.memorando.db.SetorDao;
import br.edu.ifpe.memorando.exception.ManyObjectFoundException;
import br.edu.ifpe.memorando.exception.NoUniqueObjectException;
import br.edu.ifpe.memorando.exception.SaveException;
import br.edu.ifpe.memorando.models.Memorando;
import br.edu.ifpe.memorando.models.Setor;
import br.edu.ifpe.memorando.models.Status;
import br.edu.ifpe.memorando.models.Tipo;
import br.edu.ifpe.memorando.util.DateUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author casa01
 */
@WebServlet(name = "ListarMemorandos", urlPatterns = {"/ListarMemorandos"})
public class ListarMemorandosServlet extends HttpServlet {

    private SetorDao setorDao;
    private MemorandoDao memorandoDao;
    
    
    public static final String MENSAGEM="msg";

    public ListarMemorandosServlet() {
        super();
        this.setorDao = new SetorDao();
        this.memorandoDao = new MemorandoDao();
    }
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
         if(this.hasParameters(request)){
            Setor destino = this.setorDao.findBySigla(request.getParameter(Memorando.SETOR_DESTINO));
            Memorando memorando = new Memorando();
            memorando.setSetorDestino(destino);
            request.setAttribute("memoBusca",memorando);
         }
         this.getServletContext().getRequestDispatcher("/listarMemorandos.jsp").forward(request,response);
        
    }

    
    private boolean hasParameters(HttpServletRequest request){
        return request.getParameterNames().hasMoreElements();
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
