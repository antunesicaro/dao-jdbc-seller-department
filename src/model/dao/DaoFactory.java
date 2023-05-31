package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

/*
 * a DaoFactory fornece uma abstração para a criação dos DAOs, permitindo que o código cliente solicite a criação de um DAO sem precisar conhecer a implementação específica por trás dele
 *  (a lógica real estará em SellerDaoJDBC,aqui só abstrai) para interagir com o banco de dados e executar as operações desejadas.

 
 * */
public class DaoFactory { //no programa vou poder instanciar SellerDao em uma variavel sellerDao e chamo a fábrica aqui com o método create, dai o programa princiapl não conhece a implementação, somente a interface
	public static SellerDao createSellerDao() { //método que retorna o tipo da interface, porém internamente está instanciando a implementação, ai não exponho a implementação que é a sellerdaojdbc com o esqueleto, deixa exposto só a interface que é SellerDao
		return new SellerDaoJDBC(DB.getConnection()); // antes de ir pra implementação, faz a conexão com banco para que os métodos possam ser usados.
	}
}
