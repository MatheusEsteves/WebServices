package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.Especialidade;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados da especialidade médica.
 * @author Equipe do projeto Médico Fácil 
*/
public class Especialidades{
  
  private AdaptedPreparedStatement bd;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public Especialidades(AdaptedPreparedStatement bd)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
  }
  
  /**
   * @param especialidade
   * @throws Exception 
   */
  public void incluir(Especialidade especialidade)throws Exception{
    if (especialidade == null)
      throw new Exception("Especialidade não fornecido.");
    
    String nome = especialidade.getNome();
    if (this.isEspecialidadeExistente(nome))
      throw new Exception("Já existe especialidade com esse nome.");
    
    this.bd.prepareStatement("insert into Especialidade_MF (nome) values (?)");
    this.bd.setString(1,nome);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param especialidade
   * @throws Exception 
   */
  public void alterar(Especialidade especialidade)throws Exception{
    if (especialidade == null)
      throw new Exception("Especialidade não fornecido.");
    
    int id = especialidade.getId();
    String nome = especialidade.getNome();
    if (this.isEspecialidadeExistente(nome,id))
      throw new Exception("Já existe especialidade com esse nome.");
    
    this.bd.prepareStatement("update Especialidade_MF set nome = ? where id = ?");
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
      
    this.bd.prepareStatement("delete from Especialidade_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return ArrayList<Especialidade>
   * @throws Exception 
   */
  private ArrayList<Especialidade> getEspecialidades(ResultSet resultado)throws Exception{
    ArrayList<Especialidade> lista = new ArrayList<Especialidade>();
    while (resultado.next())
      lista.add(
        new Especialidade(
          resultado.getInt(1),
          resultado.getString(2)
        )      
      );
    return lista;
  }
  
  /**
   * @param condicao
   * @return ArrayList<Especialidade>
   * @throws Exception 
   */
  public ArrayList<Especialidade> getEspecialidades(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select * from Especialidade_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Especialidade> lista = this.getEspecialidades(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return ArrayList<Especialidade>
   * @throws Exception 
   */
  public ArrayList<Especialidade> getEspecialidades()throws Exception{
    this.bd.prepareStatement("select * from Especialidade_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Especialidade> lista = this.getEspecialidades(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há especialidade com esse id.
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isEspecialidadeExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from Especialidade_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há especialidade com esse nome.
   * @param nome
   * @return boolean
   * @throws Exception 
   */
  public boolean isEspecialidadeExistente(String nome)throws Exception{
    this.bd.prepareStatement("select count(id) from Especialidade_MF where nome = ?");
    this.bd.setString(1,nome);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há especialidade com esse nome e com id diferente do passado 
   * como parâmetro.
   * @param nome
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isEspecialidadeExistente(String nome,int id)throws Exception{
    this.bd.prepareStatement("select id from Especialidade_MF where nome = ?");
    this.bd.setString(1,nome);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isEspecialidadeExistente;
    if (!resultado.first())
      isEspecialidadeExistente = false;
    else
      if (resultado.getInt(1) == id)
        isEspecialidadeExistente = false;
      else
        isEspecialidadeExistente = true;
    resultado.close();
    return isEspecialidadeExistente;
  }
  
  public int getId(String nome)throws Exception{
	this.bd.prepareStatement(
	  "select id from Especialidade_MF where nome = ?"
    );
	this.bd.setString(1,nome);
    ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
      return 0; // Não existe especialidade com esse nome.
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}