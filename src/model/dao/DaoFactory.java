package model.dao;

import model.dao.impl.SellerDaoJDBC;

/*
 * Atualmente, esses métodos estão retornando null ou não fazendo nada. É necessário adicionar a lógica real para interagir com o banco de dados e executar as operações desejadas.

Essa classe serve como um esqueleto para a implementação real do acesso ao banco de dados usando JDBC (Java Database Connectivity).
 * */
public class DaoFactory { //no programa vou poder instanciar SellerDao em uma variavel sellerDao e chamo a fábrica aqui com o método create, dai o programa princiapl não conhece a implementação, somente a interface
	public static SellerDao createSellerDao() { //método que retorna o tipo da interface, porém internamente está instanciando a implementação, ai não exponho a implementação que é a sellerdaojdbc com o esqueleto, deixa exposto só a interface que é SellerDao
		return new SellerDaoJDBC();
	}
}
