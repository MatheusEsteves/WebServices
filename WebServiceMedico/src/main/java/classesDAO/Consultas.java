package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDAO.MedicoClinicas;
import classesDBO.Consulta;
import classesDBO.Horario;
import classesDBO.MedicoClinica;

import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Relizará operações no banco de dados com os dados da consulta.
 * @author Equipe do projeto Médico Fácil 
*/
public class Consultas{
  
  private AdaptedPreparedStatement bd;
  private MedicoClinicas medicoClinicas;
  private Pacientes pacientes;
  private Horarios horarios;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public Consultas(AdaptedPreparedStatement bd,MedicoClinicas medicoClinicas,Pacientes pacientes,Horarios horarios)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
    this.medicoClinicas = medicoClinicas;
    this.pacientes      = pacientes;
    this.horarios       = horarios;
  }
  
  /**
   * @param consulta
   * @throws Exception 
   */
  public void incluir(Consulta consulta)throws Exception{
    if (consulta == null)
      throw new Exception("Consulta não fornecida.");
     
    int idPaciente  = consulta.getPaciente().getId();
    Horario horario = consulta.getHorario(); 
    int idHorario   = horario.getId();
    
    if (this.isConsultaExistentePorMedico(horario))
      throw new Exception("Já existe consulta com esse médico marcada para esse horário.");
    if (this.isConsultaExistentePorPaciente(horario,idPaciente))
      throw new Exception("Já existe consulta com esse paciente marcada para esse horário.");
    
    this.bd.prepareStatement("insert into Consulta_MF (idHorario,idPaciente) values (?,?)");
    this.bd.setInt(1,idHorario);
    this.bd.setInt(2,idPaciente);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param consulta
   * @throws Exception 
   */
  public void alterar(Consulta consulta)throws Exception{
    if (consulta == null)
      throw new Exception("Consulta não fornecida.");
    
    int id          = consulta.getId();
    int idPaciente  = consulta.getPaciente().getId();
    Horario horario = consulta.getHorario();
    int idHorario   = horario.getId();
    
    if (this.isConsultaExistentePorMedico(horario,id))
      throw new Exception("Já existe consulta com esse médico marcada para esse horário.");
    if (this.isConsultaExistentePorPaciente(horario,idPaciente,id))
      throw new Exception("Já existe consulta com esse paciente marcada para esse horário.");
    
    this.bd.prepareStatement("update Consulta_MF set idHorario = ? where idPaciente = ?");
    this.bd.setInt(1,idHorario);
    this.bd.setInt(2,idPaciente);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param id
   * @throws Exception 
   */
  public void excluir(int id)throws Exception{
    if (id <= 0)
      throw new Exception("Id inválido para exclusão.");  
      
    this.bd.prepareStatement("delete from Consulta_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return ArrayList<Consulta>
   * @throws Exception 
   */
  private ArrayList<Consulta> getConsultas(ResultSet resultado)throws Exception{
    ArrayList<Consulta> lista = new ArrayList<Consulta>();
    while (resultado.next()){
      lista.add(
        new Consulta(
          resultado.getInt(1),
          this.horarios.getHorarios("id = "+resultado.getInt(2)).get(0),
          this.pacientes.getPacientes("id = "+resultado.getInt(3)).get(0)
        )      
      );
    }
    return lista;
  }
  
  /**
   * @param condicao
   * @return ArrayList<Consulta>
   * @throws Exception 
   */
  public ArrayList<Consulta> getConsultas(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select id,idHorario,idPaciente from Consulta_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Consulta> lista = this.getConsultas(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return ArrayList<Consulta>
   * @throws Exception 
   */
  public ArrayList<Consulta> getConsultas()throws Exception{
    this.bd.prepareStatement("select id,idHorario,idPaciente from Consulta_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Consulta> lista = this.getConsultas(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há uma consulta com esse id.
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isConsultaExistentePorId(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from Consulta_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma consulta nesse horário para esse determinado médico.
   * @param idMedicoClinica
   * @param dataHora
   * @return boolean
   * @throws Exception 
   */
  public boolean isConsultaExistentePorMedico(Horario horario)throws Exception{
    this.bd.prepareStatement("select count(id) from Consulta_MF where idHorario = ?");
    this.bd.setInt(1,horario.getId());
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma consulta nesse horário para esse determinado médico.
   * @param idMedicoClinica
   * @param dataHora
   * @return boolean
   * @throws Exception 
   */
  public boolean isConsultaExistentePorMedico(int idHorario)throws Exception{
    this.bd.prepareStatement("select count(id) from Consulta_MF where idHorario = ?");
    this.bd.setInt(1,idHorario);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma consulta nesse horário para esse determinado médico e com 
   * id diferente do passado como parâmetro.
   * @param idMedicoClinica
   * @param dataHora
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isConsultaExistentePorMedico(Horario horario,int id)throws Exception{
    this.bd.prepareStatement("select id from Consulta_MF where idHorario = ?");
    this.bd.setInt(1,horario.getId());
    ResultSet resultado = this.bd.executeQuery();
    boolean isConsultaExistente;
    if (!resultado.first())
      isConsultaExistente = false;
    else
      if (resultado.getInt(1) == id)
        isConsultaExistente = false;
      else
        isConsultaExistente = true;
    resultado.close();
    return isConsultaExistente;
  }
  
  /**
   * Verifica se há uma consulta nesse horário para esse determinado paciente.
   * @param idPaciente
   * @param dataHora
   * @return boolean
   * @throws Exception 
   */
  public boolean isConsultaExistentePorPaciente(Horario horario,int idPaciente)throws Exception{
    this.bd.prepareStatement(
      "select count(id) from Consulta_MF where idPaciente = ? and idHorario in ("+
      "select id from Horario_MF where data = ? and "+
      "("+
        "(? >= horarioInicio and ? < horarioFim) or "+
        "(? <= horarioFim and ? > horarioInicio) or "+
        "(? < horarioInicio and ? > horarioFim)"+
      "))"	  
    );
    Time horaInicio = horario.getHoraInicio();
    Time horaFim    = horario.getHoraFim();
    this.bd.setInt(1,idPaciente);
    this.bd.setDate(2,horario.getData());
    this.bd.setTime(3,horaInicio);
    this.bd.setTime(4,horaInicio);
    this.bd.setTime(5,horaFim);
    this.bd.setTime(6,horaFim);
    this.bd.setTime(7,horaInicio);
    this.bd.setTime(8,horaFim);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma consulta nesse horário para esse determinado paciente e 
   * se o seu id é diferente do passado como parâmetro.
   * @param idPaciente
   * @param dataHora
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isConsultaExistentePorPaciente(Horario horario,int idPaciente,int id)throws Exception{
	this.bd.prepareStatement(
	  "select count(id) from Consulta_MF where idPaciente = ? and idHorario in ("+
	  "select id from Horario_MF where data = ? and "+
	  "("+
		 "(? >= horarioInicio and ? < horarioFim) or "+
		 "(? <= horarioFim and ? > horarioInicio) or "+
		 "(? < horarioInicio and ? > horarioFim)"+
	  "))"	   
	);
    Time horaInicio = horario.getHoraInicio();
	Time horaFim    = horario.getHoraFim();
	this.bd.setInt(1,idPaciente);
	this.bd.setDate(2,horario.getData());
	this.bd.setTime(3,horaInicio);
	this.bd.setTime(4,horaInicio);
	this.bd.setTime(5,horaFim);
	this.bd.setTime(6,horaFim);
	this.bd.setTime(7,horaInicio);
	this.bd.setTime(8,horaFim);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isConsultaExistente;
    if (!resultado.first())
      isConsultaExistente = false;
    else
      if (resultado.getInt(1) == id)
        isConsultaExistente = false;
      else
        isConsultaExistente = true;
    resultado.close();
    return isConsultaExistente;
  }
}