package application;

import model.entities.Department;

public class Program {

	public static void main(String[] args) {
		//instacio e tudo � feito l� na entidade department, aqui s� manda o argumento
		Department obj = new Department(1,"Books");
		System.out.println(obj);
	}

}
