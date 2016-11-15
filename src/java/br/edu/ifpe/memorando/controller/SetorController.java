/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.memorando.controller;

import br.edu.ifpe.memorando.db.SetorDao;
import br.edu.ifpe.memorando.exception.ManyObjectFoundException;
import br.edu.ifpe.memorando.exception.NoUniqueObjectException;
import br.edu.ifpe.memorando.exception.NotFoundObjectException;
import br.edu.ifpe.memorando.exception.SaveException;
import br.edu.ifpe.memorando.exception.UpdateException;
import br.edu.ifpe.memorando.models.Setor;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Eduardo
 */

@ManagedBean
public class SetorController {
    
    private SetorDao dao;
    private List<Setor> setores;
    
    public SetorController(){
        super();
        this.dao = new SetorDao();
        this.setores = this.dao.findAll();
    }
    
    
    public String inserir(Setor setor){
        String confirma = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("confirma");
        
        if(!setor.getSenha().equals(confirma)){
            FacesContext.getCurrentInstance().addMessage("formCadSetor:passConfirma", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro","As senha não conferem"));
            return null;
        }
        
            MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
             md.update(setor.getSenha().getBytes(),0,setor.getSenha().length());
            setor.setSenha(new String(md.digest()).replace("'", "0").replace("\r", "1"));
            
            this.dao.save(setor, true);
            this.setores = this.getSetores();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("o setor foi cadastrado com sucesso!"));
        } catch (NoSuchAlgorithmException ex) {
        } catch (SaveException ex) {
            Logger.getLogger(SetorController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoUniqueObjectException ex) {
            Logger.getLogger(SetorController.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Setor já existe!"));
        } catch (ManyObjectFoundException ex) {
            Logger.getLogger(SetorController.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        
        
        return "setorCreate.xhtml";
    }
    
    public void alterar(Setor setor){
        try {
            this.dao.update(setor);
        } catch (UpdateException ex) {
            Logger.getLogger(SetorController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundObjectException ex) {
            Logger.getLogger(SetorController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ManyObjectFoundException ex) {
            Logger.getLogger(SetorController.class.getName()).log(Level.SEVERE, null, ex);
        }
               
    }
    
    public List<Setor> getSetores(){
        return this.setores;
    }
    
    public void remover(Setor setor){
        this.dao.delete(setor);
    }
    
    
}
