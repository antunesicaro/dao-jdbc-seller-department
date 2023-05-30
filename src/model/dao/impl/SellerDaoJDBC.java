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
 * Atualmente, esses métodos estão retornando null ou não fazendo nada. É necessário adicionar a lógica real para interagir com o banco de dados e executar as operações desejadas.

Essa classe serve como um esqueleto para a implementação real do acesso ao banco de dados usando JDBC (Java Database Connectivity).
 * */
public class SellerDaoJDBC implements SellerDao {

	
	private Connection conn;
	
	//forçar injeção de dependência
	//O objetivo é separar a criação e configuração das dependências das classes que as utilizam
	//faço isso com construtor
	//agora esse objeto conn tenho dispnibilzado em qualquer lugar da classe SellerDaoJDBc
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn; //o this.conn recebe o conn que chegou como parâmetro
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
		//não precisa de conexão, o Conecction, pois o Dao já vai ter.
		PreparedStatement st = null;  //instancia para ter o método de fazer consulta parametrizada
		ResultSet rs = null; //instacia para ter método de percorrer o resultado
		
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			st.setInt(1, id);//primeiro ponto de interregação é substituido pelo id
			rs = st.executeQuery(); //resultado fica aqui pois pede pra executar o comando
			
			//obs: ResultSet trás os dados em formato de tabela, é um objeto com linhas  e colunas, porém
			//a classe dao é responsável por pegar os dados do banco, no caso da consulta q retorna uma tabela e 
			//TRANSFORMAR EM OBJETOs >> associados << 
			//rs aponta pra posição 0 que não contém objeto de começo, isto é, logo depois q query é executada
			if(rs.next()) { //testar se veio algum resultado, se a consulta não retorno nenhum registro, o if vai dar false e vai pular para retornar nulo
				
				//começo recebendo o department
				//instaciamos um novo departamento e setamos valores pra eles de acordo com o resultado obtido do banco
				Department dep = instantiateDepartment(rs);//isso é feito no método... passo pra esse método o rs, que são os resultados da consulta q estão sendo percorridos
				
				
				
				//agora o objeto seller, apontando para o departamento e com os dados do banco 
				Seller obj = instantiateSeller(rs,dep); //mesma coisa, porém agora também passo o dep em questão instanciado
				
				return obj; //retorno o seller
			}
			
			return null;
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			//fechar recurso mas n fecha conexão, deixa funcionando.. o mesmo objeto dao pode fazer outra operação, deixa pra fechar no programa a conexão
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
		//cuidado aqui na relação
		//o departamento associado com o seller não é direto o id
		//no Seller.java no model.entities, ela tem um atributo department que é do tipo Department , dai preciso pegar o obj inteiro, pois quero a referencia do id , e não apenas o id, q tenha tb o name, e isso já temos
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep =  new Department(); //instancia a entidade Department e tem acesso aos métodos q estão nele
		
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
