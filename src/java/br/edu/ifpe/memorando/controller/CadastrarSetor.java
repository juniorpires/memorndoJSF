/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.memorando.controller;

import br.edu.ifpe.memorando.db.SetorDao;
import br.edu.ifpe.memorando.exception.ManyObjectFoundException;
import br.edu.ifpe.memorando.exception.NoUniqueObjectException;
import br.edu.ifpe.memorando.exception.SaveException;
import br.edu.ifpe.memorando.models.Setor;
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
@WebServlet(name = "CadastrarSetor", urlPatterns = {"/CadastrarSetor"})
public class CadastrarSetor extends HttpServlet {

    public static final String MENSAGEM="msg";
    
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
        
        
         Setor s = Setor.createSetor();
         
         
         //verifica se foi passado algum parâmetro
         if(request.getParameterNames().hasMoreElements()){
            s.setNome(request.getParameter(Setor.NOME));
            s.setSenha(request.getParameter(Setor.SENHA));
            s.setSigla(request.getParameter(Setor.SIGLA));
        
            SetorDao dao = new SetorDao();
        
        
        try {
            if(dao.save(s, true)){
                  request.setAttribute(MENSAGEM,"O setor foi salvo com sucesso!");
            }else{
                request.setAttribute(MENSAGEM,"Não fou possível salvar o setor.");  
            }
            
        } catch (SaveException ex) {
            Logger.getLogger(CadastrarSetor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoUniqueObjectException ex) {
            Logger.getLogger(CadastrarSetor.class.getName()).log(Level.SEVERE, null, ex);
             request.setAttribute(MENSAGEM,"Não foi possível cadastrar. Já existe.");
        } catch (ManyObjectFoundException ex) {
            Logger.getLogger(CadastrarSetor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
         }
         this.getServletContext().getRequestDispatcher("/cadastrarSetor.jsp").forward(request,response);
        
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
