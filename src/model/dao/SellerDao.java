package model.dao;

import java.util.List;

import model.entities.Seller;

//explica��o detalhada da interface em DepartmentDao, aqui � apenas repeti��o.
public interface SellerDao {
	void insert(Seller obj); 
	void update(Seller obj);
	void deleteById(Integer id);
	Seller findById(Integer id);  
	List<Seller> findAll(); 
}
