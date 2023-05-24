package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		//instacio e tudo � feito l� na entidade department, aqui s� manda o argumento
		Department obj = new Department(1,"Books");
		System.out.println(obj);
		
		Seller seller = new Seller(21,"icaro","icaro@gmail.com", new Date(),3000.0,obj);
		System.out.println(seller);
		
		//no programa vou poder instanciar SellerDao em uma variavel sellerDao e chamo a f�brica aqui com o m�todo create, dai o programa princiapl n�o conhece a implementa��o, somente a interface
		SellerDao sellerDao = DaoFactory.createSellerDao();
		System.out.println(sellerDao);
	}

}
