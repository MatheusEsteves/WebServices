package classesDBO;

import java.io.Serializable;
import java.util.Objects;
import classesValidacao.*;

/**
 * Representa um determinado pronto socorro.
 * @author Equipe do projeto Médico Fácil
*/
public class ProntoSocorro implements Cloneable,Serializable{
  
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

  // Compara esse pronto socorro à outro, em termos de atributos.
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
    final ProntoSocorro other = (ProntoSocorro) obj;
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
  public ProntoSocorro(ProntoSocorro prontoSocorro)throws Exception{
    if (prontoSocorro == null)
      throw new Exception("ProntoSocorro não fornecido em construtor de cópia.");
    this.id       = prontoSocorro.getId();
    this.cidade   = prontoSocorro.getCidade();
    this.uf       = prontoSocorro.getUf();
    this.bairro   = prontoSocorro.getBairro();
    this.endereco = prontoSocorro.getEndereco();
    this.nome     = prontoSocorro.getNome();
    this.telefone = prontoSocorro.getTelefone();
    this.latitude = prontoSocorro.getLatitude();
    this.longitude = prontoSocorro.getLongitude();
  }
  
  // Retorna uma cópia desse ProntoSocorro.
  @Override
  public ProntoSocorro clone(){
    ProntoSocorro prontoSocorro = null;
    try{
      prontoSocorro = new ProntoSocorro(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return prontoSocorro;
  }
  
  // Construtor polimórfico.
  public ProntoSocorro(int id,
                       String cidade,
                       String uf,
                       String bairro,
                       String endereco,
                       String nome,
                       String telefone,
                       float latitude,
                       float longitude)throws Exception{
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
  public ProntoSocorro(){
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
  
  // Seta o id do pronto socorro.
  public void setId(int id)throws Exception{
    if (id < 0)
      throw new Exception("Id do pronto socorro inválido.");
    this.id = id;
  }
  
  // Seta o nome da cidade em que o pronto socorro se localiza.
  public void setCidade(String cidade)throws Exception{
    if (!Validacao.isNomeLocalizacaoValido(cidade,this.MAX_CIDADE))
      throw new Exception("Nome da cidade inválido.");
    this.cidade = cidade;
  }
  
  // Seta a sigla do estado em que a cidade se localiza.
  public void setUf(String uf)throws Exception{
	if (!Validacao.isSiglaValida(uf,this.MAX_UF))
	  throw new Exception("Sigla do estado inválida.");
	this.uf = uf;
  }
  
  // Seta o bairro em que o pronto socorro se localiza.
  public void setBairro(String bairro)throws Exception{
    if (!Validacao.isNomeLocalizacaoValido(bairro,this.MAX_BAIRRO))
      throw new Exception("Bairro inválido.");
    this.bairro = bairro;
  }
  
  // Seta o endereço do pronto socorro.
  public void setEndereco(String endereco)throws Exception{
    if (!Validacao.isEnderecoValido(endereco,this.MAX_ENDERECO))
      throw new Exception("Endereço inválido.");
    this.endereco = endereco;
  }
  
  // Seta o nome do pronto socorro.
  public void setNome(String nome)throws Exception{
    if (!Validacao.isNomeInstituicaoValido(nome,this.MAX_NOME))
      throw new Exception("Nome do pronto socorro inválido.");
    this.nome = nome;
  }
  
  // Seta o telefone do pronto socorro.
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
  
  // Retorna o id do pronto socorro.
  public int getId(){
    return this.id;
  }
  
  // Retorna o id da cidade em que o pronto socorro se localiza.
  public String getCidade(){
    return this.cidade;
  }
  
  public String getUf(){
	return this.uf;
  }
  
  // Retorna o bairro em que o pronto socorro se localiza.
  public String getBairro(){
    return this.bairro;
  }
  
  // Retorna o endereço do pronto socorro.
  public String getEndereco(){
    return this.endereco;
  }
  
  // Retorna o nome do pronto socorro.
  public String getNome(){
    return this.nome;
  }
  
  // Retorna o telefone do pronto socorro.
  public String getTelefone(){
    return this.telefone;
  }
  
  public float getLatitude(){
	return this.latitude;
  }
  
  public float getLongitude(){
	return this.longitude;
  }
  
  // Retorna uma string que representa esse pronto socorro.
  @Override
  public String toString(){
    return this.nome; 
  }
}
