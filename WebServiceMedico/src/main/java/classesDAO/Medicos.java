package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDAO.Estados;
import classesDBO.Medico;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados do médico.
 * @author Equipe do projeto Médico Fácil 
*/
public class Medicos{
 
  private AdaptedPreparedStatement bd;
  private Estados estados;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public Medicos(AdaptedPreparedStatement bd,Estados estados)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
    this.estados = estados;
  }
  
  /**
   * @param medico
   * @throws Exception 
   */
  public void incluir(Medico medico)throws Exception{
    if (medico == null)
      throw new Exception("Médico não fornecido para inclusão.");
    
    String crm          = medico.getCrm();
    int idEstado        = this.estados.getId(medico.getUf());
    
    if (this.isMedicoExistente(crm,idEstado))
      throw new Exception("Já existe médico com esse CRM nesse estado.");
    
    this.bd.prepareStatement("insert into Medico_MF (idEstado,crm,nome) values (?,?,?)");
    this.bd.setInt(2,idEstado);
    this.bd.setString(3,crm);
    this.bd.setString(4,medico.getNome());
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param medico
   * @throws Exception 
   */
  public void alterar(Medico medico)throws Exception{
    if (medico == null)
      throw new Exception("Medico não fornecido para alteração.");
    
    String crm          = medico.getCrm();
    int idEstado        = this.estados.getId(medico.getUf());
    
    if (this.isMedicoExistente(crm,idEstado,crm))
      throw new Exception("Já existe médico com esse CRM nesse estado.");
    
    this.bd.prepareStatement("update Medico_MF set idEstado = ?,nome = ? where crm = ?");
    this.bd.setInt(2,idEstado);
    this.bd.setString(3,medico.getNome());
    this.bd.setString(4,crm);
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
      
    this.bd.prepareStatement("delete from Medico_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return ArrayList<Medico>
   * @throws Exception 
   */
  private ArrayList<Medico> getMedicos(ResultSet resultado)throws Exception{
    ArrayList<Medico> lista = new ArrayList<Medico>();
    while (resultado.next()){
      lista.add(
        new Medico(
          resultado.getInt(1),
          this.estados.getEstados("id = "+resultado.getInt(2)).get(0).getSigla(),
          resultado.getString(3),
          resultado.getString(4)
        )
      );
    }
    return lista;
  }
  
  /**
   * @param condicao
   * @return ArrayList<Medico>
   * @throws Exception 
   */
  public ArrayList<Medico> getMedicos(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select id,idEstado,crm,nome from Medico_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Medico> lista = this.getMedicos(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return ArrayList<Medico>
   * @throws Exception 
   */
  public ArrayList<Medico> getMedicos()throws Exception{
    this.bd.prepareStatement("select id,idEstado,crm,nome from Medico_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Medico> lista = this.getMedicos(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isMedicoExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from Medico_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se já existe um médico com esse CRM no mesmo estado.
   * @param crm
   * @return boolean
   * @throws Exception 
   */
  public boolean isMedicoExistente(String crm,int idEstado)throws Exception{
    this.bd.prepareStatement("select count(id) from Medico_MF where crm = ? and idEstado = ?");
    this.bd.setString(1,crm);
    this.bd.setInt(2,idEstado);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se já existe um médico com esse CRM no mesmo estado e
   * se a chave desse médico é diferente da chave passada como parâmetro.
   * @param crm
   * @param idEstado
   * @param chave
   * @return boolean
   * @throws Exception 
   */
  public boolean isMedicoExistente(String crm,int idEstado,String chave)throws Exception{
    this.bd.prepareStatement("select crm from Medico_MF where crm = ? and idEstado = ?");
    this.bd.setString(1,crm);
    this.bd.setInt(2,idEstado);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isMedicoExistente;
    if (!resultado.first())
      isMedicoExistente = false;
    else
      if (resultado.getString(1).equals(chave))
        isMedicoExistente = false;
      else
        isMedicoExistente = true;
    resultado.close();
    return isMedicoExistente;
  }
  
  public int getId(String crm)throws Exception{
    this.bd.prepareStatement(
	  "select id from Medico_MF where crm = ?"
    );
	this.bd.setString(1,crm);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe médico com esse crm.
	int id = resultado.getInt(1);
	resultado.close();
    return id;
  }
}