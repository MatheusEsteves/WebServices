package classesDBO;

import java.io.Serializable;

public class LoginPaciente implements Serializable{
    private String cpf;
    private String senha;
    
    public LoginPaciente(){
      this.cpf   = "";
      this.senha = "";
    }
    
    public LoginPaciente(String cpf,String senha)throws Exception{
  	  this.setCpf(cpf);
  	  this.setSenha(senha);
    }
    
    public void setCpf(String cpf)throws Exception{
  	  if (cpf == null)
        throw new Exception("CPF não fornecido.");
      if (cpf.trim().equals(""))
        throw new Exception("CPF não fornecido.");
      this.cpf = cpf;
    }
    
    public String getCpf(){
  	  return this.cpf;
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
