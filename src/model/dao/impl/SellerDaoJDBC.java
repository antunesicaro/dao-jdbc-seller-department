package model.dao.impl;

import java.util.List;

import model.dao.SellerDao;
import model.entities.Seller;

/*
 * Atualmente, esses m�todos est�o retornando null ou n�o fazendo nada. � necess�rio adicionar a l�gica real para interagir com o banco de dados e executar as opera��es desejadas.

Essa classe serve como um esqueleto para a implementa��o real do acesso ao banco de dados usando JDBC (Java Database Connectivity).
 * */
public class SellerDaoJDBC implements SellerDao {

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
