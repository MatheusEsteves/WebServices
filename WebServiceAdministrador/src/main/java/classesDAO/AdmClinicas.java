package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.AdmClinica;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados da relação entre o administrador e clínica.
 * @author Equipe do projeto Médico Fácil 
*/
public class AdmClinicas{
  
  private AdaptedPreparedStatement bd;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public AdmClinicas(AdaptedPreparedStatement bd)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
  }
  
  /**
   * @param admClinica
   * @throws Exception 
   */
  public void incluir(AdmClinica admClinica)throws Exception{
    if (admClinica == null)
      throw new Exception("Relação entre administrador e clínica não fornecida.");
    
    int idAdm     = admClinica.getIdAdm();
    int idClinica = admClinica.getIdClinica();
    
    if (this.isAdmClinicaExistente(idAdm, idClinica))
      throw new Exception("Esse administrador já está administrando essa clínica.");
    
    this.bd.prepareStatement("insert into AdmClinica_MF (idAdm,idClinica) values (?,?)");
    this.bd.setInt(1,idAdm);
    this.bd.setInt(2,idClinica);
    this.bd.executeUpdate();
    this.bd.commit();
  }
 
  /**
   * 
   * @param admClinica
   * @throws Exception 
   */
  public void alterar(AdmClinica admClinica)throws Exception{
    if (admClinica == null)
      throw new Exception("Relação entre administrador e clínica não fornecida.");
    
    int id        = admClinica.getId();
    int idAdm     = admClinica.getIdAdm();
    int idClinica = admClinica.getIdClinica();
    
    if (this.isAdmClinicaExistente(idAdm, idClinica, id))
      throw new Exception("Esse administrador já está administrando essa clínica.");
    
    this.bd.prepareStatement("update AdmClinica_MF set idAdm = ?,idClinica = ? where id = ?");
    this.bd.setInt(1,idAdm);
    this.bd.setInt(2,idClinica);
    this.bd.setInt(3,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * 
   * @param id
   * @throws Exception 
   */
  public void excluir(int id)throws Exception{
    if (id <= 0)
      throw new Exception("Id inválido para exclusão.");
    
    this.bd.prepareStatement("delete from AdmClinica_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * 
   * @param resultado
   * @return
   * @throws Exception 
   */
  private ArrayList<AdmClinica> getAdmClinicas(ResultSet resultado)throws Exception{
    ArrayList<AdmClinica> lista = new ArrayList<AdmClinica>();
    while (resultado.next())
      lista.add(
        new AdmClinica(
          resultado.getInt(1),
          resultado.getInt(2),
          resultado.getInt(3)
        )      
      );
    return lista;
  }
  
  /**
   * 
   * @param condicao
   * @return
   * @throws Exception 
   */
  public ArrayList<AdmClinica> getAdmClinicas(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select id,idAdm,idClinica from AdmClinica_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<AdmClinica> lista = this.getAdmClinicas(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * 
   * @return
   * @throws Exception 
   */
  public ArrayList<AdmClinica> getAdmClinicas()throws Exception{
    this.bd.prepareStatement("select id,idAdm,idClinica from AdmClinica_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<AdmClinica> lista = this.getAdmClinicas(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há relação entre administrador e clínica com esse id.
   * @param id
   * @return
   * @throws Exception 
   */
  public boolean isAdmClinicaExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from AdmClinica_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há relação entre administrador e clínica com esses dados.
   * @param idAdm
   * @param idClinica
   * @return
   * @throws Exception 
   */
  public boolean isAdmClinicaExistente(int idAdm,int idClinica)throws Exception{
    this.bd.prepareStatement("select count(id) from AdmClinica_MF where idAdm = ? and idClinica = ?");
    this.bd.setInt(1,idAdm);
    this.bd.setInt(2,idClinica);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há relação entre administrador e clínica com esses dados 
   * e se o seu id é diferente do passado como parâmetro.
   * @param idAdm
   * @param idClinica
   * @param id
   * @return
   * @throws Exception 
   */
  public boolean isAdmClinicaExistente(int idAdm,int idClinica,int id)throws Exception{
    this.bd.prepareStatement("select id from AdmClinica_MF where idAdm = ? and idClinica = ?");
    this.bd.setInt(1,idAdm);
    this.bd.setInt(2,idClinica);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isAdmClinicaExistente;
    if (!resultado.first())
      isAdmClinicaExistente = false;
    else
      if (resultado.getInt(1) == id)
        isAdmClinicaExistente = false;
      else
        isAdmClinicaExistente = true;
    resultado.close();
    return isAdmClinicaExistente;
  }
  
  public int getId(int idAdm,int idClinica)throws Exception{
	this.bd.prepareStatement(
	  "select id from AdmClinica_MF where idAdm = ? and idClinica = ?"
    );
	this.bd.setInt(1,idAdm);
    this.bd.setInt(2,idClinica);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe relação entre administrador e clínica com esses dados.
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}