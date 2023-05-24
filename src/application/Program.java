package application;

import java.util.Date;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		//instacio e tudo é feito lá na entidade department, aqui só manda o argumento
		Department obj = new Department(1,"Books");
		System.out.println(obj);
		
		Seller seller = new Seller(21,"icaro","icaro@gmail.com", new Date(),3000.0,obj);
		System.out.println(seller);
	}

}
