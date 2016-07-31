package classesDBO;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
import classesValidacao.Validacao;

/**
 * Representa um paciente no sistema, o qual poderá usufruir das funcionalidades
 * de marcação de consultas, busca por hospitais e especialidades médicas, avaliação
 * e sugestão de atendimentos médicos e o seu próprio cadastro.
* 
* @author Equipe do projeto Médico Fácil
*/
public class Paciente implements Cloneable,Serializable{

  private String bairro,celular,complemento,cpf,nome,senha,endereco,telefone,cidade,uf;
  private Date dataNascimento;
  private int id;
  
  // Número máximo de caracteres para alguns campos.
  private final int MAX_NOME        = 50;
  private final int MAX_SENHA       = 15;
  private final int MAX_ENDERECO    = 100;
  private final int MAX_COMPLEMENTO = 30;
  private final int MAX_BAIRRO      = 30;
  private final int MAX_CIDADE      = 30;
  private final int MAX_UF          = 2;
  
  // Compara essa classe com outra, em termos de atributos.
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
    final Paciente other = (Paciente) obj;
    if (this.id != other.id) {
        return false;
    }
    if (!Objects.equals(this.cidade,other.cidade)) {
        return false;
    }
    if (!Objects.equals(this.uf,other.uf)){
        return false;
    }
    if (!Objects.equals(this.bairro, other.bairro)) {
        return false;
    }
    if (!Objects.equals(this.celular, other.celular)) {
        return false;
    }
    if (!Objects.equals(this.complemento, other.complemento)) {
        return false;
    }
    if (!Objects.equals(this.cpf, other.cpf)) {
        return false;
    }
    if (!Objects.equals(this.nome, other.nome)) {
        return false;
    }
    if (!Objects.equals(this.senha, other.senha)) {
        return false;
    }
    if (!Objects.equals(this.endereco, other.endereco)) {
        return false;
    }
    if (!Objects.equals(this.telefone, other.telefone)) {
        return false;
    }
    if (!Objects.equals(this.dataNascimento, other.dataNascimento)) {
        return false;
    }
    return true;
  }
  
  // Retorna um número relacionado à essa classe para fins externos.
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + Objects.hashCode(this.bairro);
    hash = 23 * hash + Objects.hashCode(this.celular);
    hash = 23 * hash + Objects.hashCode(this.complemento);
    hash = 23 * hash + Objects.hashCode(this.cpf);
    hash = 23 * hash + Objects.hashCode(this.nome);
    hash = 23 * hash + Objects.hashCode(this.senha);
    hash = 23 * hash + Objects.hashCode(this.endereco);
    hash = 23 * hash + Objects.hashCode(this.telefone);
    hash = 23 * hash + Objects.hashCode(this.dataNascimento);
    hash = 23 * hash + Objects.hashCode(this.cidade);
    hash = 23 * hash + Objects.hashCode(this.uf);
    hash = 23 * hash + this.id;
    return hash;                            
  }
  
  // Construtor de cópia.
  public Paciente(Paciente paciente)throws Exception{
    if (paciente == null)
      throw new Exception("Paciente não fornecido em construtor de cópia.");
    this.bairro         = paciente.getBairro();
    this.celular        = paciente.getCelular();
    this.complemento    = paciente.getComplemento();
    this.cpf            = paciente.getCpf();
    this.dataNascimento = paciente.getDataNascimento();
    this.endereco       = paciente.getEndereco();
    this.id             = paciente.getId();
    this.cidade         = paciente.getCidade();
    this.uf             = paciente.getUf();
    this.nome           = paciente.getNome();
    this.senha          = paciente.getSenha();
    this.telefone       = paciente.getTelefone();
  }
  
  // Retorna uma cópia desse paciente.
  @Override
  public Paciente clone(){
    Paciente paciente = null;
    try{
      paciente = new Paciente(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return paciente;
  }

  // Construtor polimórfico.
  public Paciente(int id,
                  String cidade,
                  String uf,
                  String nome,
                  String cpf,
                  String endereco,
                  String bairro,
                  String complemento,
                  String telefone,
                  String celular,
                  String senha,
                  Date dataNascimento)throws Exception{
    this.setId(id);
    this.setCidade(cidade);
    this.setUf(uf);
    this.setNome(nome);
    this.setCpf(cpf);
    this.setEndereco(endereco);
    this.setBairro(bairro);
    this.setComplemento(complemento);
    this.setTelefone(telefone);
    this.setCelular(celular);
    this.setSenha(senha);
    this.setDataNascimento(dataNascimento);
  }

  // Construtor padrão.
  public Paciente(){
    this.id             = 0;
    this.cidade         = "";
    this.uf             = "";
    this.nome           = "";
    this.cpf            = "";
    this.endereco       = "";
    this.bairro         = "";
    this.complemento    = "";
    this.telefone       = "";
    this.celular        = "";
    this.senha          = "";
    this.dataNascimento = null;
  }

  // Seta o código que identifica o paciente no sistema.
  public void setId(int id)throws Exception{
    if (id < 0)
      throw new Exception("id do paciente inválido.");
    this.id = id;
  }

  // Seta o nome do paciente.
  public void setNome(String nome)throws Exception{
    if (!Validacao.isNomePessoaValido(nome,this.MAX_NOME))
      throw new Exception("Nome do paciente inválido.");
    this.nome = nome;
  }

  // Seta o cpf do paciente. Não deve haver pontos nem traços.
  public void setCpf(String cpf)throws Exception{
    if (!Validacao.isCpfValido(cpf))
      throw new Exception("CPF do paciente inválido.");
    this.cpf = cpf;
  }  

  // Seta o endereço do paciente.
  public void setEndereco(String endereco)throws Exception{
    if (!Validacao.isEnderecoValido(endereco,this.MAX_ENDERECO))
      throw new Exception("Endereço do paciente inválido.");
    this.endereco = endereco;
  }

  // Seta o bairro do paciente.
  public void setBairro(String bairro)throws Exception{
    if (!Validacao.isNomeLocalizacaoValido(bairro,this.MAX_BAIRRO))
      throw new Exception("Bairro do paciente inválido.");
    this.bairro = bairro;
  }

  // Seta o complemento do paciente.
  public void setComplemento(String complemento)throws Exception{
	if (complemento == null)
	  complemento = "";
	if (complemento.trim().equals(""))
	  complemento = "";
	if (!complemento.equals("")){
      if (!Validacao.isComplementoEnderecoValido(complemento,this.MAX_COMPLEMENTO))
        throw new Exception("Complemento do endereço do paciente inválido.");
	}
    this.complemento = complemento;
  }

  // Seta o telefone do paciente. Não deve haver pontos nem traços.
  public void setTelefone(String telefone)throws Exception{
    if (!Validacao.isTelefoneValido(telefone))
      throw new Exception("Telefone inválido.");
    this.telefone = telefone;
  }

  // Seta o celular do paciente. Não deve haver pontos nem traços.
  public void setCelular(String celular)throws Exception{
    if (celular == null)
      celular = "";
    if (celular.trim().equals(""))
      celular = "";
    if (!celular.equals("")){
	  if (!Validacao.isCelularValido(celular))
        throw new Exception("Celular inválido.");
    }
    this.celular = celular;
  }
  
  //Seta o nome da cidade em que o pronto socorro se localiza.
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

  // Seta a senha do paciente.
  public void setSenha(String senha)throws Exception{
    if (senha == null)
      throw new Exception("Senha do paciente inválida.");
    if (senha.trim().equals(""))
      throw new Exception("Senha do paciente inválida.");
    if (senha.length() > this.MAX_SENHA)
      throw new Exception("Senha do paciente inválida.");
    this.senha = senha;
  }
  
  // Seta a data de nascimento do paciente.
  public void setDataNascimento(Date dataNascimento)throws Exception{
    if (!Validacao.isDataAnteriorValida(dataNascimento))
      throw new Exception("Data de nascimento do paciente inválida.");
    this.dataNascimento = dataNascimento;
  }
  
  // Retorna o código que identifica o paciente no sistema.
  public int getId(){
    return this.id;
  }

  // Retorna o nome do paciente.
  public String getNome(){
    return this.nome;
  }
  
  //Retorna o id da cidade em que o pronto socorro se localiza.
  public String getCidade(){
    return this.cidade;
  }
 
  public String getUf(){
	return this.uf;
  }

  // Retorna o cpf do paciente.
  public String getCpf(){
    return this.cpf;
  }  

  // Retorna o endereço do paciente.
  public String getEndereco(){
    return this.endereco;
  }

  // Retorna o bairro do paciente.
  public String getBairro(){
    return this.bairro;
  }

  // Retorna o complemento do paciente.
  public String getComplemento(){
    return this.complemento;
  }

  // Retorna o telefone do paciente.
  public String getTelefone(){
    return this.telefone;
  }

  // Retorna o celular do paciente.
  public String getCelular(){
    return this.celular;
  }

  // Retorna a senha do paciente.
  public String getSenha(){
    return this.senha;
  }
  
  // Retorna a data de nascimento do paciente.
  public Date getDataNascimento(){
    return this.dataNascimento;
  }
  
  // Retorna uma string que representa a classe Paciente.
  @Override
  public String toString(){
    return this.cpf;
  }
}