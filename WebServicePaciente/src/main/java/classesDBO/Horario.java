package classesDBO;

import classesValidacao.Validacao;

import java.io.Serializable;
import java.sql.Time;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Representa um horário de um determinado médico para as suas atividades.
 * @author Equipe do projeto Médico Fácil
*/
public class Horario implements Cloneable,Serializable{
 
  private int id;
  private Medico medico;
  private Clinica clinica;
  private String diaSemana;
  @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="HH:mm")
  private Time horaInicio;
  @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="HH:mm")
  private Time horaFim;
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + this.id;
    hash = 67 * hash + Objects.hashCode(this.medico);
    hash = 67 * hash + Objects.hashCode(this.clinica);
    hash = 67 * hash + Objects.hashCode(this.diaSemana);
    hash = 67 * hash + Objects.hashCode(this.horaInicio);
    hash = 67 * hash + Objects.hashCode(this.horaFim);
    return hash;
  }

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
    final Horario other = (Horario) obj;
    if (this.id != other.id) {
        return false;
    }
    if (!Objects.equals(this.medico, other.medico)) {
        return false;
    }
    if (!Objects.equals(this.clinica, other.clinica)) {
        return false;
    }
    if (!Objects.equals(this.diaSemana, other.diaSemana)) {
        return false;
    }
    if (!Objects.equals(this.horaInicio, other.horaInicio)) {
        return false;
    }
    if (!Objects.equals(this.horaFim, other.horaFim)) {
        return false;
    }
    return true;
  }
  
  @Override
  public Horario clone(){
    Horario horario = null;
    try{
      horario = new Horario(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return horario;
  }
  
  public Horario(Horario horario)throws Exception{
    if (horario == null)
      throw new Exception("Horário não fornecido em construtor de cópia.");
    this.id          = horario.getId();
    this.medico      = horario.getMedico();
    this.clinica     = horario.getClinica();
    this.diaSemana   = horario.getDiaSemana();
    this.horaInicio  = horario.getHoraInicio();
    this.horaFim     = horario.getHoraFim();
  }
  
  public Horario(int id, 
                 Medico medico,
                 Clinica clinica,
                 String diaSemana,
                 Time horaInicio,
                 Time horaFim)throws Exception{
    this.setId(id);
    this.setMedico(medico);
    this.setClinica(clinica);
    this.setDiaSemana(diaSemana);
    this.setHoraInicio(horaInicio);
    this.setHoraFim(horaFim);
  }
  
  public Horario(){
    this.id          = 0; 
    this.medico      = null;
    this.clinica     = null;
    this.diaSemana   = "";
    this.horaInicio  = null;
    this.horaFim     = null;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id)throws Exception{
    if (id < 0)
      throw new Exception("Id do horário inválido.");
    this.id = id;
  }

  public Medico getMedico() {
    return this.medico;
  }
  
  public Clinica getClinica(){
	return this.clinica;
  }

  public void setMedico(Medico medico)throws Exception{
    if (medico == null)
      throw new Exception("Médico não fornecido.");
    this.medico = medico;
  }
  
  public void setClinica(Clinica clinica)throws Exception{
	if (clinica == null)
	  throw new Exception("Clínica não fornecida.");
	this.clinica = clinica; 
  }

  public String getDiaSemana() {
    return this.diaSemana;
  }

  public void setDiaSemana(String diaSemana)throws Exception{
    if (!Validacao.isDiaSemanaValido(diaSemana))
      throw new Exception("Dia de semana inválido.");
    this.diaSemana = diaSemana.toUpperCase();
  }

  @JsonSerialize(using=CustomTimeSerializer.class)
  public Time getHoraInicio() {
    return horaInicio;
  }

  @JsonDeserialize(using=CustomTimeDeserializer.class)
  public void setHoraInicio(Time horaInicio)throws Exception{
    if (this.horaFim != null)
      if (!Validacao.isIntervaloHorariosValido(horaInicio,this.horaFim))
        throw new Exception("Horário de início inválido.");
    if (horaInicio == null)
      throw new Exception("Horário de início inválido.");
    this.horaInicio = horaInicio;
  }

  @JsonSerialize(using=CustomTimeSerializer.class)
  public Time getHoraFim() {
    return horaFim;
  }

  @JsonDeserialize(using=CustomTimeDeserializer.class)
  public void setHoraFim(Time horaFim)throws Exception{
    if (this.horaInicio != null)
      if (!Validacao.isIntervaloHorariosValido(this.horaInicio,horaFim))
        throw new Exception("Horário de término inválido.");
    if (horaFim == null)
      throw new Exception("Horário de término inválido.");
    this.horaFim = horaFim;
  }
  
  public String toString(){
    return this.horaInicio.toString() + " " + this.horaFim.toString();
  }
}
