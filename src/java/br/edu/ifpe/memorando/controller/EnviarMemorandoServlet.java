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
@WebServlet(name = "EnviarMemorando", urlPatterns = {"/EnviarMemorando"})
public class EnviarMemorandoServlet extends HttpServlet {

    private SetorDao setorDao;
    private MemorandoDao memorandoDao;
    
    
    public static final String MENSAGEM="msg";

    public EnviarMemorandoServlet() {
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
        
        
         Memorando memorando = Memorando.createMemorando();
         
         memorando.setSequencia(this.gerarSequencia());
         memorando.setAno(DateUtil.getCurrentYear());
         memorando.gerarNumero();

         request.setAttribute("memorando",memorando);
         
         //verifica se foi passado algum parâmetro
         if(this.hasParameters(request)){
             
           
            
            this.fillModel(request, memorando);
            
        try {
            if(memorandoDao.save(memorando, true)){
                  request.setAttribute(MENSAGEM,"O memorando foi salvo com sucesso!");
            }else{
                request.setAttribute(MENSAGEM,"Não foi possível salvar o memorando.");  
            }
            
        } catch (SaveException ex) {
            Logger.getLogger(EnviarMemorandoServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoUniqueObjectException ex) {
            Logger.getLogger(EnviarMemorandoServlet.class.getName()).log(Level.SEVERE, null, ex);
             request.setAttribute(MENSAGEM,"Não foi possível cadastrar. Já existe.");
        } catch (ManyObjectFoundException ex) {
            Logger.getLogger(EnviarMemorandoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
         }
         this.getServletContext().getRequestDispatcher("/enviarMemorando.jsp").forward(request,response);
        
    }
    
    
    private String gerarSequencia(){
        
        int num = Integer.valueOf(this.memorandoDao.findMaxNum());
        if(num==0){
            return "01";
        }else{
            return String.format ("%02d", (num+1));
        }
    }
    
    private void fillModel(HttpServletRequest request,Memorando memorando){
        memorando.setNumero(request.getParameter(Memorando.NUMERO));
            memorando.setAssunto(request.getParameter(Memorando.ASSUNTO));
            memorando.setMensagem(request.getParameter(Memorando.MENSAGEM));
            
            String statusValue = request.getParameter(Memorando.STATUS);
            String tipo = request.getParameter(Memorando.TIPO);
            
            
            memorando.setStatus(Status.fromValue(Integer.valueOf(statusValue)));
            memorando.setTipo(Tipo.fromValue(Integer.valueOf(tipo)));
            
            Setor origem = this.setorDao.findBySigla(request.getParameter(Memorando.SETOR_ORIGEM));
            Setor destino = this.setorDao.findBySigla(request.getParameter(Memorando.SETOR_DESTINO));
            memorando.setSetorOrigem(origem);
            memorando.setSetorDestino(destino);
            
            memorando.setSequencia(this.gerarSequencia());
            memorando.setAno(DateUtil.getCurrentYear());
            memorando.gerarNumero();
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
