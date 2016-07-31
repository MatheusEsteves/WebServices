package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.Horario;
import classesDBO.MedicoClinica;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados do horário de atendimento médico.
 * @author Equipe do projeto Médico Fácil.
*/
public class Horarios {
  
  private AdaptedPreparedStatement bd;
  private Medicos medicos;
  private Clinicas clinicas;
  private MedicoClinicas medicoClinicas;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public Horarios(AdaptedPreparedStatement bd,Medicos medicos,Clinicas clinicas,MedicoClinicas medicoClinicas)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd       = bd;
    this.medicos  = medicos;
    this.clinicas = clinicas;
    this.medicoClinicas = medicoClinicas;
  }
  
  /**
   * @param horario
   * @throws Exception 
   */
  public void incluir(Horario horario)throws Exception{
    if (horario == null)
      throw new Exception("Estado não fornecido.");
    
    int idMedico     = this.medicos.getId(horario.getMedico().getCrm());
    int idClinica    = this.clinicas.getId(horario.getClinica().getNome());
    Date data        = horario.getData();
    Time horaInicio  = horario.getHoraInicio();
    Time horaFim     = horario.getHoraFim();
    
    if (this.isHorarioExistente(idMedico, data, horaInicio, horaFim))
      throw new Exception("Já existe esse horário para esse médico nesse dia.");
    
    int idMedicoClinica = this.medicoClinicas.getId(idMedico,idClinica);
    
    this.bd.prepareStatement("insert into Horario_MF (idMedicoClinica,data,horarioInicio,horarioFim) values (?,?,?,?)");
    this.bd.setInt(1,idMedicoClinica);
    this.bd.setDate(2,data);
    this.bd.setTime(3,horaInicio);
    this.bd.setTime(4,horaFim);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param horario
   * @throws Exception 
   */
  public void alterar(Horario horario)throws Exception{
    if (horario == null)
      throw new Exception("Estado não fornecido.");
    
    int id           = horario.getId();
    int idMedico     = this.medicos.getId(horario.getMedico().getCrm());
    int idClinica    = this.clinicas.getId(horario.getClinica().getNome());
    Date data        = horario.getData();
    Time horaInicio  = horario.getHoraInicio();
    Time horaFim     = horario.getHoraFim();
    
    if (this.isHorarioExistente(idMedico, data, horaInicio, horaFim,id))
      throw new Exception("Já existe esse horário para esse médico nesse dia.");
    
    int idMedicoClinica = this.medicoClinicas.getId(idMedico,idClinica);
    
    this.bd.prepareStatement("update Horario_MF set data = ?,horarioInicio = ?,horarioFim = ? where idMedicoClinica = ? and id = ?");
    this.bd.setDate(1,data);
    this.bd.setTime(2,horaInicio);
    this.bd.setTime(3,horaFim);
    this.bd.setInt(4,idMedicoClinica);
    this.bd.setInt(5,id);
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
      
    this.bd.prepareStatement("delete from Horario_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return
   * @throws Exception 
   */
  private ArrayList<Horario> getHorarios(ResultSet resultado)throws Exception{
    ArrayList<Horario> lista = new ArrayList<Horario>();
    while (resultado.next()){
      MedicoClinica medicoClinica = this.medicoClinicas.getMedicoClinicas("id = "+resultado.getInt(1)).get(0);
      lista.add(
        new Horario(
          resultado.getInt(2),
          medicoClinica.getMedico(),
          medicoClinica.getClinica(),
          resultado.getDate(3),
          resultado.getTime(4),
          resultado.getTime(5)
        )      
      );
    }
    return lista;
  }
  
  /**
   * @param condicao
   * @return
   * @throws Exception 
   */
  public ArrayList<Horario> getHorarios(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select idMedicoClinica,id,data,horarioInicio,horarioFim from Horario_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Horario> lista = this.getHorarios(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return
   * @throws Exception 
   */
  public ArrayList<Horario> getHorarios()throws Exception{
    this.bd.prepareStatement("select idMedicoClinica,id,data,horarioInicio,horarioFim from Horario_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Horario> lista = this.getHorarios(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há um horário de atendimento médico com esse id.
   * @param id
   * @return
   * @throws Exception 
   */
  public boolean isHorarioExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from Horario_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há um horário de atendimento médico pelos dados passados pelo parâmetro.
   * @param idMedico
   * @param diaSemana
   * @param horaInicio
   * @param horaFim
   * @return
   * @throws Exception 
   */
  public boolean isHorarioExistente(int idMedico,Date data,Time horaInicio,Time horaFim)throws Exception{
    this.bd.prepareStatement(
       "select count(id) from Horario_MF where idMedicoClinica in (select id from MedicoClinica_MF where idMedico = ?) and "+
       "data = ? and "+
       "("+
         "(? >= horarioInicio and ? < horarioFim) or "+
         "(? <= horarioFim and ? > horarioInicio) or "+
         "(? < horarioInicio and ? > horarioFim)"+
       ")"
    );
    this.bd.setInt(1,idMedico);
    this.bd.setDate(2,data);
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
   * Verifica se há um horário de atendimento médico pelos dados passados pelo parâmetro e
   * se o id dele é diferente do passado pelo parâmetro.
   * @param idMedico
   * @param diaSemana
   * @param horaInicio
   * @param horaFim
   * @param id
   * @return
   * @throws Exception 
   */
  public boolean isHorarioExistente(int idMedico,Date data,Time horaInicio,Time horaFim,int id)throws Exception{
	this.bd.prepareStatement(
	  "select id from Horario_MF where idMedicoClinica in (select id from MedicoClinica_MF where idMedico = ?) and "+
	  "data = ? and "+
	  "("+
	    "(? >= horarioInicio and ? < horarioFim) or "+
	    "(? <= horarioFim and ? > horarioInicio) or "+
	    "(? < horarioInicio and ? > horarioFim)"+
	  ")"
    );
    this.bd.setInt(1,idMedico);
    this.bd.setDate(2,data);
    this.bd.setTime(3,horaInicio);
    this.bd.setTime(4,horaInicio);
    this.bd.setTime(5,horaFim);
    this.bd.setTime(6,horaFim);
    this.bd.setTime(7,horaInicio);
    this.bd.setTime(8,horaFim);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isHorarioExistente;
    if (!resultado.first())
      isHorarioExistente = false;
    else
      if (resultado.getInt(1) == id)
        isHorarioExistente = false;
      else
        isHorarioExistente = true;
    resultado.close();
    return isHorarioExistente;
  }
  
  public int getId(int idMedico,int idClinica,Date data,Time horaInicio,Time horaFim)throws Exception{
	this.bd.prepareStatement(
      "select id from Horario_MF where idMedicoClinica in "+
	  "(select id from MedicoClinica_MF where idMedico = ? and idClinica = ?) and "+
      "data = ? and horarioInicio = ? and horarioFim = ?"
    );
	this.bd.setInt(1,idMedico);
	this.bd.setInt(2,idClinica);
	this.bd.setDate(3,data);
	this.bd.setTime(4,horaInicio);
	this.bd.setTime(5,horaFim);
	
    ResultSet resultado = this.bd.executeQuery();
    if (!resultado.first())
      return 0; // Não existe horário com esses dados.
    int id = resultado.getInt(1);
    resultado.close();
    return id;
  }
}
