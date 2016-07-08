package classesDBO;

import java.io.Serializable;

public class MedicoEspecialidade implements Serializable,Cloneable{
	
  private int id;
  private int idMedico;  
  private int idEspecialidade;
  
  public MedicoEspecialidade(){
	this.id = 0;
	this.idMedico = 0;
	this.idEspecialidade = 0;
  }

  public MedicoEspecialidade(int id, int idMedico, int idEspecialidade)throws Exception{
	this.setId(id);
	this.setIdMedico(idMedico);
	this.setIdEspecialidade(idEspecialidade);
  }
  
  public MedicoEspecialidade(MedicoEspecialidade other){
	this.id = other.id;
	this.idMedico = other.idMedico;
	this.idEspecialidade = other.idEspecialidade;
  }
  
  @Override
  public MedicoEspecialidade clone(){
	return new MedicoEspecialidade(this);
  }

  @Override
  public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + idEspecialidade;
		result = prime * result + idMedico;
		return result;
  }
  @Override
  public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MedicoEspecialidade other = (MedicoEspecialidade) obj;
		if (id != other.id)
			return false;
		if (idEspecialidade != other.idEspecialidade)
			return false;
		if (idMedico != other.idMedico)
			return false;
		return true;
  }
	
  public int getId() {
		return id;
  }
  
  public void setId(int id) throws Exception{
	  if (id < 0)
		throw new Exception("Id da relação entre médico e especialidade inválido.");
	  this.id = id;
  }
  
  public int getIdMedico() {
	  return this.idMedico;
  }
  
  public void setIdMedico(int idMedico) throws Exception{
	  if (idMedico < 0)
		throw new Exception("Id do médico inválido.");
	  this.idMedico = idMedico;
  }
  
  public int getIdEspecialidade() {
	  return idEspecialidade;
  }
  
  public void setIdEspecialidade(int idEspecialidade) throws Exception{
	  if (idEspecialidade < 0)
		throw new Exception("Id da especialidade inválido.");
	  this.idEspecialidade = idEspecialidade; 
  }
}
