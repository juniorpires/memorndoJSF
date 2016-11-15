/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.memorando.db;

import br.edu.ifpe.memorando.exception.ManyObjectFoundException;
import br.edu.ifpe.memorando.models.Setor;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author casa01
 */
public class SetorDao extends GenericDb4oDAO<Setor>{

    @Override
    protected Query getQueryToUniqueObject(Setor model, ObjectContainer db) {
        Query query = db.query();
		query.constrain(Setor.class);
		query.descend("sigla").constrain(model.getSigla());

		return query;
    }
    
    public Setor findBySigla(String sigla){
        Setor setor = new Setor();
        setor.setSigla(sigla);
        ObjectContainer db = this.open();
        
                
        Setor s=null;
        try {
            s = this.find(setor);
        } catch (ManyObjectFoundException ex) {
            Logger.getLogger(SetorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return s;
    }
    
}
