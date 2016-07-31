package classesDBO;

import java.io.Serializable;
import java.util.Objects;
import classesValidacao.*;

/**
  * Representa um determinado convênio médico.
  * @author Equipe do projeto Médico Fácil
*/
public class Convenio implements Cloneable,Serializable{
    
  private int id; // Id do convênio médico.
  private String nome; // Nome do convênio médico.
  
  private final int MAX_NOME = 30; // Número máximo de caracteres para o nome do convênio.
  
  // Retorna um número associdado ao convênio.
  @Override
  public int hashCode(){
    int hash = 7;
    hash = 67 * hash + this.id;
    hash = 67 * hash + Objects.hashCode(this.nome);
    hash = 67 * hash + this.MAX_NOME;
    return hash;
  }

  // Compara esse convênio com outro, em termos de atributos.
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null) {
        return false;
    }
    if (getClass() != obj.getClass()) {
        return false;
    }
    final Convenio other = (Convenio) obj;
    if (this.id != other.id) {
        return false;
    }
    if (this.MAX_NOME != other.MAX_NOME) {
        return false;
    }
    if (!Objects.equals(this.nome, other.nome)) {
        return false;
    }
    return true;
  }
  
  // Construtor de cópia.
  public Convenio(Convenio convenio) throws Exception{
    if (convenio == null)
      throw new Exception("Convênio não fornecido para construtor de cópia.");
    this.id   = convenio.getId();
    this.nome = convenio.getNome();
  }
  
  // Retorna uma cópia desse convênio.
  @Override
  public Convenio clone(){
    Convenio convenio = null;
    try{
      convenio = new Convenio(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return convenio;
  }
  
  // Construtor polimórfico.
  public Convenio(int id,String nome) throws Exception{
    this.setId(id);
    this.setNome(nome);
  }
  
  // Construtor default.
  public Convenio(){
    this.id   = 0;
    this.nome = "";
  }

  // Retorna o id do convênio.
  public int getId(){
    return id;
  }

  // Seta o id do convênio.
  public void setId(int id) throws Exception{
    if (id <= 0)
      throw new Exception("Id do convênio inválido.");
    this.id = id;
  }

  // Retorna o nome do convênio.
  public String getNome(){
    return nome;
  }

  // Seta o nome do convênio.
  public void setNome(String nome) throws Exception{
    if (!Validacao.isNomeInstituicaoValido(nome,this.MAX_NOME))
      throw new Exception("Nome do convênio inválido.");
    this.nome = nome;
  }
  
  // Retorna uma string relacionada ao convênio.
  @Override
  public String toString(){
    return this.nome;
  }
}
