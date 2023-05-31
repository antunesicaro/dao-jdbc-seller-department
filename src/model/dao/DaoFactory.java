package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

/*
 * a DaoFactory fornece uma abstra��o para a cria��o dos DAOs, permitindo que o c�digo cliente solicite a cria��o de um DAO sem precisar conhecer a implementa��o espec�fica por tr�s dele
 *  (a l�gica real estar� em SellerDaoJDBC,aqui s� abstrai) para interagir com o banco de dados e executar as opera��es desejadas.

 
 * */
public class DaoFactory { //no programa vou poder instanciar SellerDao em uma variavel sellerDao e chamo a f�brica aqui com o m�todo create, dai o programa princiapl n�o conhece a implementa��o, somente a interface
	public static SellerDao createSellerDao() { //m�todo que retorna o tipo da interface, por�m internamente est� instanciando a implementa��o, ai n�o exponho a implementa��o que � a sellerdaojdbc com o esqueleto, deixa exposto s� a interface que � SellerDao
		return new SellerDaoJDBC(DB.getConnection()); // antes de ir pra implementa��o, faz a conex�o com banco para que os m�todos possam ser usados.
	}
}
