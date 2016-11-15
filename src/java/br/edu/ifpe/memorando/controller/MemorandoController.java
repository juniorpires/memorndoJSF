/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.memorando.controller;

import br.edu.ifpe.memorando.db.MemorandoDao;
import br.edu.ifpe.memorando.exception.ManyObjectFoundException;
import br.edu.ifpe.memorando.exception.NoUniqueObjectException;
import br.edu.ifpe.memorando.exception.NotFoundObjectException;
import br.edu.ifpe.memorando.exception.SaveException;
import br.edu.ifpe.memorando.exception.UpdateException;
import br.edu.ifpe.memorando.models.Memorando;
import br.edu.ifpe.memorando.models.Setor;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Eduardo
 */

@ManagedBean
public class MemorandoController {
    
    private MemorandoDao dao;
    private List<Memorando> memorandos;
    private List<SelectItem> setores;
    
    public MemorandoController(){
        super();
        this.dao = new MemorandoDao();
        this.memorandos = this.dao.findAll();
        this.findSetores();
        
        
        
    }
    
    private void findSetores(){
        this.setores = new ArrayList<SelectItem>();
        
        List<Setor> list = new SetorController().getSetores();
        for (Setor setor : list) {
            this.setores.add(new SelectItem(setor.getId(),setor.getSigla()));
        }
        
    }
    public String inserir(Memorando memorando){
        
        try {
            this.dao.save(memorando, true);
            this.memorandos = this.getMemorandos();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("o memorando foi cadastrado com sucesso!"));
        }catch (SaveException ex) {
            Logger.getLogger(MemorandoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoUniqueObjectException ex) {
            Logger.getLogger(MemorandoController.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Memorando já existe!"));
        } catch (ManyObjectFoundException ex) {
            Logger.getLogger(MemorandoController.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        
        
        return "memorandoCreate.xhtml";
    }
    
    public void alterar(Memorando memorando){
        try {
            this.dao.update(memorando);
        } catch (UpdateException ex) {
            Logger.getLogger(MemorandoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundObjectException ex) {
            Logger.getLogger(MemorandoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ManyObjectFoundException ex) {
            Logger.getLogger(MemorandoController.class.getName()).log(Level.SEVERE, null, ex);
        }
               
    }
    
    public List<Memorando> getMemorandos(){
        return this.memorandos;
    }
    
    public List<SelectItem> getSetores(){
        return this.setores;
    }
    
    public void remover(Memorando memorando){
        this.dao.delete(memorando);
    }
    
    
}
