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
  private Horario horario;
  
  // Retorna um número relacionado à essa consulta.
  @Override
  public int hashCode(){
    int hash = 3;
    hash = 97 * hash + this.id;
    hash = 97 * hash + Objects.hashCode(this.paciente);
    hash = 97 * hash + Objects.hashCode(this.horario);
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
    if (!Objects.equals(this.horario,other.horario)){
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
    this.horario  = consulta.getHorario();
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
                  Horario horario,
                  Paciente paciente)throws Exception{
    this.setId(id);
    this.setPaciente(paciente);
    this.setHorario(horario);
  }
  
  // Construtor default.
  public Consulta(){
    this.id       = 0;
    this.horario  = null;
    this.paciente = null;
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
 
  public void setHorario(Horario horario)throws Exception{
	if (horario == null)
      throw new Exception("Horário não fornecido.");
	this.horario = horario;
  }
  
  public Horario getHorario(){
	return this.horario;
  }
}