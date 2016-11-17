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
import br.edu.ifpe.memorando.exception.NotFoundObjectException;
import br.edu.ifpe.memorando.exception.SaveException;
import br.edu.ifpe.memorando.exception.UpdateException;
import br.edu.ifpe.memorando.models.Memorando;
import br.edu.ifpe.memorando.models.Setor;
import br.edu.ifpe.memorando.models.Status;
import br.edu.ifpe.memorando.models.Tipo;
import br.edu.ifpe.memorando.util.DateUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private SetorDao setorDao;
    private List<Memorando> memorandos;
    private List<SelectItem> setores;
    
    public MemorandoController(){
        super();
        this.dao = new MemorandoDao();
        this.setorDao = new SetorDao();
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
            this.fillModel(memorando);
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
    
    private void fillModel(Memorando memorando){
        
            Map<String,Object> map = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
            String status = (String) map.get("status");
            String tipo = (String) map.get("tipo");
            String origem = (String) map.get("origem");
            String destino = (String) map.get("destino");
            memorando.setStatus(Status.fromValue(Integer.valueOf(status)));
            memorando.setTipo(Tipo.fromValue(Integer.valueOf(tipo)));
            
            Setor sOrigem = this.setorDao.findBySigla(origem);
            Setor sDestino = this.setorDao.findBySigla(destino);
            memorando.setSetorOrigem(sOrigem);
            memorando.setSetorDestino(sDestino);
            
            memorando.setSequencia(this.gerarSequencia());
            memorando.setAno(DateUtil.getCurrentYear());
            memorando.gerarNumero();
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
    
    private String gerarSequencia(){
        
        int num = Integer.valueOf(this.dao.findMaxNum());
        if(num==0){
            return "01";
        }else{
            return String.format ("%02d", (num+1));
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
