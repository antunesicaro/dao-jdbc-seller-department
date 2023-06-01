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
	
	//obs: explicações melhores e completas no método finbyId e findByDepartment
	@Override
	public void insert(Seller obj) {
			
		PreparedStatement st = null; 
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+"VALUES " 
					+"(?, ?, ?, ?, ?) " , Statement.RETURN_GENERATED_KEYS ); //vou precisar do retorno do objeto inserido, no caso o id de quem foi inserido novo, que é a chave primária gerada.
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();// é chamado para executar a instrução sql e ela tá retornando a chave primária
			
			//se for maior que zero, é que inseriu, linhas foram modificadas no banco
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys(); //recebe a chave
				if(rs.next()) { //se existir 
					int id = rs.getInt(1); //pega a primeira coluna da chave generatedkeys
					
					//popula o objeto que veio como parametro, pois o id eu não mando, ele é automático
					obj.setId(id);
				}
				
				DB.closeResultSet(rs); //fecha para não vazar memória
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
		//não precisa de conexão, o Conecction, pois o Dao já vai ter.
				PreparedStatement st = null;  //instancia para ter o método de fazer consulta parametrizada
				ResultSet rs = null; //instacia para ter método de percorrer o resultado
				
				try {
					st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
							+ "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id "
							+ "ORDER BY Name");
					
					rs = st.executeQuery(); //resultado da query fica aqui pois pede pra executar ela, igual no mysql, quando executo ele vai retornar os valores, esses valores agora estão em rs
					//obs: ResultSet trás os dados em formato de tabela, é um objeto com linhas  e colunas, porém
					//a classe dao é responsável por pegar os dados do banco, no caso da consulta q retorna uma tabela e 
					//TRANSFORMAR EM OBJETOs >> associados << 
					//rs aponta pra posição 0 que não contém objeto de começo, isto é, logo depois q query é executada
					
					//vários valores eu crio uma lista
					List<Seller> list = new ArrayList<>();
					
					//preciso controlar a não repetição de departamento, pois todos resultados precisam estar linkados a uma mesma instanciação de departamento
					//pra isso vou usar o map, onde vou poder declarar uma chave e um valor,lembrar q map não aceita repetições de chave
					//tá vazio esse map q criei
					Map<Integer,Department> map = new HashMap<>(); // declaro um map e digo q sua chave vai ser um integer, q é o id do departamento, e o  valor é um objeto do tipo Department. sobre o hashmap: O HashMap é uma tabela de dispersão que permite um acesso rápido aos elementos com base na chave fornecida...evitar a duplicação de instâncias do objeto Department para o mesmo ID de departamento. Nesse caso, o Map é definido para ter chaves do tipo Integer (representando o ID do departamento) e valores do tipo Department. checar no console q realemnte department = Department com values
					
					while(rs.next()) { //agora, o resultado pode ter 0 ou mais valores, então vai ter q ser o while  e não o if, pois o while diz que enquanto tiver resultado, vai percorrendo e salvando. 
							
						//testar se o departamento já existe
						//criei acima um map vazio e vou guardar dentro do map qualqur departamento q eu instaciar aqui em baixo
						//cada vez que passar no while, vai ver se o departamento já existe, pra isso...
						//vai no map e tenta buscar com o método get um departamento que tem o id q está em (rs.getInt("DepartmentId")) q é justamente um id q pode estar no resultado da consulta... busca dentro do map se já tem algum departamento com o id q foi fornecido lá no program, se não existir, retorna nulo pra Department dep... ai sim se for nulo, eu instacio o departamento... assim garanto q não vão ter vários departamentos de mesma chave instaciados
						//se o dep já existir, o map pega ele, ai o if dá falso e reaproveito o dep q já existia para pegar os dados 
						Department dep = map.get(rs.getInt("DepartmentId"));//map.get vai buscar o valor no mapa,esse valor vai ser o valor recuperado DepartmentId da linha que está sendo percorrida e salva no resultSET... é como se tivesse o id do dep e o map tá buscando isso
						
						if(dep == null) { //explicado acima, se for nulo é pq n tem, ai a precisa instanciar o dep
							dep = instantiateDepartment(rs);
							//salvo dentro do map, pra q da proxima vez q for passado o id no program, por exemplo, não precisar instanciar e já ter salvo na lista
							map.put(rs.getInt("DepartmentId"), dep); //chave é o departmentid passado e o valor é o objeto dep
						}
						
						
						//pra cada valor do rs q tá sendo percorrido com while, instancio o departamento, o vendedor e depois adicionar o vendedor na lista
						//agora o objeto seller, apontando para o departamento e com os dados do banco 
						Seller obj = instantiateSeller(rs,dep); //mesma coisa, porém agora também passo o dep em questão instanciado
						
						list.add(obj);
						
						//agora sim tenho o mesmo departamento e seus respectivos vendedores apontando
						
					}
					
					return list;
					
				}catch(SQLException e){
					throw new DbException(e.getMessage());
				}
				finally {
					//fechar recurso mas n fecha conexão, deixa funcionando.. o mesmo objeto dao pode fazer outra operação, deixa pra fechar no programa a conexão
					DB.closeStatement(st);
					DB.closeResultSet(rs);
				}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		//não precisa de conexão, o Conecction, pois o Dao já vai ter.
		PreparedStatement st = null;  //instancia para ter o método de fazer consulta parametrizada
		ResultSet rs = null; //instacia para ter método de percorrer o resultado
		
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, department.getId());//primeiro ponto de interregação é substituido pelo id do departamento q foi passado no argumento ali em cima no findbydepartment
			rs = st.executeQuery(); //resultado da query fica aqui pois pede pra executar ela, igual no mysql, quando executo ele vai retornar os valores, esses valores agora estão em rs
			//obs: ResultSet trás os dados em formato de tabela, é um objeto com linhas  e colunas, porém
			//a classe dao é responsável por pegar os dados do banco, no caso da consulta q retorna uma tabela e 
			//TRANSFORMAR EM OBJETOs >> associados << 
			//rs aponta pra posição 0 que não contém objeto de começo, isto é, logo depois q query é executada
			
			//vários valores eu crio uma lista
			List<Seller> list = new ArrayList<>();
			
			//preciso controlar a não repetição de departamento, pois todos resultados precisam estar linkados a uma mesma instanciação de departamento
			//pra isso vou usar o map, onde vou poder declarar uma chave e um valor,lembrar q map não aceita repetições de chave
			//tá vazio esse map q criei
			Map<Integer,Department> map = new HashMap<>(); // declaro um map e digo q sua chave vai ser um integer, q é o id do departamento, e o  valor é um objeto do tipo Department. sobre o hashmap: O HashMap é uma tabela de dispersão que permite um acesso rápido aos elementos com base na chave fornecida...evitar a duplicação de instâncias do objeto Department para o mesmo ID de departamento. Nesse caso, o Map é definido para ter chaves do tipo Integer (representando o ID do departamento) e valores do tipo Department. checar no console q realemnte department = Department com values
			
			while(rs.next()) { //agora, o resultado pode ter 0 ou mais valores, então vai ter q ser o while  e não o if, pois o while diz que enquanto tiver resultado, vai percorrendo e salvando. 
					
				//testar se o departamento já existe
				//criei acima um map vazio e vou guardar dentro do map qualqur departamento q eu instaciar aqui em baixo
				//cada vez que passar no while, vai ver se o departamento já existe, pra isso...
				//vai no map e tenta buscar com o método get um departamento que tem o id q está em (rs.getInt("DepartmentId")) q é justamente um id q pode estar no resultado da consulta... busca dentro do map se já tem algum departamento com o id q foi fornecido lá no program, se não existir, retorna nulo pra Department dep... ai sim se for nulo, eu instacio o departamento... assim garanto q não vão ter vários departamentos de mesma chave instaciados
				//se o dep já existir, o map pega ele, ai o if dá falso e reaproveito o dep q já existia para pegar os dados 
				Department dep = map.get(rs.getInt("DepartmentId"));//map.get vai buscar o valor no mapa,esse valor vai ser o valor recuperado DepartmentId da linha que está sendo percorrida e salva no resultSET... é como se tivesse o id do dep e o map tá buscando isso
				
				if(dep == null) { //explicado acima, se for nulo é pq n tem, ai a precisa instanciar o dep
					dep = instantiateDepartment(rs);
					//salvo dentro do map, pra q da proxima vez q for passado o id no program, por exemplo, não precisar instanciar e já ter salvo na lista
					map.put(rs.getInt("DepartmentId"), dep); //chave é o departmentid passado e o valor é o objeto dep
				}
				
				
				//pra cada valor do rs q tá sendo percorrido com while, instancio o departamento, o vendedor e depois adicionar o vendedor na lista
				//agora o objeto seller, apontando para o departamento e com os dados do banco 
				Seller obj = instantiateSeller(rs,dep); //mesma coisa, porém agora também passo o dep em questão instanciado
				
				list.add(obj);
				
				//agora sim tenho o mesmo departamento e seus respectivos vendedores apontando
				
			}
			
			return list;
			
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			//fechar recurso mas n fecha conexão, deixa funcionando.. o mesmo objeto dao pode fazer outra operação, deixa pra fechar no programa a conexão
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
