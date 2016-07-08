package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.Convenio;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados do convênio.
 * @author Equipe do projeto Médico Fácil 
*/
public class Convenios{
  
  private AdaptedPreparedStatement bd;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public Convenios(AdaptedPreparedStatement bd)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
  }
  
  /**
   * @param convenio
   * @throws Exception 
   */
  public void incluir(Convenio convenio)throws Exception{
    if (convenio == null)
      throw new Exception("Convênio não fornecido.");
    
    String nome = convenio.getNome();
    if (this.isConvenioExistente(nome))
      throw new Exception("Já existe convênio com esse nome.");
    
    this.bd.prepareStatement("insert into Convenio_MF (nome) values (?)");
    this.bd.setString(1,nome);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param convenio
   * @throws Exception 
   */
  public void alterar(Convenio convenio)throws Exception{
    if (convenio == null)
      throw new Exception("Convênio não fornecido.");
    
    int id = convenio.getId();
    String nome = convenio.getNome();
    if (this.isConvenioExistente(nome,id))
      throw new Exception("Já existe convênio com esse nome.");
    
    this.bd.prepareStatement("update Convenio_MF set nome = ? where id = ?");
    this.bd.setString(1,nome);
    this.bd.setInt(2,id);
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
      
    this.bd.prepareStatement("delete from Convenio_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return ArrayList<Convenio>
   * @throws Exception 
   */
  private ArrayList<Convenio> getConvenios(ResultSet resultado)throws Exception{
    ArrayList<Convenio> lista = new ArrayList<Convenio>();
    while (resultado.next())
      lista.add(
        new Convenio(
          resultado.getInt(1),
          resultado.getString(2)
        )      
      );
    return lista;
  }
  
  /**
   * @param condicao
   * @return ArrayList<Convenio>
   * @throws Exception 
   */
  public ArrayList<Convenio> getConvenios(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select * from Convenio_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Convenio> lista = this.getConvenios(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return ArrayList<Convenio>
   * @throws Exception 
   */
  public ArrayList<Convenio> getConvenios()throws Exception{
    this.bd.prepareStatement("select * from Convenio_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Convenio> lista = this.getConvenios(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há convênio com esse id.
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isConvenioExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from Convenio_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há convênio com esse nome.
   * @param nome
   * @return boolean
   * @throws Exception 
   */
  public boolean isConvenioExistente(String nome)throws Exception{
    this.bd.prepareStatement("select count(id) from Convenio_MF where nome = ?");
    this.bd.setString(1,nome);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há convênio com esse nome e com id diferente do passado como parâmetro.
   * @param nome
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isConvenioExistente(String nome,int id)throws Exception{
    this.bd.prepareStatement("select id from Convenio_MF where nome = ?");
    this.bd.setString(1,nome);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isConvenioExistente;
    if (!resultado.first())
      isConvenioExistente = false;
    else
      if (resultado.getInt(1) == id)
        isConvenioExistente = false;
      else
        isConvenioExistente = true;
    resultado.close();
    return isConvenioExistente;
  }
  
  public int getId(String nome)throws Exception{
	this.bd.prepareStatement(
	  "select id from Convenio_MF where nome = ?"
	);
    this.bd.setString(1,nome);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe convênio com esse nome.
    int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}
