package classesDBO;

import java.util.Objects;

/**
  * Representa um administrador do sistema.
  * @author Equipe do projeto Médico Fácil
*/
public class Adm implements Cloneable{
    
  private int id; // Id do administrador.
  private String usuario; // Username do administrador para entrar no sistema.
  private String senha; // Senha do administrador para entrar no sistema.
  
  //Máximo de caracteres para alguns atributos.
  private final int MAX_USUARIO = 15;
  private final int MAX_SENHA   = 10;
  
  /**
   * Retorna um número associado à essa classe.
  */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + this.id;
    hash = 37 * hash + Objects.hashCode(this.usuario);
    hash = 37 * hash + Objects.hashCode(this.senha);
    return hash;
  }

  // Compara esse administrador com outro, em termos de atributos.
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
    final Adm other = (Adm) obj;
    if (this.id != other.id) {
        return false;
    }
    if (!Objects.equals(this.usuario, other.usuario)) {
        return false;
    }
    if (!Objects.equals(this.senha, other.senha)) {
        return false;
    }
    return true;
  }
 
  // Construtor de cópia.
  public Adm(Adm adm)throws Exception{
    if (adm == null)
      throw new Exception("Administrador não fornecido em construtor de cópia.");
    this.id      = adm.getId();
    this.usuario = adm.getUsuario();
    this.senha   = adm.getSenha();
  }
 
  // Retorna uma cópia desse administrador.
  @Override
  public Adm clone(){
    Adm adm = null;
    try{
      adm = new Adm(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return adm;
  }

  // Construtor polimórfico.
  public Adm(int id,
             String usuario,
             String senha)throws Exception{
    this.setId(id);
    this.setUsuario(usuario);
    this.setSenha(senha);
  }
 
  // Construtor default.
  public Adm(){
    this.id      = 0;
    this.usuario = "";
    this.senha   = "";
  }
  
  // Retorna o id do administrador.
  public int getId() {
    return id;
  }

  // Seta o id do administrador.
  public void setId(int id)throws Exception{
    if (id < 0)
      throw new Exception("Id do administrador inválido.");
    this.id = id;
  }

  // Retorna o username do administrador.
  public String getUsuario() {
    return usuario;
  }

  // Seta o username do administrador.
  public void setUsuario(String usuario)throws Exception{
    if (usuario == null)
      throw new Exception("Usuário não fornecido.");
    if (usuario.trim().equals(""))
      throw new Exception("Usuário não fornecido.");
    if (usuario.length() > this.MAX_USUARIO)
      throw new Exception("Usuário com número de caracteres não permitido.");
    this.usuario = usuario;
  }

  // Retorna a senha do administrador.
  public String getSenha() {
    return senha;
  }

  // Seta a senha do administrador.
  public void setSenha(String senha)throws Exception{
    if (senha == null)
      throw new Exception("Senha não fornecida.");
    if (senha.trim().equals(""))
      throw new Exception("Senha não fornecida.");
    if (senha.length() > this.MAX_SENHA)
      throw new Exception("Senha com número de caracteres não permitido.");
    this.senha = senha;
  }
  
  // Retorna uma string relacionada ao administrador.
  @Override
  public String toString(){
    return this.usuario;
  }
}
