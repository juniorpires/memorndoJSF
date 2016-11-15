/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.memorando.db;

import br.edu.ifpe.memorando.models.Memorando;
import br.edu.ifpe.memorando.util.DateUtil;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author casa01
 */
public class MemorandoDao extends GenericDb4oDAO<Memorando>{

    @Override
    protected Query getQueryToUniqueObject(Memorando model, ObjectContainer db) {
        Query query = db.query();
		query.constrain(Memorando.class);
		query.descend("numero").constrain(model.getNumero());

		return query;
    }
    
    
     public String findMaxNum(){
        List<Memorando> memorandos = new ArrayList<Memorando>();
        ObjectContainer db = this.open();
        Query query=db.query();
	query.constrain(Memorando.class);
			query.descend("ano").equals(DateUtil.getCurrentYear());
			query.descend("sequencia").orderDescending();
			memorandos=this.findAll(query, db);
        if(memorandos.isEmpty()){
            return "0";
        }else{
            return memorandos.get(0).getSequencia();
        }
        
        
    }
    
}
