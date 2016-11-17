/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.memorando.db;

import br.edu.ifpe.memorando.exception.ManyObjectFoundException;
import br.edu.ifpe.memorando.exception.NoUniqueObjectException;
import br.edu.ifpe.memorando.exception.NotFoundObjectException;
import br.edu.ifpe.memorando.exception.SaveException;
import br.edu.ifpe.memorando.exception.UpdateException;
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
    
    private SetorDao setorDao = new SetorDao();

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
     @Override
	protected boolean update(Memorando modelReceived,
			ObjectContainer db) throws UpdateException,
			ManyObjectFoundException, NotFoundObjectException {

		Memorando memorando = this
				.find(modelReceived, db);
		return this.update(memorando, modelReceived, db);

	}

        @Override
	protected boolean update(Memorando modelRetrieved,
			Memorando modelReceived, ObjectContainer db)
			throws UpdateException, ManyObjectFoundException,
			         NotFoundObjectException {

		modelRetrieved.copyAttributesOf(modelReceived);
		// atualiza o SetorDestino
		modelRetrieved.setSetorDestino(this.setorDao.find(
				modelReceived.getSetorDestino(), db));
		// atualiza o SetorOrigem
		modelRetrieved.setSetorOrigem(this.setorDao.find(
				modelReceived.getSetorOrigem(), db));

		return true;
	}
        
        @Override
	protected boolean save(Memorando model, ObjectContainer db,
			boolean checkExists) throws SaveException,
			ManyObjectFoundException, NoUniqueObjectException {
		try {
			if (checkExists) {
				if (this.exists(model, db)) {

					throw new NoUniqueObjectException(
							"memorando encontra-se cadastrado no banco de dados "
									+ model);
				}
			}
			// atualiza o SetorDestino
                        model.setSetorDestino(this.setorDao.find(
				model.getSetorDestino(), db));
                        // atualiza o SetorOrigem
                        model.setSetorOrigem(this.setorDao.find(
				model.getSetorOrigem(), db));

			db.store(model);
			return true;
		} catch (NotFoundObjectException e) {
			throw new SaveException(e);
		}
	}

    
}
