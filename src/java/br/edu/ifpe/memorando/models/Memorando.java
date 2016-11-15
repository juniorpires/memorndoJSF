/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.memorando.models;

import java.util.UUID;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author casa01
 */
@ManagedBean
public class Memorando extends IModel<Setor>{
    
    
    public static final String NUMERO = "numero";
    public static final String ASSUNTO = "assunto";
    public static final String MENSAGEM = "mensagem";
    public static final String STATUS = "status";
    public static final String TIPO = "tipo";
    public static final String SETOR_ORIGEM = "setorOrigem";
    public static final String SETOR_DESTINO = "setorDestino";
            
            
            
    private String id;
    private String numero;
    private String assunto;
    private String mensagem;
    private Status status;
    private Tipo tipo;
    private Setor setorOrigem;
    private Setor setorDestino;
    private String sequencia;
    private String ano;
    

    public static Memorando createMemorando(){
        Memorando m = new Memorando();
        m.gerarId();
        return m;
    }
    
    public Memorando(){
        
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
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the assunto
     */
    public String getAssunto() {
        return assunto;
    }

    /**
     * @param assunto the assunto to set
     */
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    /**
     * @return the mensagem
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * @param mensagem the mensagem to set
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the tipo
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the setorOrigem
     */
    public Setor getSetorOrigem() {
        return setorOrigem;
    }

    /**
     * @param setorOrigem the setorOrigem to set
     */
    public void setSetorOrigem(Setor setorOrigem) {
        this.setorOrigem = setorOrigem;
    }

    /**
     * @return the setorDestino
     */
    public Setor getSetorDestino() {
        return setorDestino;
    }

    /**
     * @param setorDestino the setorDestino to set
     */
    public void setSetorDestino(Setor setorDestino) {
        this.setorDestino = setorDestino;
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
        return this.assunto;
    }

    /**
     * @return the sequencia
     */
    public String getSequencia() {
        return sequencia;
    }

    /**
     * @param sequencia the sequencia to set
     */
    public void setSequencia(String sequencia) {
        this.sequencia = sequencia;
    }

    /**
     * @return the ano
     */
    public String getAno() {
        return ano;
    }

    /**
     * @param ano the ano to set
     */
    public void setAno(String ano) {
        this.ano = ano;
    }
    
    public void gerarNumero(){
        this.numero = this.sequencia+"/"+this.ano;
    }
    
}
