package classesDBO;

import java.io.Serializable;

public class LoginAdm implements Serializable{

  private String usuario;
  private String senha;
  
  public LoginAdm(){
	this.usuario    = "";
	this.senha      = "";
  }
  
  public LoginAdm(String usuario,String senha)throws Exception{
	this.setUsuario(usuario);
	this.setSenha(senha);
  }
  
  public void setUsuario(String usuario)throws Exception{
	if (usuario == null)
	  throw new Exception("Usuário não fornecido.");
	if (usuario.trim().equals(""))
	  throw new Exception("Usuário não fornecido.");
	this.usuario = usuario;
  }
  
  public void setSenha(String senha)throws Exception{
	if (senha == null)
      throw new Exception("Senha não fornecida.");
    if (senha.trim().equals(""))
	  throw new Exception("Senha não fornecida.");
    this.senha = senha;
  }
  
  public String getUsuario(){
	return this.usuario;
  }
  
  public String getSenha(){
	return this.senha;
  }
}
