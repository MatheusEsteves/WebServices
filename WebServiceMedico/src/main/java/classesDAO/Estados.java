package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.Estado;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Relizará operações no banco de dados com os dados do estado brasileiro.
 * @author Equipe do projeto Médico Fácil 
*/
public class Estados{
   
  private AdaptedPreparedStatement bd;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public Estados(AdaptedPreparedStatement bd)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
  }
  
  /**
   * @param estado
   * @throws Exception 
   */
  public void incluir(Estado estado)throws Exception{
    if (estado == null)
      throw new Exception("Estado não fornecido.");
    
    String nome  = estado.getNome();
    String sigla = estado.getSigla();
    
    if (this.isEstadoExistentePorNome(nome))
      throw new Exception("Já existe estado com esse nome.");
    if (this.isEstadoExistentePorSigla(sigla))
      throw new Exception("Já existe estado com essa sigla.");
    
    this.bd.prepareStatement("insert into Estado_MF (nome,uf) values (?,?)");
    this.bd.setString(1,nome);
    this.bd.setString(2,sigla);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param estado
   * @throws Exception 
   */
  public void alterar(Estado estado)throws Exception{
    if (estado == null)
      throw new Exception("Estado não fornecido.");
    
    int id       = estado.getId();
    String nome  = estado.getNome();
    String sigla = estado.getSigla();
    
    if (this.isEstadoExistentePorNome(nome,id))
      throw new Exception("Já existe estado com esse nome.");
    if (this.isEstadoExistentePorSigla(sigla,id))
      throw new Exception("Já existe estado com essa sigla.");
    
    this.bd.prepareStatement("update Estado_MF set nome = ?,uf = ? where id = ?");
    this.bd.setString(1,nome);
    this.bd.setString(2,sigla);
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
      
    this.bd.prepareStatement("delete from Estado_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return ArrayList<Estado>
   * @throws Exception 
   */
  private ArrayList<Estado> getEstados(ResultSet resultado)throws Exception{
    ArrayList<Estado> lista = new ArrayList<Estado>();
    while (resultado.next())
      lista.add(
        new Estado(
          resultado.getInt(1),
          resultado.getString(2),
          resultado.getString(3)
        )      
      );
    return lista;
  }
  
  /**
   * @param condicao
   * @return ArrayList<Estado>
   * @throws Exception 
   */
  public ArrayList<Estado> getEstados(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select id,nome,uf from Estado_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Estado> lista = this.getEstados(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return ArrayList<Estado>
   * @throws Exception 
   */
  public ArrayList<Estado> getEstados()throws Exception{
    this.bd.prepareStatement("select id,nome,uf from Estado_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Estado> lista = this.getEstados(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há um estado com esse nome.
   * @param nome
   * @return boolean
   * @throws Exception 
   */
  public boolean isEstadoExistentePorNome(String nome)throws Exception{
    this.bd.prepareStatement("select count(id) from Estado_MF where nome = ?");
    this.bd.setString(1,nome);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há um estado com esse nome e com id diferente do passado por parâmetro.
   * @param nome
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isEstadoExistentePorNome(String nome,int id)throws Exception{
    this.bd.prepareStatement("select id from Estado_MF where nome = ?");
    this.bd.setString(1,nome);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isEstadoExistente;
    if (!resultado.first())
      isEstadoExistente = false;
    else
      if (resultado.getInt(1) == id)
        isEstadoExistente = false;
      else
        isEstadoExistente = true;
    resultado.close();
    return isEstadoExistente;
  }
  
  /**
   * Verifica se há um estado com essa sigla.
   * @param sigla
   * @return boolean
   * @throws Exception 
   */
  public boolean isEstadoExistentePorSigla(String sigla)throws Exception{
    this.bd.prepareStatement("select count(id) from Estado_MF where uf = ?");
    this.bd.setString(1,sigla);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há um estado com essa sigla e com id diferente do passado por parâmetro.
   * @param sigla
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isEstadoExistentePorSigla(String sigla,int id)throws Exception{
    this.bd.prepareStatement("select id from Estado_MF where uf = ?");
    this.bd.setString(1,sigla);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isEstadoExistente;
    if (!resultado.first())
      isEstadoExistente = false;
    else
      if (resultado.getInt(1) == id)
        isEstadoExistente = false;
      else
        isEstadoExistente = true;
    resultado.close();
    return isEstadoExistente;
  }
  
  /**
   * Verifica se há um estado com esse id.
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isEstadoExistentePorId(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from Estado_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  public int getId(String uf)throws Exception{
    this.bd.prepareStatement(
	  "select id from Estado_MF where uf = ?"
	);
    this.bd.setString(1,uf);
    ResultSet resultado = this.bd.executeQuery();
    if (!resultado.first())
	  return 0; // Não existe estado com esse uf.
    int id = resultado.getInt(1);
    resultado.close();
    return id;
  }
}