package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.ConvenioClinica;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados da relação entre o convênio e clínica.
 * @author Equipe do projeto Médico Fácil 
*/
public class ConvenioClinicas{
  
  private AdaptedPreparedStatement bd;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public ConvenioClinicas(AdaptedPreparedStatement bd)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
  }
  
  /**
   * @param convenioClinica
   * @throws Exception 
   */
  public void incluir(ConvenioClinica convenioClinica)throws Exception{
    if (convenioClinica == null)
      throw new Exception("Relação entre convênio e clínica não fornecida.");
    
    int idConvenio = convenioClinica.getIdConvenio();
    int idClinica  = convenioClinica.getIdClinica();
    
    if (this.isConvenioClinicaExistente(idConvenio,idClinica))
      throw new Exception("Essa clínica já atende esse convênio.");
    
    this.bd.prepareStatement("insert into ConvenioClinica_MF (idConvenio,idClinica) values (?,?)");
    this.bd.setInt(1,idConvenio);
    this.bd.setInt(2,idClinica);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param convenioClinica
   * @throws Exception 
   */
  public void alterar(ConvenioClinica convenioClinica)throws Exception{
    if (convenioClinica == null)
      throw new Exception("Relação entre convênio e clínica não fornecida.");
    
    int id         = convenioClinica.getId();
    int idConvenio = convenioClinica.getIdConvenio();
    int idClinica  = convenioClinica.getIdClinica();
    
    if (this.isConvenioClinicaExistente(idConvenio,idClinica,id))
      throw new Exception("Essa clínica já atende esse convênio.");
    
    this.bd.prepareStatement("update ConvenioClinica_MF set idClinica = ?,idConvenio = ? where id = ?");
    this.bd.setInt(1,idClinica);
    this.bd.setInt(2,idConvenio);
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
      
    this.bd.prepareStatement("delete from ConvenioClinica_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return ArrayList<ConvenioClinica>
   * @throws Exception 
   */
  private ArrayList<ConvenioClinica> getConvenioClinicas(ResultSet resultado)throws Exception{
    ArrayList<ConvenioClinica> lista = new ArrayList<ConvenioClinica>();
    while (resultado.next())
      lista.add(
        new ConvenioClinica(
          resultado.getInt(1),
          resultado.getInt(2),
          resultado.getInt(3)
        )      
      );
    return lista;
  }
  
  /**
   * @param condicao
   * @return ArrayList<ConvenioClinica>
   * @throws Exception 
   */
  public ArrayList<ConvenioClinica> getConvenioClinicas(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select id,idConvenio,idClinica from ConvenioClinica_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<ConvenioClinica> lista = this.getConvenioClinicas(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return ArrayList<ConvenioClinica>
   * @throws Exception 
   */
  public ArrayList<ConvenioClinica> getConvenioClinicas()throws Exception{
    this.bd.prepareStatement("select id,idConvenio,idClinica from ConvenioClinica_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<ConvenioClinica> lista = this.getConvenioClinicas(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há relação entre convênio e clínica com esse id.
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isConvenioClinicaExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from ConvenioClinica_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se existe relação entre convênio e clínica com os dados informados.
   * @param idConvenio
   * @param idClinica
   * @return boolean
   * @throws Exception 
   */
  public boolean isConvenioClinicaExistente(int idConvenio,int idClinica)throws Exception{
    this.bd.prepareStatement("select count(id) from ConvenioClinica_MF where idConvenio = ? and idClinica = ?");
    this.bd.setInt(1,idConvenio);
    this.bd.setInt(2,idClinica);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se existe relação entre convênio e clínica com os dados informados e 
   * se o seu id é diferente do id passado pelo parâmetro.
   * @param idConvenio
   * @param idClinica
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isConvenioClinicaExistente(int idConvenio,int idClinica,int id)throws Exception{
    this.bd.prepareStatement("select id from ConvenioClinica_MF where idConvenio = ? and idClinica = ?");
    this.bd.setInt(1,idConvenio);
    this.bd.setInt(2,idClinica);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isConvenioClinicaExistente;
    if (!resultado.first())
      isConvenioClinicaExistente = false;
    else
      if (resultado.getInt(1) == id)
        isConvenioClinicaExistente = false;
      else
        isConvenioClinicaExistente = true;
    resultado.close();
    return isConvenioClinicaExistente;
  }
  
  public int getId(int idConvenio,int idClinica)throws Exception{
	this.bd.prepareStatement(
      "select id from ConvenioClinica_MF where idConvenio = ? and idClinica = ?"
    );
	this.bd.setInt(1,idConvenio);
	this.bd.setInt(2,idClinica);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
      return 0; // Não existe relação entre convênio e clínica com esses dados.
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}