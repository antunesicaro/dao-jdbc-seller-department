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
	
	public Department() {//padr�o, vazio
		
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

	
	//4-hashcode e equals para o objeto ser comparado pela refer�ncia de conte�do que foi dado � variavel, e n�o de ponteiros na mem�ria
	//usa o override pra sobrescrever,ao sobrescrever esses m�todos, voc� pode controlar o comportamento de compara��o e uso dos objetos da sua classe. Isso � �til quando voc� precisa fazer compara��es personalizadas, como comparar objetos com base em atributos espec�ficos, ignorar certos atributos na compara��o ou considerar objetos iguais mesmo que sejam inst�ncias diferentes.
	
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
	//O m�todo toString() � um m�todo da classe Object em Java e � amplamente utilizado para fornecer uma representa��o de texto de um objeto. Por padr�o, o m�todo toString() retorna uma representa��o da classe e do endere�o de mem�ria do objeto, algo como NomeDaClasse@C�digoHash.
	//pq sobrescrever? : � comum e recomendado sobrescrever o m�todo toString() nas classes personalizadas para fornecer uma representa��o mais significativa e leg�vel do objeto. Ao sobrescrever o m�todo toString(), voc� pode definir o formato e o conte�do do texto retornado para melhor representar o estado do objeto, pode modificar, etc, pode personalziar o que vai ser retornado.
	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}
	
	
	//6-implements Serializable
	//� usada para marcar classes que podem ser convertidas em bytes e restauradas posteriormente, permitindo que objetos sejam armazenados, transmitidos ou mantidos em mem�ria de forma persistente.
	//objeto � transformado em byte.. se quiser gravar em arquivo, trafegar em rede, banco, precisa disso
	//basta colocar no come�o da classe o implements Serializable
	
	
	
	
	
}
