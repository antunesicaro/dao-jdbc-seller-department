package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

//explicação detalhada da interface em DepartmentDao, aqui é apenas repetição.
public interface SellerDao {
	void insert(Seller obj); 
	void update(Seller obj);
	void deleteById(Integer id);
	Seller findById(Integer id);  
	List<Seller> findAll(); 
	
	//buscar por departamento
	List<Seller> findByDepartment(Department department); //recebe aqui do program e é implementado pelo sellerdaojdbc, dai tem acesso aos dados daqui
}
