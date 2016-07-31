package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.Adm;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados do administrador.
 * @author Equipe do projeto Médico Fácil 
*/
public class Adms{

  private AdaptedPreparedStatement bd;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public Adms(AdaptedPreparedStatement bd)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
  }
      
  /**
   * @param adm
   * @throws Exception 
   */
  public void incluir(Adm adm)throws Exception{
    if (adm == null)
      throw new Exception("Administrador não fornecido para inclusão.");
    
    String usuario = adm.getUsuario();
    if (this.isAdmExistente(usuario))
      throw new Exception("Username já existente para administrador.");
    
    this.bd.prepareStatement("insert into Adm_MF (usuario,senha) values (?,?)");
    this.bd.setString(1,usuario);
    this.bd.setString(2,adm.getSenha());
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param adm
   * @throws Exception 
   */
  public void alterar(Adm adm)throws Exception{
    if (adm == null)
      throw new Exception("Administrador não fornecido para alteração.");
   
    this.bd.prepareStatement("update Adm_MF set senha = ? where usuario = ?");
    this.bd.setString(1,adm.getSenha());
    this.bd.setString(2,adm.getUsuario());
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
      
    this.bd.prepareStatement("delete from Adm_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return
   * @throws Exception 
   */
  private ArrayList<Adm> getAdms(ResultSet resultado)throws Exception{
    ArrayList<Adm> lista = new ArrayList<Adm>();
    while (resultado.next())
      lista.add(
        new Adm(
          resultado.getInt(1),
          resultado.getString(2),
          resultado.getString(3)
        )      
      );
    return lista;
  }
  
  /**
   * @param condicao
   * @return
   * @throws Exception 
   */
  public ArrayList<Adm> getAdms(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select id,usuario,senha from Adm_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Adm> lista = this.getAdms(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return
   * @throws Exception 
   */
  public ArrayList<Adm> getAdms()throws Exception{
    this.bd.prepareStatement("select id,usuario,senha from Adm_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Adm> lista = this.getAdms(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há administrador com esse id.
   * @param id
   * @return
   * @throws Exception 
   */
  public boolean isAdmExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from Adm_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há administrador com esse usuário.
   * @param usuario
   * @return
   * @throws Exception 
   */
  public boolean isAdmExistente(String usuario)throws Exception{
    this.bd.prepareStatement("select count(id) from Adm_MF where usuario = ?");
    this.bd.setString(1,usuario);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  public int getId(String usuario,String senha)throws Exception{
	this.bd.prepareStatement(
	  "select id from Adm_MF where usuario = ? and senha = ?"
	);
	this.bd.setString(1,usuario);
	this.bd.setString(2,senha);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe administrador com esse usuário e senha.
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}