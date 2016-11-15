/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.memorando.models;

/**
 *
 * @author casa01
 */
public enum Status {
    ENVIADO(1,"Enviado"),RECEBIDO_E_NAO_ENVIADO(2,"Recebido e não enviado")
    ,LIDO(3,"Lido"),DADO_CIENCIA(4,"Dado ciência"),RESPONDIDO(5,"Respondido");
    
    
    private int codigo;
    private String label;
    Status(int codigo,String label){
        this.codigo = codigo;
        this.label = label;
    }
    
    public int getCodigo(){
        return this.codigo;
    }
    
    public String getLabel(){
        return this.label;
    }
     public static Status fromValue(int value) 
             throws IllegalArgumentException {
         try {
              return Status.values()[value];
         } catch(ArrayIndexOutOfBoundsException e) {
              throw new IllegalArgumentException("Unknown enum value :"+ value);
         }
     }
}
