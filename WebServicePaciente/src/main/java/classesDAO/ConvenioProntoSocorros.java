package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.ConvenioProntoSocorro;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados da relação entre o administrador e pronto socorro.
 * @author Equipe do projeto Médico Fácil 
*/
public class ConvenioProntoSocorros{
  
  private AdaptedPreparedStatement bd;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public ConvenioProntoSocorros(AdaptedPreparedStatement bd)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
  }
  
  /**
   * @param convenioProntoSocorro
   * @throws Exception 
   */
  public void incluir(ConvenioProntoSocorro convenioProntoSocorro)throws Exception{
    if (convenioProntoSocorro == null)
      throw new Exception("Relação entre convênio e pronto socorro não fornecida.");
    
    int idConvenio      = convenioProntoSocorro.getIdConvenio();
    int idProntoSocorro = convenioProntoSocorro.getIdProntoSocorro();
    
    if (this.isConvenioProntoSocorroExistente(idConvenio,idProntoSocorro))
      throw new Exception("Esse pronto socorro já atende esse convênio.");
    
    this.bd.prepareStatement("insert into ConvenioPs_MF (idConvenio,idPs) values (?,?)");
    this.bd.setInt(1,idConvenio);
    this.bd.setInt(2,idProntoSocorro);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param convenioProntoSocorro
   * @throws Exception 
   */
  public void alterar(ConvenioProntoSocorro convenioProntoSocorro)throws Exception{
    if (convenioProntoSocorro == null)
      throw new Exception("Relação entre convênio e pronto socorro não fornecida.");
    
    int id              = convenioProntoSocorro.getId();
    int idConvenio      = convenioProntoSocorro.getIdConvenio();
    int idProntoSocorro = convenioProntoSocorro.getIdProntoSocorro();
    
    if (this.isConvenioProntoSocorroExistente(idConvenio,idProntoSocorro,id))
      throw new Exception("Esse pronto socorro já atende esse convênio.");
    
    this.bd.prepareStatement("update ConvenioPs_MF set idPs = ?,idConvenio = ? where id = ?");
    this.bd.setInt(1,idProntoSocorro);
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
      
    this.bd.prepareStatement("delete from ConvenioPs_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  private ArrayList<ConvenioProntoSocorro> getConvenioProntoSocorros(ResultSet resultado)throws Exception{
    ArrayList<ConvenioProntoSocorro> lista = new ArrayList<ConvenioProntoSocorro>();
    while (resultado.next())
      lista.add(
        new ConvenioProntoSocorro(
          resultado.getInt(1),
          resultado.getInt(2),
          resultado.getInt(3)
        )      
      );
    return lista;
  }
  
  /**
   * @param condicao
   * @return ArrayList<ConvenioProntoSocorro>
   * @throws Exception 
   */
  public ArrayList<ConvenioProntoSocorro> getConvenioProntoSocorros(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select * from ConvenioPs_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<ConvenioProntoSocorro> lista = this.getConvenioProntoSocorros(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return ArrayList<ConvenioProntoSocorro>
   * @throws Exception 
   */
  public ArrayList<ConvenioProntoSocorro> getConvenioProntoSocorros()throws Exception{
    this.bd.prepareStatement("select * from ConvenioPs_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<ConvenioProntoSocorro> lista = this.getConvenioProntoSocorros(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se existe relação entre convênio e pronto socorro com esse id.
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isConvenioProntoSocorroExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from ConvenioPs_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se existe relação entre convênio e pronto socorro pelos dados informados.
   * @param idConvenio
   * @param idProntoSocorro
   * @return boolean
   * @throws Exception 
   */
  public boolean isConvenioProntoSocorroExistente(int idConvenio,int idProntoSocorro)throws Exception{
    this.bd.prepareStatement("select count(id) from ConvenioPs_MF where idConvenio = ? and idPs = ?");
    this.bd.setInt(1,idConvenio);
    this.bd.setInt(2,idProntoSocorro);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se existe relação entre convênio e pronto socorro pelos dados informados 
   * e se o seu id é diferente do passado pelo parâmetro.
   * @param idConvenio
   * @param idProntoSocorro
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isConvenioProntoSocorroExistente(int idConvenio,int idProntoSocorro,int id)throws Exception{
    this.bd.prepareStatement("select id from ConvenioPs_MF where idConvenio = ? and idPs = ?");
    this.bd.setInt(1,idConvenio);
    this.bd.setInt(2,idProntoSocorro);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isConvenioProntoSocorroExistente;
    if (!resultado.first())
      isConvenioProntoSocorroExistente = false;
    else
      if (resultado.getInt(1) == id)
        isConvenioProntoSocorroExistente = false;
      else
        isConvenioProntoSocorroExistente = true;
    resultado.close();
    return isConvenioProntoSocorroExistente;
  }
  
  public int getId(int idConvenio,int idPs)throws Exception{
    this.bd.prepareStatement(
	  "select id from ConvenioPs_MF where idConvenio = ? and idPs = ?"
	);
	this.bd.setInt(1,idConvenio);
	this.bd.setInt(2,idPs);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe relação entre convênio e clínica com esses dados.
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  } 
}