package classesDBO;

import java.io.Serializable;
import java.util.Objects;
import classesValidacao.*;

/**
  * Representa uma determinada cidade.
  * @author Equipe do projeto Médico Fácil
*/
public class Cidade implements Cloneable,Serializable{
    
  private int id; // Id da cidade.
  private int idEstado; // Id do estado ao qual a cidade pertence.
  private String nome; // Nome da cidade.
  
  private final int MAX_NOME = 30; // Número máximo de caracteres para o nome da cidade.
  
  // Retorna um número associado à essa classe.
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 59 * hash + this.id;
    hash = 59 * hash + this.idEstado;
    hash = 59 * hash + Objects.hashCode(this.nome);
    hash = 59 * hash + this.MAX_NOME;
    return hash;
  }

  // Compara essa cidade com outra, em termos de atributos.
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
    final Cidade other = (Cidade) obj;
    if (this.id != other.id) {
        return false;
    }
    if (this.idEstado != other.idEstado) {
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
  public Cidade(Cidade cidade)throws Exception{
    if (cidade == null)
      throw new Exception("Cidade não fornecida para construtor de cópia.");
    this.id       = cidade.getId();
    this.idEstado = cidade.getIdEstado();
    this.nome     = cidade.getNome();
  }
  
  // Retorna uma cópia dessa cidade.
  @Override
  public Cidade clone(){
    Cidade cidade = null;
    try{
      cidade = new Cidade(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return cidade;
  }
  
  // Construtor polimórfico.
  public Cidade(int id,
                int idEstado,
                String nome)throws Exception{
    this.setId(id);
    this.setIdEstado(idEstado);
    this.setNome(nome);
  }
  
  // Construtor default.
  public Cidade(){
    this.id       = 0;
    this.idEstado = 0;
    this.nome     = "";
  }

  // Retorna o id da cidade.
  public int getId(){
    return id;
  }

  // Seta o id da cidade.
  public void setId(int id) throws Exception{
    if (id <= 0)
      throw new Exception("Id da cidade inválido.");
    this.id = id;
  }

  // Retorna o id do estado ao qual a cidade pertence.
  public int getIdEstado(){
    return idEstado;
  }

  // Seta o id do estado ao qual a cidade pertence.
  public void setIdEstado(int idEstado) throws Exception{
    if (idEstado <= 0)
      throw new Exception("Id do estado inválido.");
    this.idEstado = idEstado;
  }

  // Retorna o nome do estado.
  public String getNome(){
    return nome;
  }

  // Seta o nome do estado.
  public void setNome(String nome) throws Exception{
    if (!Validacao.isNomeLocalizacaoValido(nome,this.MAX_NOME))
      throw new Exception("Nome da cidade inválido.");
    this.nome = nome;
  }
  
  // Retorna uma string relacionada à cidade.
  @Override
  public String toString(){
    return this.nome;
  }
}
