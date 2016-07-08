package classesDBO;

import java.io.Serializable;
import java.util.Objects;
import classesValidacao.*;

/**
 * Representa um determinado médico.
 * @author Equipe do projeto Médico Fácil
*/
public class Medico implements Cloneable,Serializable{
    
  private int id; // Id do médico.
  private String uf; // Sigla do estado em que o médico atua.
  private String crm;  // Identificação do médico.
  private String nome; // Nome do médico.
  
  private final int MAX_NOME = 50; // Máximo de caracteres para o nome do médico.
  private final int MAX_UF = 2;
  private final int MAX_ESPECIALIDADE = 30;
  
  // Retorna o id do médico.
  public int getId() {
    return id;
  }

  // Seta o id do médico.
  public void setId(int id)throws Exception{
    if (id < 0)
      throw new Exception("Id do médico inválido.");
    this.id = id;
  }

  // Retorna a sigla do estado.
  public String getUf() {
    return this.uf;
  }

  // Seta a sigla do estado.
  public void setUf(String uf)throws Exception{
    if (!Validacao.isSiglaValida(uf,this.MAX_UF))
      throw new Exception("Sigla do estado inválida.");
    this.uf = uf;
  }

  // Retorna o crm do médico.
  public String getCrm(){
    return this.crm;
  }

  // Seta o crm do médico.
  public void setCrm(String crm)throws Exception{
    if (!Validacao.isCrmValido(crm))
      throw new Exception("Crm do médico inválido.");
    this.crm = crm;
  }

  // Retorna o nome do médico.
  public String getNome(){
    return nome;
  }

  // Seta o nome do médico.
  public void setNome(String nome)throws Exception{
    if (!Validacao.isNomePessoaValido(nome,this.MAX_NOME))
      throw new Exception("Nome do médico inválido.");
    this.nome = nome;
  }

  // Retorna um número associado à essa classe.
  @Override
  public int hashCode(){
    int hash = 7;
    hash = 97 * hash + this.id;
    hash = 97 * hash + Objects.hashCode(this.uf);
    hash = 97 * hash + Objects.hashCode(this.crm);
    hash = 97 * hash + Objects.hashCode(this.nome);
    return hash;
  }

  // Compara esse médico com outro, em termos de atributos.
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
    final Medico other = (Medico) obj;
    if (this.id != other.id) {
        return false;
    }
    if (!Objects.equals(this.uf, other.uf)) {
        return false;
    }
    if (!Objects.equals(this.crm, other.crm)) {
        return false;
    }
    if (!Objects.equals(this.nome, other.nome)) {
        return false;
    }
    return true;
  }
  
  // Construtor de cópia.
  public Medico(Medico medico)throws Exception{
    if (medico == null)
      throw new Exception("Médico não fornecido para construtor de cópia.");
    this.id   = medico.getId();
    this.uf   = medico.getUf();
    this.crm  = medico.getCrm();
    this.nome = medico.getNome();
  }
  
  // Retorna uma cópia desse médico.
  @Override
  public Medico clone(){
    Medico medico = null;
    try{
      medico = new Medico(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return medico;
  }
  
  // Construor default.
  public Medico(){
    this.id              = 0;
    this.uf              = "";
    this.crm             = "";
    this.nome            = "";
  }
  
  // Construtor polimórfico.
  public Medico(int id,
                String uf,
                String crm,
                String nome)throws Exception{
    this.setId(id);
    this.setUf(uf);
    this.setCrm(crm);
    this.setNome(nome);
  }
  
  // Retorna uma string relacionada à esse médico.
  @Override
  public String toString(){
    return this.crm;
  }
}
