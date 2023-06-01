package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	//obs: explica��es melhores e completas no m�todo finbyId e findByDepartment
	@Override
	public void insert(Seller obj) {
			
		PreparedStatement st = null; 
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+"VALUES " 
					+"(?, ?, ?, ?, ?) " , Statement.RETURN_GENERATED_KEYS ); //vou precisar do retorno do objeto inserido, no caso o id de quem foi inserido novo, que � a chave prim�ria gerada.
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();// � chamado para executar a instru��o sql e ela t� retornando a chave prim�ria
			
			//se for maior que zero, � que inseriu, linhas foram modificadas no banco
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys(); //recebe a chave
				if(rs.next()) { //se existir 
					int id = rs.getInt(1); //pega a primeira coluna da chave generatedkeys
					
					//popula o objeto que veio como parametro, pois o id eu n�o mando, ele � autom�tico
					obj.setId(id);
				}
				
				DB.closeResultSet(rs); //fecha para n�o vazar mem�ria
			}
			
			else {
				throw new DbException("Erro inesperado, nenhuma linha afetada");
			}
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
			
		
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
		//n�o precisa de conex�o, o Conecction, pois o Dao j� vai ter.
				PreparedStatement st = null;  //instancia para ter o m�todo de fazer consulta parametrizada
				ResultSet rs = null; //instacia para ter m�todo de percorrer o resultado
				
				try {
					st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
							+ "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id "
							+ "ORDER BY Name");
					
					rs = st.executeQuery(); //resultado da query fica aqui pois pede pra executar ela, igual no mysql, quando executo ele vai retornar os valores, esses valores agora est�o em rs
					//obs: ResultSet tr�s os dados em formato de tabela, � um objeto com linhas  e colunas, por�m
					//a classe dao � respons�vel por pegar os dados do banco, no caso da consulta q retorna uma tabela e 
					//TRANSFORMAR EM OBJETOs >> associados << 
					//rs aponta pra posi��o 0 que n�o cont�m objeto de come�o, isto �, logo depois q query � executada
					
					//v�rios valores eu crio uma lista
					List<Seller> list = new ArrayList<>();
					
					//preciso controlar a n�o repeti��o de departamento, pois todos resultados precisam estar linkados a uma mesma instancia��o de departamento
					//pra isso vou usar o map, onde vou poder declarar uma chave e um valor,lembrar q map n�o aceita repeti��es de chave
					//t� vazio esse map q criei
					Map<Integer,Department> map = new HashMap<>(); // declaro um map e digo q sua chave vai ser um integer, q � o id do departamento, e o  valor � um objeto do tipo Department. sobre o hashmap: O HashMap � uma tabela de dispers�o que permite um acesso r�pido aos elementos com base na chave fornecida...evitar a duplica��o de inst�ncias do objeto Department para o mesmo ID de departamento. Nesse caso, o Map � definido para ter chaves do tipo Integer (representando o ID do departamento) e valores do tipo Department. checar no console q realemnte department = Department com values
					
					while(rs.next()) { //agora, o resultado pode ter 0 ou mais valores, ent�o vai ter q ser o while  e n�o o if, pois o while diz que enquanto tiver resultado, vai percorrendo e salvando. 
							
						//testar se o departamento j� existe
						//criei acima um map vazio e vou guardar dentro do map qualqur departamento q eu instaciar aqui em baixo
						//cada vez que passar no while, vai ver se o departamento j� existe, pra isso...
						//vai no map e tenta buscar com o m�todo get um departamento que tem o id q est� em (rs.getInt("DepartmentId")) q � justamente um id q pode estar no resultado da consulta... busca dentro do map se j� tem algum departamento com o id q foi fornecido l� no program, se n�o existir, retorna nulo pra Department dep... ai sim se for nulo, eu instacio o departamento... assim garanto q n�o v�o ter v�rios departamentos de mesma chave instaciados
						//se o dep j� existir, o map pega ele, ai o if d� falso e reaproveito o dep q j� existia para pegar os dados 
						Department dep = map.get(rs.getInt("DepartmentId"));//map.get vai buscar o valor no mapa,esse valor vai ser o valor recuperado DepartmentId da linha que est� sendo percorrida e salva no resultSET... � como se tivesse o id do dep e o map t� buscando isso
						
						if(dep == null) { //explicado acima, se for nulo � pq n tem, ai a precisa instanciar o dep
							dep = instantiateDepartment(rs);
							//salvo dentro do map, pra q da proxima vez q for passado o id no program, por exemplo, n�o precisar instanciar e j� ter salvo na lista
							map.put(rs.getInt("DepartmentId"), dep); //chave � o departmentid passado e o valor � o objeto dep
						}
						
						
						//pra cada valor do rs q t� sendo percorrido com while, instancio o departamento, o vendedor e depois adicionar o vendedor na lista
						//agora o objeto seller, apontando para o departamento e com os dados do banco 
						Seller obj = instantiateSeller(rs,dep); //mesma coisa, por�m agora tamb�m passo o dep em quest�o instanciado
						
						list.add(obj);
						
						//agora sim tenho o mesmo departamento e seus respectivos vendedores apontando
						
					}
					
					return list;
					
				}catch(SQLException e){
					throw new DbException(e.getMessage());
				}
				finally {
					//fechar recurso mas n fecha conex�o, deixa funcionando.. o mesmo objeto dao pode fazer outra opera��o, deixa pra fechar no programa a conex�o
					DB.closeStatement(st);
					DB.closeResultSet(rs);
				}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		//n�o precisa de conex�o, o Conecction, pois o Dao j� vai ter.
		PreparedStatement st = null;  //instancia para ter o m�todo de fazer consulta parametrizada
		ResultSet rs = null; //instacia para ter m�todo de percorrer o resultado
		
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, department.getId());//primeiro ponto de interrega��o � substituido pelo id do departamento q foi passado no argumento ali em cima no findbydepartment
			rs = st.executeQuery(); //resultado da query fica aqui pois pede pra executar ela, igual no mysql, quando executo ele vai retornar os valores, esses valores agora est�o em rs
			//obs: ResultSet tr�s os dados em formato de tabela, � um objeto com linhas  e colunas, por�m
			//a classe dao � respons�vel por pegar os dados do banco, no caso da consulta q retorna uma tabela e 
			//TRANSFORMAR EM OBJETOs >> associados << 
			//rs aponta pra posi��o 0 que n�o cont�m objeto de come�o, isto �, logo depois q query � executada
			
			//v�rios valores eu crio uma lista
			List<Seller> list = new ArrayList<>();
			
			//preciso controlar a n�o repeti��o de departamento, pois todos resultados precisam estar linkados a uma mesma instancia��o de departamento
			//pra isso vou usar o map, onde vou poder declarar uma chave e um valor,lembrar q map n�o aceita repeti��es de chave
			//t� vazio esse map q criei
			Map<Integer,Department> map = new HashMap<>(); // declaro um map e digo q sua chave vai ser um integer, q � o id do departamento, e o  valor � um objeto do tipo Department. sobre o hashmap: O HashMap � uma tabela de dispers�o que permite um acesso r�pido aos elementos com base na chave fornecida...evitar a duplica��o de inst�ncias do objeto Department para o mesmo ID de departamento. Nesse caso, o Map � definido para ter chaves do tipo Integer (representando o ID do departamento) e valores do tipo Department. checar no console q realemnte department = Department com values
			
			while(rs.next()) { //agora, o resultado pode ter 0 ou mais valores, ent�o vai ter q ser o while  e n�o o if, pois o while diz que enquanto tiver resultado, vai percorrendo e salvando. 
					
				//testar se o departamento j� existe
				//criei acima um map vazio e vou guardar dentro do map qualqur departamento q eu instaciar aqui em baixo
				//cada vez que passar no while, vai ver se o departamento j� existe, pra isso...
				//vai no map e tenta buscar com o m�todo get um departamento que tem o id q est� em (rs.getInt("DepartmentId")) q � justamente um id q pode estar no resultado da consulta... busca dentro do map se j� tem algum departamento com o id q foi fornecido l� no program, se n�o existir, retorna nulo pra Department dep... ai sim se for nulo, eu instacio o departamento... assim garanto q n�o v�o ter v�rios departamentos de mesma chave instaciados
				//se o dep j� existir, o map pega ele, ai o if d� falso e reaproveito o dep q j� existia para pegar os dados 
				Department dep = map.get(rs.getInt("DepartmentId"));//map.get vai buscar o valor no mapa,esse valor vai ser o valor recuperado DepartmentId da linha que est� sendo percorrida e salva no resultSET... � como se tivesse o id do dep e o map t� buscando isso
				
				if(dep == null) { //explicado acima, se for nulo � pq n tem, ai a precisa instanciar o dep
					dep = instantiateDepartment(rs);
					//salvo dentro do map, pra q da proxima vez q for passado o id no program, por exemplo, n�o precisar instanciar e j� ter salvo na lista
					map.put(rs.getInt("DepartmentId"), dep); //chave � o departmentid passado e o valor � o objeto dep
				}
				
				
				//pra cada valor do rs q t� sendo percorrido com while, instancio o departamento, o vendedor e depois adicionar o vendedor na lista
				//agora o objeto seller, apontando para o departamento e com os dados do banco 
				Seller obj = instantiateSeller(rs,dep); //mesma coisa, por�m agora tamb�m passo o dep em quest�o instanciado
				
				list.add(obj);
				
				//agora sim tenho o mesmo departamento e seus respectivos vendedores apontando
				
			}
			
			return list;
			
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			//fechar recurso mas n fecha conex�o, deixa funcionando.. o mesmo objeto dao pode fazer outra opera��o, deixa pra fechar no programa a conex�o
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
