package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.MedicoClinica;
import classesDBO.MedicoEspecialidade;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados da relação entre o médico e clínica.
 * @author Equipe do projeto Médico Fácil 
*/
public class MedicoEspecialidades{
  
  private AdaptedPreparedStatement bd;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public MedicoEspecialidades(AdaptedPreparedStatement bd)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd       = bd;
  }
  
  /**
   * @param medicoClinica
   * @throws Exception 
   */
  public void incluir(MedicoEspecialidade medicoEspecialidade)throws Exception{
    if (medicoEspecialidade == null)
      throw new Exception("Relação entre médico e especialidade não fornecida.");
    
    int idMedico        = medicoEspecialidade.getIdMedico();
    int idEspecialidade = medicoEspecialidade.getIdEspecialidade();
    
    if (this.isMedicoEspecialidadeExistente(idMedico,idEspecialidade))
      throw new Exception("Esse médico já atua nessa nessa especialidade.");
    
    this.bd.prepareStatement("insert into MedicoEspecialidade_MF (idMedico,idEspecialidade) values (?,?)");
    this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idEspecialidade);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param medicoClinica
   * @throws Exception 
   */
  public void alterar(MedicoEspecialidade medicoEspecialidade)throws Exception{
    if (medicoEspecialidade == null)
      throw new Exception("Relação entre médico e especialidade não fornecida.");
    
    int id              = medicoEspecialidade.getId();
    int idMedico        = medicoEspecialidade.getIdMedico();
    int idEspecialidade = medicoEspecialidade.getIdEspecialidade();
    
    if (this.isMedicoEspecialidadeExistente(idMedico,idEspecialidade,id))
      throw new Exception("Esse médico já atua nessa especialidade.");
    
    this.bd.prepareStatement("update MedicoEspecialidade_MF set idMedico = ?,idEspecialidade = ? where id = ?");
    this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idEspecialidade);
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
    
    this.bd.prepareStatement("delete from MedicoEspecialidade_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return ArrayList<MedicoClinica>
   * @throws Exception 
   */
  private ArrayList<MedicoEspecialidade> getMedicoEspecialidades(ResultSet resultado)throws Exception{
    ArrayList<MedicoEspecialidade> lista = new ArrayList<MedicoEspecialidade>();
    while (resultado.next())
      lista.add(
        new MedicoEspecialidade(
          resultado.getInt(1),
          resultado.getInt(2),
          resultado.getInt(3)
        )      
      );
    return lista;
  }
  
  /**
   * @param condicao
   * @return ArrayList<MedicoClinica>
   * @throws Exception 
   */
  public ArrayList<MedicoEspecialidade> getMedicoEspecialidades(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select id,idMedico,idEspecialidade from MedicoEspecialidade_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<MedicoEspecialidade> lista = this.getMedicoEspecialidades(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return ArrayList<MedicoClinica>
   * @throws Exception 
   */
  public ArrayList<MedicoEspecialidade> getMedicoEspecialidades()throws Exception{
    this.bd.prepareStatement("select id,idMedico,idEspecialidade from MedicoEspecialidade_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<MedicoEspecialidade> lista = this.getMedicoEspecialidades(resultado);
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
  public boolean isMedicoEspecialidadeExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from MedicoEspecialidade_MF where id = ?");
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
  public boolean isMedicoEspecialidadeExistente(int idMedico,int idEspecialidade)throws Exception{
    this.bd.prepareStatement("select count(id) from MedicoEspecialidade_MF where idMedico = ? and idEspecialidade = ?");
    this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idEspecialidade);
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
  public boolean isMedicoEspecialidadeExistente(int idMedico,int idEspecialidade,int id)throws Exception{
    this.bd.prepareStatement("select id from MedicoEspecialidade_MF where idMedico = ? and idEspecialidade = ?");
    this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idEspecialidade);
    
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
  
  public int getId(int idMedico,int idEspecialidade)throws Exception{
    this.bd.prepareStatement(
	 "select id from MedicoEspecialidade_MF where idMedico = ? and idEspecialidade = ?"
	);
    this.bd.setInt(1,idMedico);
	this.bd.setInt(2,idEspecialidade);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe relação entre médico e especialidade com esses dados.
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}