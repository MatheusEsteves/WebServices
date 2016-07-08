package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDAO.MedicoClinicas;
import classesDBO.Consulta;
import classesDBO.MedicoClinica;

import java.sql.ResultSet;
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
  private Medicos medicos;
  private Clinicas clinicas;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public Consultas(AdaptedPreparedStatement bd,MedicoClinicas medicoClinicas,Pacientes pacientes,Medicos medicos,Clinicas clinicas)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
    this.medicoClinicas = medicoClinicas;
    this.pacientes      = pacientes;
    this.medicos        = medicos;
    this.clinicas       = clinicas;
  }
  
  /**
   * @param consulta
   * @throws Exception 
   */
  public void incluir(Consulta consulta)throws Exception{
    if (consulta == null)
      throw new Exception("Consulta não fornecida.");
     
    int idPaciente      = consulta.getPaciente().getId();
    int idMedico        = this.medicos.getId(consulta.getMedico().getCrm());
    int idClinica       = this.clinicas.getId(consulta.getClinica().getNome());
    Timestamp dataHora  = consulta.getDataHora();
    int idMedicoClinica = this.medicoClinicas.getMedicoClinicas(
      "idMedico = "+idMedico+" and idClinica = "+idClinica
    ).get(0).getId();
    
    if (this.isConsultaExistentePorMedico(idMedicoClinica,dataHora))
      throw new Exception("Já existe consulta com esse médico marcada para esse horário.");
    if (this.isConsultaExistentePorPaciente(idPaciente,dataHora))
      throw new Exception("Já existe consulta com esse paciente marcada para esse horário.");
    
    this.bd.prepareStatement("insert into Consulta_MF (idMedicoClinica,idPaciente,dataHora) values (?,?,?)");
    this.bd.setInt(1,idMedicoClinica);
    this.bd.setInt(2,idPaciente);
    this.bd.setTimestamp(3,dataHora);
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
    
    int id              = consulta.getId();
    int idPaciente      = consulta.getPaciente().getId();
    int idMedico        = this.medicos.getId(consulta.getMedico().getCrm());
    int idClinica       = this.clinicas.getId(consulta.getClinica().getNome());
    Timestamp dataHora  = consulta.getDataHora();
    int idMedicoClinica = this.medicoClinicas.getMedicoClinicas(
      "idMedico = "+idMedico+" and idClinica = "+idClinica
    ).get(0).getId();
    
    if (this.isConsultaExistentePorMedico(idMedicoClinica,dataHora,id))
      throw new Exception("Já existe consulta com esse médico marcada para esse horário.");
    if (this.isConsultaExistentePorPaciente(idPaciente,dataHora,id))
      throw new Exception("Já existe consulta com esse paciente marcada para esse horário.");
    
    this.bd.prepareStatement("update Consulta_MF set idMedicoClinica = ?,dataHora = ? where id = ?");
    this.bd.setInt(1,idMedicoClinica);
    this.bd.setTimestamp(2,dataHora);
    this.bd.setInt(3,id);
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
      MedicoClinica medicoClinica = this.medicoClinicas.getMedicoClinicas("id = "+resultado.getInt(3)).get(0);
      lista.add(
        new Consulta(
          resultado.getInt(1),
          this.pacientes.getPacientes("id = "+resultado.getInt(2)).get(0),
          this.medicos.getMedicos("id = "+medicoClinica.getMedico().getId()).get(0),
          this.clinicas.getClinicas("id = "+medicoClinica.getClinica().getId()).get(0),
          resultado.getTimestamp(4)
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
    
    this.bd.prepareStatement("select id,idPaciente,idMedicoClinica,dataHora from Consulta_MF where "+condicao);
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
    this.bd.prepareStatement("select id,idPaciente,idMedicoClinica,dataHora from Consulta_MF");
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
  public boolean isConsultaExistentePorMedico(int idMedicoClinica,Timestamp dataHora)throws Exception{
    this.bd.prepareStatement("select count(id) from Consulta_MF where idMedicoClinica = ? and dataHora = ?");
    this.bd.setInt(1,idMedicoClinica);
    this.bd.setTimestamp(2,dataHora);
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
  public boolean isConsultaExistentePorMedico(int idMedicoClinica,Timestamp dataHora,int id)throws Exception{
    this.bd.prepareStatement("select id from Consulta_MF where idMedicoClinica = ? and dataHora = ?");
    this.bd.setInt(1,idMedicoClinica);
    this.bd.setTimestamp(2,dataHora);
    
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
  public boolean isConsultaExistentePorPaciente(int idPaciente,Timestamp dataHora)throws Exception{
    this.bd.prepareStatement("select count(id) from Consulta_MF where idPaciente = ? and dataHora = ?");
    this.bd.setInt(1,idPaciente);
    this.bd.setTimestamp(2,dataHora);
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
  public boolean isConsultaExistentePorPaciente(int idPaciente,Timestamp dataHora,int id)throws Exception{
    this.bd.prepareStatement("select id from Consulta_MF where idPaciente = ? and dataHora = ?");
    this.bd.setInt(1,idPaciente);
    this.bd.setTimestamp(2,dataHora);
    
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
  
  public int getId(String cpf,String crm,String clinica,Timestamp dataHora)throws Exception{
    this.bd.prepareStatement(
	  "select id from Consulta_MF where dataHora = ? and "+
      "idPaciente in (select id from Paciente_MF where cpf = ?) and "+
	  "idMedicoClinica in (select id from MedicoClinica_MF where "+
      "idMedico in (select id from Medico_MF where crm = ?) and "+
	  "idClinica in (select id from Clinica_MF where nome = ?))"
	);
	this.bd.setTimestamp(1,dataHora);
	this.bd.setString(2,cpf);
	this.bd.setString(3,crm);
	this.bd.setString(4,clinica);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe consulta com esse nome;
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}