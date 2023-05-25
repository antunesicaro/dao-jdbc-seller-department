package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		//no programa vou poder instanciar SellerDao em uma variavel sellerDao e chamo a fábrica aqui com o método create, dai o programa princiapl não conhece a implementação, somente a interface
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		//variavel seller é do tipo Seller e vai receber o retorno do findbyid com argumento 3, tem q mostrar os dados retornados corretamente.
		Seller seller  = sellerDao.findById(3);
		
		System.out.println(seller);
		
		
		
		
		
		
		
		
		
		
		
		
		
		//##TESTES E ESTUDOS INICIAIS##
		//instacio e tudo é feito lá na entidade department, aqui só manda o argumento
		//Department obj = new Department(1,"Books");
		//System.out.println(obj);
		//Seller seller = new Seller(21,"icaro","icaro@gmail.com", new Date(),3000.0,obj);
		//System.out.println(seller);
		//no programa vou poder instanciar SellerDao em uma variavel sellerDao e chamo a fábrica aqui com o método create, dai o programa princiapl não conhece a implementação, somente a interface
		//SellerDao sellerDao = DaoFactory.createSellerDao();
		//System.out.println(sellerDao);
	}

}
