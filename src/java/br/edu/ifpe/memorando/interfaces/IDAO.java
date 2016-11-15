package br.edu.ifpe.memorando.interfaces;

import java.util.List;

import br.edu.ifpe.memorando.exception.ManyObjectFoundException;
import br.edu.ifpe.memorando.exception.NoUniqueObjectException;
import br.edu.ifpe.memorando.exception.NotFoundObjectException;
import br.edu.ifpe.memorando.exception.SaveException;
import br.edu.ifpe.memorando.exception.UpdateException;
import br.edu.ifpe.memorando.models.IModel;

public interface IDAO<T extends IModel> {

	
public List<T> findAllWithPagination(int limit, int offset);
	
	public int count();
	/**
	 * Armazena um novo objeto no banco de dados.
	 * Pode-se verificar se ela já existe ou não
	 * @param model
	 * @param checkExist caso seja true vai verificar se o objeto existe
	 * @return true se o modelo for armazenado com sucesso
	 */
	public boolean save(T model,boolean checkExist) throws SaveException,NoUniqueObjectException,ManyObjectFoundException;
	
	/**
	 * Aramazena um novo objeto caso ele ainda não exista, caso contrário atualizará-lo.
	 * @param model
	 * @return true se conseguir inserir ou atualizar o objeto
	 */
	public boolean merge(T model) throws SaveException,UpdateException,NoUniqueObjectException,ManyObjectFoundException;
	
	/**
	 * Atualiza um objeto caso ele exista
	 * @param model
	 * @return
	 */
	public boolean update(T model) throws UpdateException,NotFoundObjectException,ManyObjectFoundException;
	
	/**
	 * Retorna todos os objetos
	 * @return
	 */
	public List<T> findAll();
	
	/**
	 * Retorna um objeto em específico. 
	 * Caso não seja encontrado retorna null ou se
	 * tiver mais de um objeto que satisfaça a condição lança uma exceção 
	 * @param model
	 * @return 
	 */
	public T find(T model) throws ManyObjectFoundException;
	
	public boolean delete(T model);
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	public T findByHashCode(T model) throws ManyObjectFoundException;
	
	public T findByExemple(T model) throws ManyObjectFoundException;
	
	public List<T> findAll(T model);
	
	public boolean save(List<T> models,boolean checkExist)  throws SaveException,NoUniqueObjectException,ManyObjectFoundException;
	
	public boolean merge(List<T> models) throws SaveException,UpdateException,NoUniqueObjectException,ManyObjectFoundException;
	
	public boolean update(List<T> models) throws UpdateException,NotFoundObjectException,ManyObjectFoundException;
	
	public boolean deleteAll();
	
}
