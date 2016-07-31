package classesDBO;

import java.io.Serializable;

public class InclusaoMedicoClinica implements Serializable{

	private Medico medico;
	private String senha;
	
	public InclusaoMedicoClinica(){
	  this.medico = null;
	  this.senha  = "";
	}
	
	public InclusaoMedicoClinica(Medico medico,String senha)throws Exception{
	  this.setMedico(medico);
	  this.setSenha(senha);
	}
	
	public void setMedico(Medico medico)throws Exception{
	  if (medico == null)
	    throw new Exception("Médico não fornecido.");
	  this.medico = medico;
	}
	
	public void setSenha(String senha)throws Exception{
	  if (senha == null)
		throw new Exception("Senha não fornecida.");
	  if (senha.trim().equals(""))
		throw new Exception("Senha não fornecida.");
	  this.senha = senha;
	}
	
	public Medico getMedico(){
	  return this.medico;
	}
	
	public String getSenha(){
	  return this.senha;
	}
}
