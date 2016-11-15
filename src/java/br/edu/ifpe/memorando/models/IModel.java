package br.edu.ifpe.memorando.models;

import java.io.Serializable;



public abstract class IModel<T> implements Comparable<IModel>, Serializable {

	/**
	 * Copia os atributos do objeto passado para ele mesmo.
	 * 
	 * @param copy
	 */
	public abstract void copyAttributesOf(T copy);

	/**
	 * Retira os valores dos atributos
	 */
	public abstract void unsetAttributes();

	/**
	 * Todos os atributos do tipo String devem ficar null ou vazios caso possuam
	 * como valor a string "null". Deve-se usar esse método quando os dados
	 * utilizados para popular o objeto em questão são oriundos do formato JSON.
	 */
	public abstract void cleanStringWithNull();

	public abstract String label();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(IModel another) {
		// TODO Auto-generated method stub
		if (another != null) {
			return this.label().compareToIgnoreCase(another.label());
		}
		return 0;
	}
}
