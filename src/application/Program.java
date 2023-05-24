package application;

import model.entities.Department;

public class Program {

	public static void main(String[] args) {
		//instacio e tudo é feito lá na entidade department, aqui só manda o argumento
		Department obj = new Department(1,"Books");
		System.out.println(obj);
	}

}
