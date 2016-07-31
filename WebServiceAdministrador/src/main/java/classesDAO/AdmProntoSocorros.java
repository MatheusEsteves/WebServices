package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.AdmProntoSocorro;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados da relação entre o administrador e pronto socorro.
 * @author Equipe do projeto Médico Fácil 
*/
public class AdmProntoSocorros{
  
  private AdaptedPreparedStatement bd;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public AdmProntoSocorros(AdaptedPreparedStatement bd)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
  }
  
  /**
   * @param admProntoSocorro
   * @throws Exception 
   */
  public void incluir(AdmProntoSocorro admProntoSocorro)throws Exception{
    if (admProntoSocorro == null)
      throw new Exception("Relação entre administrador e pronto socorro não fornecida.");
    
    int idAdm = admProntoSocorro.getIdAdm();
    int idProntoSocorro = admProntoSocorro.getIdProntoSocorro();
    
    if (this.isAdmProntoSocorroExistente(idAdm,idProntoSocorro))
      throw new Exception("Esse administrador já está administrando esse pronto socorro.");
    
    this.bd.prepareStatement("insert into AdmPs_MF (idAdm,idPs) values (?,?)");
    this.bd.setInt(1,idAdm);
    this.bd.setInt(2,idProntoSocorro);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param admProntoSocorro
   * @throws Exception 
   */
  public void alterar(AdmProntoSocorro admProntoSocorro)throws Exception{
    if (admProntoSocorro == null)
      throw new Exception("Relação entre administrador e pronto socorro não fornecida.");
    
    int id = admProntoSocorro.getId();
    int idAdm = admProntoSocorro.getIdAdm();
    int idProntoSocorro = admProntoSocorro.getIdProntoSocorro();
    
    if (this.isAdmProntoSocorroExistente(idAdm,idProntoSocorro,id))
      throw new Exception("Esse administrador já está administrando esse pronto socorro.");
    
    this.bd.prepareStatement("update AdmPs_MF set idAdm = ?,idPs = ? where id = ?");
    this.bd.setInt(1,idAdm);
    this.bd.setInt(2,idProntoSocorro);
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
    
    this.bd.prepareStatement("delete from AdmPs_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return
   * @throws Exception 
   */
  private ArrayList<AdmProntoSocorro> getAdmProntoSocorros(ResultSet resultado)throws Exception{
    ArrayList<AdmProntoSocorro> lista = new ArrayList<AdmProntoSocorro>();
    while (resultado.next())
      lista.add(
        new AdmProntoSocorro(
          resultado.getInt(1),
          resultado.getInt(2),
          resultado.getInt(3)
        )      
      );
    return lista;
  }
  
  /**
   * @param condicao
   * @return
   * @throws Exception 
   */
  public ArrayList<AdmProntoSocorro> getAdmProntoSocorros(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select id,idAdm,idPs from AdmPs_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<AdmProntoSocorro> lista = this.getAdmProntoSocorros(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return
   * @throws Exception 
   */
  public ArrayList<AdmProntoSocorro> getAdmProntoSocorros()throws Exception{
    this.bd.prepareStatement("select id,idAdm,idPs from AdmPs_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<AdmProntoSocorro> lista = this.getAdmProntoSocorros(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há uma relação entre administrador e pronto socorro com esse id.
   * @param id
   * @return
   * @throws Exception 
   */
  public boolean isAdmProntoSocorroExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from AdmPs_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma relação entre administrador e pronto socorro com esses dados.
   * @param idAdm
   * @param idProntoSocorro
   * @return
   * @throws Exception 
   */
  public boolean isAdmProntoSocorroExistente(int idAdm,int idProntoSocorro)throws Exception{
    this.bd.prepareStatement("select count(id) from AdmPs_MF where idAdm = ? and idPs = ?");
    this.bd.setInt(1,idAdm);
    this.bd.setInt(2,idProntoSocorro);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma relação entre administrador e pronto socorro com esses dados
   * e com id diferente do passado como parâmetro.
   * @param idAdm
   * @param idProntoSocorro
   * @param id
   * @return
   * @throws Exception 
   */
  public boolean isAdmProntoSocorroExistente(int idAdm,int idProntoSocorro,int id)throws Exception{
    this.bd.prepareStatement("select id from AdmPs_MF where idAdm = ? and idPs = ?");
    this.bd.setInt(1,idAdm);
    this.bd.setInt(2,idProntoSocorro);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isAdmProntoSocorroExistente;
    if (!resultado.first())
      isAdmProntoSocorroExistente = false;
    else
      if (resultado.getInt(1) == id)
        isAdmProntoSocorroExistente = false;
      else
        isAdmProntoSocorroExistente = true;
    resultado.close();
    return isAdmProntoSocorroExistente;
  }
  
  public int getId(int idMedico,int idPs)throws Exception{
	this.bd.prepareStatement(
	  "select id from MedicoPs_MF where idMedico = ? and idPs = ?"
	);
	this.bd.setInt(1,idMedico);
	this.bd.setInt(2,idPs);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
      return 0; // Não existe relação entre administrador e pronto socorro com esses dados.
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}