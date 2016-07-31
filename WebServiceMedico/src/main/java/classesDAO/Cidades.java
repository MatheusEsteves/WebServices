package classesDAO;
import classesBD.AdaptedPreparedStatement;
import classesDBO.Cidade;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados da cidade.
 * @author Equipe do projeto Médico Fácil 
*/
public class Cidades{

  private AdaptedPreparedStatement bd;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public Cidades(AdaptedPreparedStatement bd)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
  }
  
  /**
   * @param cidade
   * @throws Exception 
   */
  public void incluir(Cidade cidade)throws Exception{
    if (cidade == null)
      throw new Exception("Cidade não fornecida para inclusão.");
    
    String nome  = cidade.getNome();
    int idEstado = cidade.getIdEstado();
    
    if (this.isCidadeExistente(nome,idEstado))
      throw new Exception("Já existe uma cidade cadastrada nesse estado com esse nome.");
    
    this.bd.prepareStatement("insert into Cidade_MF (idEstado,nome) values (?,?)");
    this.bd.setInt(1,idEstado);
    this.bd.setString(2,nome);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param cidade
   * @throws Exception 
   */
  public void alterar(Cidade cidade)throws Exception{
    if (cidade == null)
      throw new Exception("Cidade não fornecida para alteração.");
    
    int id       = cidade.getId();
    String nome  = cidade.getNome();
    int idEstado = cidade.getIdEstado();
    
    if (this.isCidadeExistente(nome,idEstado,id))
      throw new Exception("Já existe uma cidade cadastrada nesse estado com esse nome.");
    
    this.bd.prepareStatement("update Cidade_MF set idEstado = ?,nome = ? where id = ?");
    this.bd.setInt(1,idEstado);
    this.bd.setString(2,nome);
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
      
    this.bd.prepareStatement("delete from Cidade_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return ArrayList<Cidade>
   * @throws Exception
   */
  private ArrayList<Cidade> getCidades(ResultSet resultado)throws Exception{
    ArrayList<Cidade> lista = new ArrayList<Cidade>();
    while (resultado.next())
      lista.add(
        new Cidade(
          resultado.getInt(1),
          resultado.getInt(2),
          resultado.getString(3)
        )      
      );
    return lista;
  }
  
  /**
   * @param condicao
   * @return ArrayList<Cidade>
   * @throws Exception 
   */
  public ArrayList<Cidade> getCidades(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select id,idEstado,nome from Cidade_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Cidade> lista = this.getCidades(resultado);
    resultado.close();
    return lista;
  }   
  
  /**
   * @return ArrayList<Cidade>
   * @throws Exception 
   */
  public ArrayList<Cidade> getCidades()throws Exception{
    this.bd.prepareStatement("select id,idEstado,nome from Cidade_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Cidade> lista = this.getCidades(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há cidade com esse id.
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isCidadeExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from Cidade_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se já existe cidade esse nome nesse mesmo estado.
   * Não deve haver cidades com mesmo nome localizadas no mesmo estado.
   * Pode haver cidades com mesmo nome localizadas em estados diferentes.
   * @param nome
   * @return boolean
   * @throws Exception 
   */
  public boolean isCidadeExistente(String nome,int idEstado)throws Exception{
    this.bd.prepareStatement("select count(id) from Cidade_MF where nome = ? and idEstado = ?");
    this.bd.setString(1,nome);
    this.bd.setInt(2,idEstado);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se já existe cidade esse nome nesse mesmo estado e se o seu id
   * é diferente do passado como parâmetro.
   * Não deve haver cidades com mesmo nome localizadas no mesmo estado.
   * Pode haver cidades com mesmo nome localizadas em estados diferentes.
   * @param nome
   * @param idEstado
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isCidadeExistente(String nome,int idEstado,int id)throws Exception{
    this.bd.prepareStatement("select id from Cidade_MF where nome = ? and idEstado = ?");
    this.bd.setString(1,nome);
    this.bd.setInt(2,idEstado);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isCidadeExistente;
    if (!resultado.first())
      isCidadeExistente = false;
    else
      if (resultado.getInt(1) == id)
        isCidadeExistente = false;
      else
        isCidadeExistente = true;
    resultado.close();
    return isCidadeExistente;
  }
  
  public int getId(String nome,String uf)throws Exception{
	this.bd.prepareStatement(
	  "select id from Cidade_MF where nome = ? and idEstado in (select id from Estado_MF where uf = ?)"
    );
	this.bd.setString(1,nome);
	this.bd.setString(2,uf);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe Cidade com esse nome e uf.
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}