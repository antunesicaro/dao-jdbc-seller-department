package model.dao;

import java.util.List;

import model.entities.Department;

/*	tenho os m�todos aqui na interface que v�o ser declarados e usados de outro lugar da aplica��o
 * Em uma interface, os m�todos s�o declarados sem implementa��o, ou seja, eles n�o possuem um corpo. A declara��o dos m�todos � feita apenas com a assinatura do m�todo
 * */
public interface DepartmentDao {
	void insert(Department obj); //opera��o respons�vel por inserir no banco de dados o objeto q eu enviar como parametro de entrada
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id); // acha um departamento pelo id e o retorna um Department 
	List<Department> findAll();  // encontrar e retornar todos.. por�m retorna no formato de list de departments
}
