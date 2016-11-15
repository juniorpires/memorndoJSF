/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.memorando.models;

import java.util.List;
import java.util.UUID;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author casa01
 */
@ManagedBean
public class Setor extends IModel<Setor>{
    
    
    public static final String NOME= "nome";
    public static final String SIGLA= "sigla";
    public static final String SENHA = "senha";
            
    private String id;
    private String nome;
    private String sigla;
    private String senha;
    private List<Memorando> memorandos;

    
   public static Setor createSetor(){
       Setor s = new Setor();
       s.gerarId();
       return s;
   }
   public Setor(){
      
   }
   
   private void gerarId(){
        this.id = UUID.randomUUID().toString();
   }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    

    /**
     * @return the sigla
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * @param sigla the sigla to set
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

   

    @Override
    public void copyAttributesOf(Setor copy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unsetAttributes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cleanStringWithNull() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String label() {
        return this.sigla;
    }

    /**
     * @return the memorandos
     */
    public List<Memorando> getMemorandos() {
        return memorandos;
    }

    /**
     * @param memorandos the memorandos to set
     */
    public void setMemorandos(List<Memorando> memorandos) {
        this.memorandos = memorandos;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    
}
