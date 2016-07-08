package classesDBO;

import java.io.Serializable;
import java.util.Objects;
import classesValidacao.*;

/**
 * Representa um determinado estado.
 * @author Equipe do projeto Médico Fácil
*/
public class Estado implements Cloneable,Serializable{
  
  private int id; // Id do estado.
  private String sigla; // Sigla do estado. Ex: AM,SP,RJ.
  private String nome;  // Nome do estado. Ex: Amazonas,São Paulo,Rio de Janeiro.
  
  // Número máximo de caracteres de alguns atributos.
  private final int MAX_NOME  = 30;
  private final int MAX_SIGLA = 2;
  
  // Retorna um número relacionado à essa classe, para fins externos.
  @Override
  public int hashCode(){
    int hash = 7;
    hash = 53 * hash + this.id;
    hash = 53 * hash + Objects.hashCode(this.sigla);
    hash = 53 * hash + Objects.hashCode(this.nome);
    return hash;
  }

  // Compara esse estado com outro, em termos de atributos.
  @Override
  public boolean equals(Object obj){
    if (this == obj) {
        return true;
    }
    if (obj == null) {
        return false;
    }
    if (getClass() != obj.getClass()) {
        return false;
    }
    final Estado other = (Estado) obj;
    if (this.id != other.id) {
        return false;
    }
    if (!Objects.equals(this.sigla, other.sigla)) {
        return false;
    }
    if (!Objects.equals(this.nome, other.nome)) {
        return false;
    }
    return true;
  }
  
  // Construtor de cópia.
  public Estado(Estado estado)throws Exception{
    if (estado == null)
      throw new Exception("Estado não fornecido em construtor de cópia.");
    this.id    = estado.getId();
    this.sigla = estado.getSigla();
    this.nome  = estado.getNome();
  }
  
  // Retorna uma cópia desse estado.
  @Override
  public Estado clone(){
    Estado estado = null;
    try{
      estado = new Estado(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return estado;
  }
  
  // Construtor polimórfico.
  public Estado(int id,
                String nome,
                String sigla)throws Exception{
    this.setId(id);
    this.setNome(nome);
    this.setSigla(sigla);
  }
  
  // Construtor default.
  public Estado(){
    this.id    = 0;
    this.nome  = "";
    this.sigla = "";
  }

  // Retorna o id do estado.
  public int getId(){
    return id;
  }

  // Seta o id do estado.
  public void setId(int id) throws Exception{
    if (id <= 0)
      throw new Exception("Id do estado inválido.");
    this.id = id;
  }

  // Retorna a sigla do estado.
  public String getSigla(){
    return sigla;
  }

  // Seta a sigla do estado.
  public void setSigla(String sigla) throws Exception{
    if (!Validacao.isSiglaValida(sigla,this.MAX_SIGLA))
      throw new Exception("Sigla do estado inválida.");
    this.sigla = sigla;
  }

  // Retorna o nome do estado.
  public String getNome(){
    return nome;
  }

  // Seta o nome do estado.
  public void setNome(String nome) throws Exception{
    if (!Validacao.isPalavraValida(nome,this.MAX_NOME))
      throw new Exception("Nome do estado inválido.");
    this.nome = nome;
  }
  
  // Retorna uma string relacionada à esse estado.
  @Override
  public String toString(){
    return this.nome;
  }
}
