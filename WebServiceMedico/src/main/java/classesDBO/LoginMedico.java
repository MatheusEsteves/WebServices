package classesDBO;

import java.io.Serializable;

public class LoginMedico implements Serializable{

  private String crm;
  private String senha;
  
  public LoginMedico(){
	this.crm   = "";
	this.senha = "";
  }
  
  public LoginMedico(String crm,String senha)throws Exception{
	this.setCrm(crm);
	this.setSenha(senha);
  }
  
  public void setCrm(String crm)throws Exception{
	if (crm == null)
	  throw new Exception("Crm não fornecido.");
	if (crm.trim().equals(""))
	  throw new Exception("Crm não fornecido.");
	this.crm   = crm;
  }
  
  public String getCrm(){
	return this.crm;
  }
  
  public void setSenha(String senha)throws Exception{
	if (senha == null)
	  throw new Exception("Senha não fornecida.");
	if (senha.trim().equals(""))
	  throw new Exception("Senha não fornecida.");
	this.senha = senha;
  }
  
  public String getSenha(){
	return this.senha;
  }
}
