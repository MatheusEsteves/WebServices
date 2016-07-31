package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.MedicoClinica;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados da relação entre o médico e clínica.
 * @author Equipe do projeto Médico Fácil 
*/
public class MedicoClinicas{
  
  private AdaptedPreparedStatement bd;
  private Medicos medicos;
  private Clinicas clinicas;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public MedicoClinicas(AdaptedPreparedStatement bd,Medicos medicos,Clinicas clinicas)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd       = bd;
    this.medicos  = medicos;
    this.clinicas = clinicas;
  }
  
  /**
   * @param medicoClinica
   * @throws Exception 
   */
  public void incluir(MedicoClinica medicoClinica)throws Exception{
    if (medicoClinica == null)
      throw new Exception("Relação entre médico e clínica não fornecida.");
    
    int idMedico  = this.medicos.getId(medicoClinica.getMedico().getCrm());
    int idClinica = this.clinicas.getId(medicoClinica.getClinica().getNome());
    
    if (this.isMedicoClinicaExistente(idMedico,idClinica))
      throw new Exception("Esse médico já atua nessa clínica.");
    
    this.bd.prepareStatement("insert into MedicoClinica_MF (idMedico,idClinica,senha) values (?,?,?)");
    this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idClinica);
    this.bd.setString(3,medicoClinica.getSenha());
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param medicoClinica
   * @throws Exception 
   */
  public void alterar(MedicoClinica medicoClinica)throws Exception{
    if (medicoClinica == null)
      throw new Exception("Relação entre médico e clínica não fornecida.");
    
    int id        = medicoClinica.getId();
    int idMedico  = this.medicos.getId(medicoClinica.getMedico().getCrm());
    int idClinica = this.clinicas.getId(medicoClinica.getClinica().getNome());
    
    if (this.isMedicoClinicaExistente(idMedico,idClinica,id))
      throw new Exception("Esse médico já atua nessa clínica.");
    
    this.bd.prepareStatement("update MedicoClinica_MF set idMedico = ?,idClinica = ?,senha = ? where id = ?");
    this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idClinica);
    this.bd.setString(3,medicoClinica.getSenha());
    this.bd.setInt(4,id);
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
    
    this.bd.prepareStatement("delete from MedicoClinica_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return ArrayList<MedicoClinica>
   * @throws Exception 
   */
  private ArrayList<MedicoClinica> getMedicoClinicas(ResultSet resultado)throws Exception{
    ArrayList<MedicoClinica> lista = new ArrayList<MedicoClinica>();
    while (resultado.next())
      lista.add(
        new MedicoClinica(
          resultado.getInt(1),
          this.medicos.getMedicos("id = "+resultado.getInt(2)).get(0),
          this.clinicas.getClinicas("id = "+resultado.getInt(3)).get(0),
          resultado.getString(4)
        )      
      );
    return lista;
  }
  
  /**
   * @param condicao
   * @return ArrayList<MedicoClinica>
   * @throws Exception 
   */
  public ArrayList<MedicoClinica> getMedicoClinicas(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select id,idMedico,idClinica,senha from MedicoClinica_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<MedicoClinica> lista = this.getMedicoClinicas(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return ArrayList<MedicoClinica>
   * @throws Exception 
   */
  public ArrayList<MedicoClinica> getMedicoClinicas()throws Exception{
    this.bd.prepareStatement("select id,idMedico,idClinica,senha from MedicoClinica_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<MedicoClinica> lista = this.getMedicoClinicas(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há uma relação entre médico e clínica com o id passado como
   * parâmetro.
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isMedicoClinicaExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from MedicoClinica_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma relação entre médico e clínica pelos dados passados como
   * parâmetro.
   * @param idMedico
   * @param idClinica
   * @return boolean
   * @throws Exception 
   */
  public boolean isMedicoClinicaExistente(int idMedico,int idClinica)throws Exception{
    this.bd.prepareStatement("select count(id) from MedicoClinica_MF where idMedico = ? and idClinica = ?");
    this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idClinica);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma relação entre médico e clínica pelos dados passados como parâmetro
   * e se o id dela é diferente do passado pelo parâmetro.
   * @param idMedico
   * @param idClinica
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isMedicoClinicaExistente(int idMedico,int idClinica,int id)throws Exception{
    this.bd.prepareStatement("select id from MedicoClinica_MF where idMedico = ? and idClinica = ?");
    this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idClinica);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isMedicoClinicaExistente;
    if (!resultado.first())
      isMedicoClinicaExistente = false;
    else
      if (resultado.getInt(1) == id)
        isMedicoClinicaExistente = false;
      else
        isMedicoClinicaExistente = true;
    resultado.close();
    return isMedicoClinicaExistente;
  }
  
  public int getId(int idMedico,int idClinica)throws Exception{
    this.bd.prepareStatement(
	 "select id from MedicoClinica_MF where idMedico = ? and idClinica = ?"
	);
    this.bd.setInt(1,idMedico);
	this.bd.setInt(2,idClinica);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe relação entre médico e clínica com esses dados.
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}