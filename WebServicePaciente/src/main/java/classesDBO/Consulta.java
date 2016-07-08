package classesDBO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

import classesValidacao.*;

/**
  * Representa uma determinada consulta na clínica.
  * @author Equipe do projeto Médico Fácil
*/
public class Consulta implements Cloneable,Serializable{
  
  private int id; // Id dessa consulta.
  private Paciente paciente;
  private Medico medico;
  private Clinica clinica;
  @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm")
  private Timestamp dataHora; // Data e hora da consulta.
  
  // Retorna um número relacionado à essa consulta.
  @Override
  public int hashCode(){
    int hash = 3;
    hash = 97 * hash + this.id;
    hash = 97 * hash + Objects.hashCode(this.paciente);
    hash = 97 * hash + Objects.hashCode(this.medico);
    hash = 97 * hash + Objects.hashCode(this.clinica);
    hash = 97 * hash + Objects.hashCode(this.dataHora);
    return hash;
  }

  // Compara essa consulta com outra, em termos de atributos.
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
    final Consulta other = (Consulta) obj;
    if (this.id != other.id) {
        return false;
    }
    if (!Objects.equals(this.paciente,other.paciente)) {
        return false;
    }
    if (!Objects.equals(this.medico,other.medico)){
        return false;
    }
    if (!Objects.equals(this.clinica,other.clinica)){
        return false;
    }
    if (!Objects.equals(this.dataHora, other.dataHora)) {
        return false;
    }
    return true;
  }
  
  // Construtor de cópia.
  public Consulta(Consulta consulta)throws Exception{
    if (consulta == null)
      throw new Exception("Consulta não fornecida em construtor de cópia.");
    this.id       = consulta.getId();
    this.paciente = consulta.getPaciente();
    this.medico   = consulta.getMedico();
    this.clinica  = consulta.getClinica();
    this.dataHora = consulta.getDataHora();
  }
  
  // Retorna uma cópia dessa consulta.
  @Override
  public Consulta clone(){
    Consulta consulta = null;
    try{
      consulta = new Consulta(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return consulta;
  }
  
  // Constutor polimórfico.
  public Consulta(int id,
                  Paciente paciente,
                  Medico medico,
                  Clinica clinica,
                  Timestamp dataHora)throws Exception{
    this.setId(id);
    this.setPaciente(paciente);
    this.setMedico(medico);
    this.setClinica(clinica);
    this.setDataHora(dataHora);
  }
  
  // Construtor default.
  public Consulta(){
    this.id       = 0;
    this.medico   = null;
    this.clinica  = null;
    this.paciente = null;
    this.dataHora = null;
  }
  
  // Retorna o id dessa consulta.
  public int getId(){
    return id;
  }

  // Seta o id dessa consulta.
  public void setId(int id)throws Exception{
    if (id < 0)
      throw new Exception("Id da consulta inválido.");
    this.id = id;
  }

  public Paciente getPaciente(){
	return this.paciente;
  }
  
  // O paciente pode ser nulo, pois não precisaremos dele, por exemplo,
  // para cadastrar uma consulta. Nesse processo, o paciente será preenchido
  // automaticamente com base nos dados do paciente logado no momento.
  public void setPaciente(Paciente paciente)throws Exception{
	this.paciente = paciente;
  }
  
  public Medico getMedico(){
	return this.medico;
  }
  
  public void setMedico(Medico medico)throws Exception{
	if (medico == null)
	  throw new Exception("Médico não fornecido.");
	this.medico = medico;
  }
  
  public Clinica getClinica(){
	return this.clinica;
  }
  
  public void setClinica(Clinica clinica)throws Exception{
	if (clinica == null)
	  throw new Exception("Clínica não fornecida.");
	this.clinica = clinica;
  }

  // Retorna o período da consulta do paciente.
  @JsonSerialize(using=CustomTimestampSerializer.class)
  public Timestamp getDataHora(){
    return dataHora;
  }

  // Seta o período da consulta do paciente.
  @JsonDeserialize(using=CustomTimestampDeserializer.class)
  public void setDataHora(Timestamp dataHora)throws Exception{
    if (!Validacao.isDataPosteriorValida(dataHora))
      throw new Exception("Período da consulta inválido.");
    this.dataHora = dataHora;
  }
}