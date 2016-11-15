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
public enum Tipo {
    CIRCULAR("1"),REQUISICAO("2");
    
    private String codigo;
    
      Tipo(String codigo){
          this.codigo = codigo;
    }
        
      
      public static Tipo fromValue(int value) 
             throws IllegalArgumentException {
         try {
              return Tipo.values()[value];
         } catch(ArrayIndexOutOfBoundsException e) {
              throw new IllegalArgumentException("Unknown enum value :"+ value);
         }
     }
}
