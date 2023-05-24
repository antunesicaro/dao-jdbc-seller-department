package model.dao;

import model.dao.impl.SellerDaoJDBC;

/*
 * Atualmente, esses m�todos est�o retornando null ou n�o fazendo nada. � necess�rio adicionar a l�gica real para interagir com o banco de dados e executar as opera��es desejadas.

Essa classe serve como um esqueleto para a implementa��o real do acesso ao banco de dados usando JDBC (Java Database Connectivity).
 * */
public class DaoFactory { //no programa vou poder instanciar SellerDao em uma variavel sellerDao e chamo a f�brica aqui com o m�todo create, dai o programa princiapl n�o conhece a implementa��o, somente a interface
	public static SellerDao createSellerDao() { //m�todo que retorna o tipo da interface, por�m internamente est� instanciando a implementa��o, ai n�o exponho a implementa��o que � a sellerdaojdbc com o esqueleto, deixa exposto s� a interface que � SellerDao
		return new SellerDaoJDBC();
	}
}
