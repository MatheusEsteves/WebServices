package classesDBO;

import java.io.Serializable;
import java.util.Objects;
import classesValidacao.*;

/**
  * Representa uma determinada clínica.
  * @author Equipe do projeto Médico Fácil
*/
public class Clinica implements Cloneable,Serializable{
  
  private int id;
  private String bairro,endereco,nome,telefone,cidade,uf;
  private float latitude,longitude;
 
  // Número máximo de caracteres para alguns atributos.
  private final int MAX_BAIRRO   = 30;
  private final int MAX_ENDERECO = 100;
  private final int MAX_NOME     = 30;
  private final int MAX_CIDADE   = 30;
  private final int MAX_UF       = 2;
  
  // Retorna um número relacionado à essa classe, para fins externos.
  @Override
  public int hashCode(){
    int hash = 5;
    hash = 61 * hash + this.id;
    hash = 61 * hash + (int)this.latitude;
    hash = 61 * hash + (int)this.longitude;
    hash = 61 * hash + Objects.hashCode(this.cidade);
    hash = 61 * hash + Objects.hashCode(this.uf);
    hash = 61 * hash + Objects.hashCode(this.bairro);
    hash = 61 * hash + Objects.hashCode(this.endereco);
    hash = 61 * hash + Objects.hashCode(this.nome);
    hash = 61 * hash + Objects.hashCode(this.telefone);
    return hash;
  }

  // Compara essa clínica à outra, em termos de atributos.
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
    final Clinica other = (Clinica) obj;
    if (this.id != other.id) {
        return false;
    }
    if (this.latitude != other.latitude){
       return false;
    }
    if (this.longitude != other.longitude){
       return false;
    }
    if (!Objects.equals(this.cidade,other.cidade)){
        return false;
    }
    if (!Objects.equals(this.uf,other.uf)){
        return false;
    }
    if (!Objects.equals(this.bairro, other.bairro)) {
        return false;
    }
    if (!Objects.equals(this.endereco, other.endereco)) {
        return false;
    }
    if (!Objects.equals(this.nome, other.nome)) {
        return false;
    }
    if (!Objects.equals(this.telefone, other.telefone)) {
        return false;
    }
    return true;
  }
  
  // Construtor de cópia.
  public Clinica(Clinica clinica)throws Exception{
    if (clinica == null)
      throw new Exception("Clínica não fornecida em construtor de cópia.");
    this.id        = clinica.getId();
    this.cidade    = clinica.getCidade();
    this.uf        = clinica.getUf();
    this.bairro    = clinica.getBairro();
    this.endereco  = clinica.getEndereco();
    this.nome      = clinica.getNome();
    this.telefone  = clinica.getTelefone();
    this.latitude  = clinica.getLatitude();
    this.longitude = clinica.getLongitude();
  }
  
  // Retorna uma cópia dessa clínica.
  @Override
  public Clinica clone(){
    Clinica clinica = null;
    try{
      clinica = new Clinica(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return clinica;
  }
  
  // Construtor polimórfico.
  public Clinica(int id,
                 String cidade,
                 String uf,
                 String bairro,
                 String endereco,
                 String nome,
                 String telefone,
                 float latitude,
                 float longitude
                )throws Exception{
    this.setId(id);
    this.setCidade(cidade);
    this.setUf(uf);
    this.setBairro(bairro);
    this.setEndereco(endereco);
    this.setNome(nome);
    this.setTelefone(telefone);
    this.setLatitude(latitude);
    this.setLongitude(longitude);
  }
  
  // Construtor default.
  public Clinica(){
    this.id        = 0;
    this.cidade    = "";
    this.uf        = "";
    this.bairro    = "";
    this.endereco  = "";
    this.nome      = "";
    this.telefone  = "";
    this.latitude  = 0;
    this.longitude = 0;
  }
  
  // Seta o id da clínica.
  public void setId(int id)throws Exception{
    if (id < 0)
      throw new Exception("Id da clínica inválido.");
    this.id = id;
  }
  
  // Seta o nome da cidade em que a clínica se localiza.
  public void setCidade(String cidade)throws Exception{
    if (!Validacao.isNomeLocalizacaoValido(cidade,this.MAX_CIDADE))
      throw new Exception("Nome da cidade inválido.");
    this.cidade = cidade;
  }
  
  // Seta a sigla do estado em que a cidade se encontra.
  public void setUf(String uf)throws Exception{
	if (!Validacao.isSiglaValida(uf,this.MAX_UF))
	  throw new Exception("Sigla do estado inválida.");
	this.uf = uf;
  }
  
  // Seta o bairro em que a clínica se localiza.
  public void setBairro(String bairro)throws Exception{
    if (!Validacao.isNomeLocalizacaoValido(bairro,this.MAX_BAIRRO))
      throw new Exception("Bairro inválido.");
    this.bairro = bairro;
  }
  
  // Seta o endereço da clínica.
  public void setEndereco(String endereco)throws Exception{
    if (!Validacao.isEnderecoValido(endereco,this.MAX_ENDERECO))
      throw new Exception("Endereço inválido.");
    this.endereco = endereco;
  }
  
  // Seta o nome da clínica.
  public void setNome(String nome)throws Exception{
    if (!Validacao.isNomeInstituicaoValido(nome,this.MAX_NOME))
      throw new Exception("Nome da clínica inválido.");
    this.nome = nome;
  }
  
  // Seta o telefone da clínica.
  public void setTelefone(String telefone)throws Exception{
    if (!Validacao.isTelefoneValido(telefone))
      throw new Exception("Telefone inválido.");
    this.telefone = telefone;
  }
  
  public void setLatitude(float latitude){
	this.latitude = latitude;
  }
  
  public void setLongitude(float longitude){
	this.longitude = longitude;
  }
  
  // Retorna o id da clínica.
  public int getId(){
    return this.id;
  }
  
  // Retorna o nome da cidade em que a clínica se localiza.
  public String getCidade(){
    return this.cidade;
  }
  
  // Retorna a sigla do estado em que a cidade se encontra.
  public String getUf(){
	return this.uf;
  }
  
  // Retorna o bairro em que a clínica se localiza.
  public String getBairro(){
    return this.bairro;
  }
  
  // Retorna o endereço da clínica.
  public String getEndereco(){
    return this.endereco;
  }
  
  // Retorna o nome da clínica.
  public String getNome(){
    return this.nome;
  }
  
  // Retorna o telefone da clínica.
  public String getTelefone(){
    return this.telefone;
  }
  
  public float getLatitude(){
	return this.latitude;
  }
  
  public float getLongitude(){
	return this.longitude;
  }
  
  // Retorna uma string que representa essa clínica.
  @Override
  public String toString(){
    return this.nome;
  }
}
