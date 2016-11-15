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
import br.edu.ifpe.memorando.interfaces.IDAO;
import br.edu.ifpe.memorando.models.IModel;
import br.edu.ifpe.memorando.models.Setor;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.edu.ifpe.memorando.models.Memorando;

public abstract class GenericDb4oDAO<T extends IModel> implements IDAO<T> {

	private Class<T> classModel;
	public static String DATA_BASE_FILE = GenericDb4oDAO.path();
	;
	//public static String DATA_BASE_FILE = "/etc/inspetor_digital_database.YAP";

//	static {
//		DATA_BASE_FILE = Environment.getExternalStorageDirectory()
//				+ "/inspetor_digital_database.YAP";
//
//	}
        
        public static String path(){
            
            String canonicalPath = "memorando_database.YAP";
            return canonicalPath;
        }

	public GenericDb4oDAO() {
		super();
           
		this.classModel = ((Class) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0]);

	}

	protected ObjectContainer open() {
		return Db4oEmbedded.openFile(this.getConfiguration(),
				GenericDb4oDAO.DATA_BASE_FILE);
	}

	/**
	 * 
	 * @return
	 */
	protected EmbeddedConfiguration getConfiguration() {

		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		
		config.file().lockDatabaseFile(false);
		
		// Setor
		config.common().objectClass(Setor.class).objectField("sigla")
				.indexed(true);
                
                //Memorando
                config.common().objectClass(Memorando.class).objectField("id")
				.indexed(true);
                config.common().objectClass(Memorando.class).objectField("setorOrigem")
				.indexed(true);
                config.common().objectClass(Memorando.class).objectField("setorDestino")
				.indexed(true);
                config.common().objectClass(Memorando.class).objectField("setorOrigem")
				.cascadeOnDelete(true);
                config.common().objectClass(Memorando.class).objectField("setorDestino")
				.cascadeOnDelete(true);

		

		return config;
	}

	public List<T> findAllWithPagination(int limit, int offset) {
		List<T> lista = new ArrayList<T>();
		ObjectContainer db = this.open();
		try {
			Query query = db.query();
			query.constrain(getClassModel());

			ObjectSet<T> result = query.execute();
			// pega o tamanho da lista
			int size = result.size();
			// calcula o final da lista
			int end = this.calculateEnd(limit, offset, size);
			for (int i = offset; i < end; i++) {
				lista.add(result.get(i));
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return lista;

	}

	/**
	 * 
	 * @param limit quantidade máxima de registros que devem ser
	 * recuperados do banco de dados
	 * @param offset posição em que se deve buscar os registros
	 * @param query consulta com os filtros
	 * @param db conexão com o banco de dados
	 * @return registros encontrados respeitando os parâmetros
	 */
	protected List<T> findAllWithPagination(int limit, int offset, Query query,
			ObjectContainer db) {
		List<T> lista = new ArrayList<T>();
		query.constrain(getClassModel());

		ObjectSet<T> result = query.execute();
		// pega o tamanho da lista
		int size = result.size();
		// calcula o final da lista
		int end = this.calculateEnd(limit, offset, size);
		for (int i = offset; i < end; i++) {
			lista.add(result.get(i));
		}
		
		return lista;

	}

	private int calculateEnd(int limit, int offset, int size) {
		int end = limit + offset;
		if (end >= size) {
			return size;
		}
		return end;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.pe.saudecaruaru.inspetordigital.interfaces.IDAO#count()
	 */
	@Override
	public int count() {
		int total = -1;
		ObjectContainer db = this.open();
		try {
			Query query = db.query();
			query.constrain(getClassModel());
			total = query.execute().size();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return total;
	}

	@Override
	public boolean save(T model, boolean checkExist) throws SaveException,
			         NoUniqueObjectException, ManyObjectFoundException {
		boolean success = false;
		ObjectContainer db = this.open();
		try {
			if (!this.save(model, db, checkExist)) {
				throw new SaveException("Impossível salvar o objeto! " + model);
			}
			db.commit();
			success = true;
		} catch (SaveException ex) {
			throw ex;
		} catch (NoUniqueObjectException ex) {
			throw ex;
		} catch (ManyObjectFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			success = false;
			db.rollback();
			ex.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return success;
	}

	@Override
	public boolean merge(T model) throws SaveException, UpdateException,
			NoUniqueObjectException, ManyObjectFoundException {
		boolean success = false;
		ObjectContainer db = this.open();
		try {
			T retrieved = this.getModel(model, db);
			if (retrieved == null) {
				if (!this.save(model, db, false)) {
					throw new SaveException("Impossível salvar o objeto! "
							+ model);
				}

			} else {
				this.update(retrieved, model, db);
			}
			db.commit();
			success = true;
		} catch (SaveException ex) {
			throw ex;
		} catch (UpdateException ex) {
			throw ex;
		} catch (NoUniqueObjectException ex) {
			throw ex;
		} catch (ManyObjectFoundException ex) {
			throw ex;
		} catch (Exception e) {
			// TODO: handle exception
			success = false;
			db.rollback();
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return success;
	}

	@Override
	public boolean update(T model) throws UpdateException,
			         NotFoundObjectException, ManyObjectFoundException {
		boolean success = false;
		ObjectContainer db = this.open();
		try {
			this.update(model, db);
			db.commit();
			success = true;
		} catch (UpdateException ex) {
			throw ex;
		} catch (NotFoundObjectException ex) {
			throw ex;
		} catch (ManyObjectFoundException ex) {
			throw ex;
		} catch (Exception e) {
			success = false;
			db.rollback();
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return success;
	}

	@Override
	public List<T> findAll() {
		List<T> l = new ArrayList<T>();
		ObjectContainer db = this.open();
		try {
			l.addAll(db.query(this.getClassModel()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return l;
	}

	@Override
	public T find(T model) throws ManyObjectFoundException {
		// TODO Auto-generated method stub
		T t = null;
		ObjectContainer db = this.open();

		try {
			ObjectSet<T> set = this.getQueryToUniqueObject(model, db).execute();
			int size = set.size();
			if (size == 1) {
				t = set.get(0);
			} else if (size == 0) {
				throw new NotFoundObjectException("Não foi possível encontrar "
						+ model + " no banco de dados.");
			} else {
				throw new ManyObjectFoundException("Mais de um " + model
						+ " encontrado para no banco de dados.");
			}

		} catch (ManyObjectFoundException ex) {
			throw ex;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return t;
	}

	@Override
	public T findByHashCode(T model) throws ManyObjectFoundException {

		T t = null;
		ObjectContainer db = this.open();

		try {
			ObjectSet<T> set = db.query(new PredicateForHashCode(model));
			if (set.size() == 1) {
				t = set.get(0);
			} else if (set.size() > 1) {
				throw new ManyObjectFoundException(
						"Muitos objetos encontrados!");
			}

		} catch (ManyObjectFoundException ex) {
			throw ex;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return t;
	}

	@Override
	public List<T> findAll(T model) {
		// TODO Auto-generated method stub
		List<T> l = new ArrayList<T>();
		ObjectContainer db = this.open();
		try {

           ObjectSet<T> list =  db.queryByExample(model);

           for(T item: list){
              l.add(item);
           }
			//l.addAll((Collection<? extends T>)
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return l;
	}

	
	protected List<T> findAll(Query query, ObjectContainer db) {
		
		List<T> l = new ArrayList<T>();
        ObjectSet<T> list =  query.execute();

        for(T item: list){
            l.add(item);
        }
		//l.addAll((Collection<? extends T>) query.execute());
		
		return l;
	}
	@Override
	public boolean save(List<T> models, boolean checkExist)
			throws SaveException, NoUniqueObjectException,
			ManyObjectFoundException {
		// TODO Auto-generated method stub
		ObjectContainer db = this.open();
		boolean success = false;
		try {
			for (T t : models) {
				if (!this.save(t, db, checkExist)) {
					throw new SaveException("Impossível salvar o objeto! " + t);
				}
			}
			db.commit();
			success = true;
		} catch (SaveException ex) {
			throw ex;
		} catch (NoUniqueObjectException ex) {
			throw ex;
		} catch (ManyObjectFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			success = false;
			ex.printStackTrace();
			db.rollback();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return success;
	}

	@Override
	public boolean merge(List<T> models) throws SaveException, UpdateException,
			NoUniqueObjectException, ManyObjectFoundException {
		// TODO Auto-generated method stub
		boolean success = false;
		ObjectContainer db = this.open();
		try {
			for (T t : models) {

				ObjectSet<T> set = this.getQueryToUniqueObject(t, db).execute();
				int size = set.size();

				if (size == 0) {
					// não existe um objeto no banco
					if (!this.save(t, db, false)) {
						throw new SaveException("Impossível salvar o objeto! "
								+ t);
					}
				} else if (size == 1) {
					// existe o objeto no banco, então ele deve atualizá-lo
					if (!this.update(set.get(0), t, db)) {
						throw new UpdateException(
								"Falha ao atualizar o objeto! " + t);
					}
				} else {
					throw new ManyObjectFoundException(
							"Existe mais de um objeto para " + t);
				}
			}
			db.commit();
			success = true;
		} catch (SaveException ex) {
			throw ex;
		} catch (UpdateException ex) {
			throw ex;
		} catch (NoUniqueObjectException ex) {
			throw ex;
		} catch (ManyObjectFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			db.rollback();
		} finally {
			if (db != null) {
				db.close();
			}
		}

		return success;
	}

	@Override
	public boolean update(List<T> models) throws UpdateException,
			NotFoundObjectException, ManyObjectFoundException {
		// TODO Auto-generated method stub
		boolean success = false;
		ObjectContainer db = this.open();
		try {
			for (T t : models) {
				if (!this.update(t, db)) {
					throw new UpdateException(
							"Falha ana atualização do objeto!" + t);
				}
			}
			db.commit();
			success = true;
		} catch (UpdateException ex) {
			throw ex;
		} catch (NotFoundObjectException ex) {
			throw ex;
		} catch (ManyObjectFoundException ex) {
			throw ex;
		} catch (Exception e) {
			// TODO: handle exception
			db.rollback();
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return success;
	}

	/**
	 * @return the classModel
	 */
	protected Class<T> getClassModel() {
		return classModel;
	}

	@Override
	public boolean delete(T model) {
		// TODO Auto-generated method stub
		boolean success = false;
		ObjectContainer db = this.open();

		try {
			ObjectSet<T> set = this.getQueryToUniqueObject(model, db).execute();
			if (set.size() == 1) {
				db.delete(set.get(0));
			} else if (set.size() > 1) {
				throw new ManyObjectFoundException(
						"Mais de um objeto encontrado para " + model);
			} else {
				throw new NotFoundObjectException(
						"Nenhum objeto encontrado no banco de dados para "
								+ model);
			}
			db.commit();
			success = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			db.rollback();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return success;
	}

	@Override
	public boolean deleteAll() {
		// TODO Auto-generated method stub
		boolean success = false;
		ObjectContainer db = this.open();
		try {
			ObjectSet<T> all = db.query(this.getClassModel());

			for (T t : all) {
				db.delete(t);
			}
			db.commit();
			success = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			db.rollback();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return success;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public T findByExemple(T model) {
		T t = null;
		ObjectContainer db = this.open();

		try {
			ObjectSet<T> set = db.queryByExample(model);
			if (set.size() == 1) {
				t = set.get(0);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return t;
	}

	/**
	 * Busca todos os modelos repassados em models, mas agora são gerenciados
	 * pelo container (db).
	 * @param models
	 * @param db
	 * @return
	 * @throws ManyObjectFoundException
	 * @throws NotFoundObjectException
	 */
	protected Collection<T> findAll(Collection<T> models,ObjectContainer db) throws ManyObjectFoundException, NotFoundObjectException{
		List<T> list=new ArrayList<T>();
		
		for (T t : models) {
			list.add(this.find(t, db));
		}
		
		return list;
	}

	/**
	 * 
	 * @param model
	 * @param db
	 * @return
	 * @throws ManyObjectFoundException
	 * @throws NotFoundObjectException
	 */
	protected T find(T model, ObjectContainer db) throws ManyObjectFoundException, NotFoundObjectException{
		T retrieved=this.getModel(model, db);
		if (retrieved != null){
			return retrieved;
		}
		throw new NotFoundObjectException(model.label()+" não foi encontrado no banco de dados!");
	}
	
	/**
	 * Retornar o modelo da DAO a partir do Container passado. Se não
	 * encontrar o modelo, retorna null, caso contrário a exceção
	 * ManyObjectsFoundException será lançada.
	 * 
	 * @param model
	 * @param db
	 * @return
	 */
	protected  T getModel(T model, ObjectContainer db)
			throws ManyObjectFoundException{
		ObjectSet<T> l = this.getQueryToUniqueObject(model, db).execute();
		int size = l.size();

		if (size == 1) {
			return l.get(0);
		} else if (size == 0) {
			return null;
		} else {
			throw new ManyObjectFoundException(
					"Mais de um modelo encontrado para " + model);
		}
	}
	/**
	 * Salva o modelo caso não esteja cadastrado no banco de dados, caso contrário
	 * atualiza o modelo.
	 * @param model modelo a ser salvo ou atualizado
	 * @param db container do banco de daods
	 * @return true se tudo ocorrer bem, caso contrário false
	 * @throws ManyObjectFoundException
	 * @throws SaveException
	 * @throws NoUniqueObjectException
	 * @throws UpdateException
	 * @throws NotFoundObjectException
	 */
	protected boolean merge(T model,ObjectContainer db) throws ManyObjectFoundException, SaveException, NoUniqueObjectException, UpdateException, NotFoundObjectException{
		T modelRetrieved=this.getModel(model, db);
		if (modelRetrieved== null){
			return this.save(model, db, false);
		}
		else{
			return this.update(modelRetrieved, model, db);
		}
	}
	/**
	 * 
	 * @param model
	 * @param db
	 * @return
	 */
	protected  boolean exists(T model, ObjectContainer db){
		ObjectSet<T> l = this.getQueryToUniqueObject(model, db).execute();
		return  l.size() > 0;
	}
	
	/**
	 * Atualiza o modelo. Esse método deve ser sobrescrito pelas classes filhas caso
	 * o modelo seja formado por agregação ou composição de outros
	 * modelos.
	 * @param modelReceived
	 *            Objeto com os dados a serem atualizados
	 * @param db
	 *            Container atual
	 * @return true se tudo ocorrer bem, caso contrário retorna false
	 * @throws UpdateException
	 *             Caso algum erro inesperado aconteça
	 * @throws ManyObjectFoundException
	 *             Se possui atributos que são objetos armazenados no banco,
	 *             então lança essa exceção caso possua mais de objetos iguais
	 *             armazenados no banco. Deve-se levar em consideração a
	 *             implementação de objetos iguais.
	 * @throws NotFoundObjectException
	 *             Mesmo caso acima, porém o objeto não foi encontrado
	 */
	protected boolean update(T modelReceived, ObjectContainer db)
			throws UpdateException, ManyObjectFoundException,
			NotFoundObjectException{
		T modelRetrieved=this.find(modelReceived, db);
		return this.update(modelRetrieved, modelReceived, db);
	}

	/**
	 * Atualiza o modelo (modelReceived) com base no modelo gerenciado (modelRetreived)
	 * no container (db). Esse método deve ser sobrescrito pelas classes filhas caso
	 * o modelo seja formado por composição ou agregação de outros modelos.
	 * @param modelRetrieved
	 *            objeto recuperado do banco de dados, e que está ativo para o
	 *            container atual
	 * @param modelReceived
	 *            Objeto com os dados a serem atualizados
	 * @param db
	 *            Container atual
	 * @return true se tudo ocorrer bem, caso contrário retorna false
	 * @throws UpdateException
	 *             Caso algum erro inesperado aconteça
	 * @throws ManyObjectFoundException
	 *             Se possui atributos que são objetos armazenados no banco,
	 *             então lança essa exceção caso possua mais de objetos iguais
	 *             armazenados no banco. Deve-se levar em consideração a
	 *             implementação de objetos iguais.
	 * @throws NotFoundObjectException
	 *             Mesmo caso acima, porém o objeto não foi encontrado
	 */
	protected boolean update(T modelRetrieved, T modelReceived,
			ObjectContainer db) throws UpdateException,
			ManyObjectFoundException, NotFoundObjectException{
		
		modelRetrieved.copyAttributesOf(modelReceived);
		db.store(modelRetrieved);
		return true;
	}

	/**
	 * Salva um objeto no banco de dados.Esse método deve ser sobrescrito pelas classes filhas caso
	 * o modelo seja formado por composição ou agregação de outros modelos.
	 * @param model
	 * @param db
	 * @return true se nenhum erro ocorreu
	 * @throws SaveException
	 *             se ocorrer algum erro não identificado
	 * @throws ManyObjectFoundException
	 *             Atributos que são objetos armazenados no banco, mas possuem
	 *             mais de uma referência para o mesmo, levando-se em
	 *             consideração a impplementação do desenvolver do que vem a ser
	 *             dois objetos iguais.
	 * @throws NoUniqueObjectException quando checkExist é true e
	 * já existe um modelo igual já cadastrado.
	 */
	protected boolean save(T model, ObjectContainer db,
			boolean checkExist) throws SaveException, ManyObjectFoundException,
			NoUniqueObjectException{
		if (checkExist){
			if (this.exists(model, db)){
				throw new NoUniqueObjectException(model.getClass().getName()+" já está cadastrado "+model.label());
			}
		}
		
		db.store(model);
		return true;
	}

	/**
	 * Deve retornar uma Query para consulta onda as retrições aplicadas sejam
	 * para retornar somente um objeto, ou seja, as retrições devem ser baseadas nos
	 * mesmos atributos que os métodos equals() e hash().
	 * 
	 * @param model
	 * @param db
	 * @return
	 */
	protected abstract Query getQueryToUniqueObject(T model, ObjectContainer db);

	

	protected class PredicateForHashCode<T2> extends Predicate<T2> {

		private T2 object;

		public PredicateForHashCode(T2 object) {
			super();
			this.object = object;
		}

		@Override
		public boolean match(T2 arg0) {
			// TODO Auto-generated method stub
			return this.object.equals(arg0);
		}

	}
}
