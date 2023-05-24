package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Department implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//1-atributos
	private Integer id;
	private String name;
	
	//2-construtores
	
	public Department() {//padrão, vazio
		
	}

	public Department(Integer id, String name) {//com argumentos
		this.id = id;
		this.name = name;
	}
	
	//3-getters e setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	//4-hashcode e equals para o objeto ser comparado pela referência de conteúdo que foi dado à variavel, e não de ponteiros na memória
	//usa o override pra sobrescrever,ao sobrescrever esses métodos, você pode controlar o comportamento de comparação e uso dos objetos da sua classe. Isso é útil quando você precisa fazer comparações personalizadas, como comparar objetos com base em atributos específicos, ignorar certos atributos na comparação ou considerar objetos iguais mesmo que sejam instâncias diferentes.
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		return Objects.equals(id, other.id);
	}

	

	//5- toString para ter maior facilidade na hora de imprimir os valores do objeto
	//O método toString() é um método da classe Object em Java e é amplamente utilizado para fornecer uma representação de texto de um objeto. Por padrão, o método toString() retorna uma representação da classe e do endereço de memória do objeto, algo como NomeDaClasse@CódigoHash.
	//pq sobrescrever? : é comum e recomendado sobrescrever o método toString() nas classes personalizadas para fornecer uma representação mais significativa e legível do objeto. Ao sobrescrever o método toString(), você pode definir o formato e o conteúdo do texto retornado para melhor representar o estado do objeto, pode modificar, etc, pode personalziar o que vai ser retornado.
	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}
	
	
	//6-implements Serializable
	//é usada para marcar classes que podem ser convertidas em bytes e restauradas posteriormente, permitindo que objetos sejam armazenados, transmitidos ou mantidos em memória de forma persistente.
	//objeto é transformado em byte.. se quiser gravar em arquivo, trafegar em rede, banco, precisa disso
	//basta colocar no começo da classe o implements Serializable
	
	
	
	
	
}
