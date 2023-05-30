package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

/*
 * Atualmente, esses m�todos est�o retornando null ou n�o fazendo nada. � necess�rio adicionar a l�gica real para interagir com o banco de dados e executar as opera��es desejadas.

Essa classe serve como um esqueleto para a implementa��o real do acesso ao banco de dados usando JDBC (Java Database Connectivity).
 * */
public class SellerDaoJDBC implements SellerDao {

	
	private Connection conn;
	
	//for�ar inje��o de depend�ncia
	//O objetivo � separar a cria��o e configura��o das depend�ncias das classes que as utilizam
	//fa�o isso com construtor
	//agora esse objeto conn tenho dispnibilzado em qualquer lugar da classe SellerDaoJDBc
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn; //o this.conn recebe o conn que chegou como par�metro
	}
	
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
		//n�o precisa de conex�o, o Conecction, pois o Dao j� vai ter.
		PreparedStatement st = null;  //instancia para ter o m�todo de fazer consulta parametrizada
		ResultSet rs = null; //instacia para ter m�todo de percorrer o resultado
		
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			st.setInt(1, id);//primeiro ponto de interrega��o � substituido pelo id
			rs = st.executeQuery(); //resultado fica aqui pois pede pra executar o comando
			
			//obs: ResultSet tr�s os dados em formato de tabela, � um objeto com linhas  e colunas, por�m
			//a classe dao � respons�vel por pegar os dados do banco, no caso da consulta q retorna uma tabela e 
			//TRANSFORMAR EM OBJETOs >> associados << 
			//rs aponta pra posi��o 0 que n�o cont�m objeto de come�o, isto �, logo depois q query � executada
			if(rs.next()) { //testar se veio algum resultado, se a consulta n�o retorno nenhum registro, o if vai dar false e vai pular para retornar nulo
				
				//come�o recebendo o department
				//instaciamos um novo departamento e setamos valores pra eles de acordo com o resultado obtido do banco
				Department dep = instantiateDepartment(rs);//isso � feito no m�todo... passo pra esse m�todo o rs, que s�o os resultados da consulta q est�o sendo percorridos
				
				
				
				//agora o objeto seller, apontando para o departamento e com os dados do banco 
				Seller obj = instantiateSeller(rs,dep); //mesma coisa, por�m agora tamb�m passo o dep em quest�o instanciado
				
				return obj; //retorno o seller
			}
			
			return null;
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			//fechar recurso mas n fecha conex�o, deixa funcionando.. o mesmo objeto dao pode fazer outra opera��o, deixa pra fechar no programa a conex�o
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		//cuidado aqui na rela��o
		//o departamento associado com o seller n�o � direto o id
		//no Seller.java no model.entities, ela tem um atributo department que � do tipo Department , dai preciso pegar o obj inteiro, pois quero a referencia do id , e n�o apenas o id, q tenha tb o name, e isso j� temos
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep =  new Department(); //instancia a entidade Department e tem acesso aos m�todos q est�o nele
		
		dep.setId(rs.getInt("DepartmentId")); //mudo o id desse instanciado pegando o resultado da consulta atual
		
		dep.setName(rs.getString("DepName"));
		
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
