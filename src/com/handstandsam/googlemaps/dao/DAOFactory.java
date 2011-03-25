package com.handstandsam.googlemaps.dao;

import com.googlecode.objectify.ObjectifyFactory;

public class DAOFactory {

	private static ObjectifyFactory fact = null;

	/**
	 * Required if going to override the {@link ObjectifyFactory}, required for
	 * testing
	 * 
	 * @param factory
	 */
	public static void setFactory(ObjectifyFactory factory) {
		fact = factory;
	}

	public static DAO getDAO() {
		return new DAOImpl(fact);
	}

}
