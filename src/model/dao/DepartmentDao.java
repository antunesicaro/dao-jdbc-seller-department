package model.dao;

import java.util.List;

import model.entities.Department;

/*	tenho os métodos aqui na interface que vão ser declarados e usados de outro lugar da aplicação
 * Em uma interface, os métodos são declarados sem implementação, ou seja, eles não possuem um corpo. A declaração dos métodos é feita apenas com a assinatura do método
 * */
public interface DepartmentDao {
	void insert(Department obj); //operação responsável por inserir no banco de dados o objeto q eu enviar como parametro de entrada
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id); // acha um departamento pelo id e o retorna um Department 
	List<Department> findAll();  // encontrar e retornar todos.. porém retorna no formato de list de departments
}
