package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDAO.Cidades;
import classesDAO.Estados;
import classesDBO.Cidade;
import classesDBO.Clinica;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados da clínica.
 * @author Equipe do projeto Médico Fácil 
*/
public class Clinicas{
  
  private AdaptedPreparedStatement bd;
  private Cidades cidades;
  private Estados estados;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public Clinicas(AdaptedPreparedStatement bd,Cidades cidades,Estados estados)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
    this.cidades = cidades;
    this.estados = estados;
  }
  
  /**
   * @param clinica
   * @throws Exception 
   */
  public void incluir(Clinica clinica)throws Exception{
    if (clinica == null)
      throw new Exception("Clínica não fornecida.");
    
    int idCidade    = this.cidades.getId(clinica.getCidade(),clinica.getUf());
    String bairro   = clinica.getBairro();
    String endereco = clinica.getEndereco();
    String nome     = clinica.getNome();
    String telefone = clinica.getTelefone();
    
    if (this.isClinicaExistente(idCidade,bairro,endereco))
      throw new Exception("Localização já existente para clínica.");
    if (this.isClinicaExistentePorNome(nome))
      throw new Exception("Nome já existente para clínica.");
    if (this.isClinicaExistentePorTelefone(telefone))
      throw new Exception("Telefone já existente para clínica.");
    
    this.bd.prepareStatement("insert into Clinica_MF (idCidade,bairro,endereco,nome,telefone,latitude,longitude) values (?,?,?,?,?,?,?)");
    this.bd.setInt(1,idCidade);
    this.bd.setString(2,bairro);
    this.bd.setString(3,endereco);
    this.bd.setString(4,nome);
    this.bd.setString(5,telefone);
    this.bd.setFloat(6,clinica.getLatitude());
    this.bd.setFloat(7,clinica.getLongitude());
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param clinica
   * @throws Exception 
   */
  public void alterar(Clinica clinica)throws Exception{
    if (clinica == null)
      throw new Exception("Clínica não fornecida.");
    
    int id          = clinica.getId();
    int idCidade    = this.cidades.getId(clinica.getCidade(),clinica.getUf());
    String bairro   = clinica.getBairro();
    String endereco = clinica.getEndereco();
    String nome     = clinica.getNome();
    String telefone = clinica.getTelefone();
    
    if (this.isClinicaExistente(idCidade,bairro,endereco,id))
      throw new Exception("Localização já existente para clínica.");
    if (this.isClinicaExistentePorNome(nome,id))
      throw new Exception("Nome já existente para clínica.");
    if (this.isClinicaExistentePorTelefone(telefone,id))
      throw new Exception("Telefone já existente para clínica.");
    
    this.bd.prepareStatement("update Clinica_MF set idCidade = ?,bairro = ?,endereco = ?,nome = ?,telefone = ?,latitude = ?,longitude = ? where id = ?");
    this.bd.setInt(1,idCidade);
    this.bd.setString(2,bairro);
    this.bd.setString(3,endereco);
    this.bd.setString(4,nome);
    this.bd.setString(5,telefone);
    this.bd.setFloat(6,clinica.getLatitude());
    this.bd.setFloat(7,clinica.getLongitude());
    this.bd.setInt(8,id);
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
      
    this.bd.prepareStatement("delete from Clinica_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return ArrayList<Clinica>
   * @throws Exception 
   */
  private ArrayList<Clinica> getClinicas(ResultSet resultado)throws Exception{
    ArrayList<Clinica> lista = new ArrayList<Clinica>();
    while (resultado.next()){  
      Cidade cidade = this.cidades.getCidades("id = "+resultado.getInt(1)).get(0);
      lista.add(
        new Clinica(
          resultado.getInt(2),
          cidade.getNome(),
          this.estados.getEstados("id = "+cidade.getIdEstado()).get(0).getSigla(),
          resultado.getString(3),
          resultado.getString(4),
          resultado.getString(5),
          resultado.getString(6),
          resultado.getFloat(7),
          resultado.getFloat(8)
        )      
      );
    }
    return lista;
  }
 
  /**
   * @param condicao
   * @return ArrayList<Clinica>
   * @throws Exception 
   */
  public ArrayList<Clinica> getClinicas(String condicao,float latitudePaciente,float longitudePaciente)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement(
      "select idCidade,id,bairro,endereco,nome,telefone,latitude,longitude "+
      "from getClinicasOrdenadas("+latitudePaciente+","+longitudePaciente+") where "+condicao
    );
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Clinica> lista = this.getClinicas(resultado);
    resultado.close();
    return lista;
  }
  
  public ArrayList<Clinica> getClinicas(String condicao)throws Exception{
	if (condicao == null)
	  throw new Exception("Condição não fornecida.");
	    
	this.bd.prepareStatement("select idCidade,id,bairro,endereco,nome,telefone,latitude,longitude from Clinica_MF where "+condicao);
	ResultSet resultado = this.bd.executeQuery();
	ArrayList<Clinica> lista = this.getClinicas(resultado);
	resultado.close();
	return lista;
  }
  
  /**
   * @return ArrayList<Clinica>
   * @throws Exception 
   */
  public ArrayList<Clinica> getClinicas(float latitudePaciente,float longitudePaciente)throws Exception{
    this.bd.prepareStatement(
      "select idCidade,id,bairro,endereco,nome,telefone,latitude,longitude "+
      "from getClinicasOrdenadas("+latitudePaciente+","+longitudePaciente+")"
    );
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Clinica> lista = this.getClinicas(resultado);
    resultado.close();
    return lista;
  }
  
  public ArrayList<Clinica> getClinicas()throws Exception{
	this.bd.prepareStatement("select idCidade,id,bairro,endereco,nome,telefone,latitude,longitude from Clinica_MF");
	ResultSet resultado = this.bd.executeQuery();
	ArrayList<Clinica> lista = this.getClinicas(resultado);
	resultado.close();
	return lista;
  }
  
  /**
   * Verifica se há uma clínica com esse id.
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isClinicaExistentePorId(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from Clinica_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma clínica com esse nome.
   * @param nome
   * @return boolean
   * @throws Exception 
   */
  public boolean isClinicaExistentePorNome(String nome)throws Exception{
    this.bd.prepareStatement("select count(id) from Clinica_MF where nome = ?");
    this.bd.setString(1,nome);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma clínica com esse nome e com id diferente do passado por parâmetro.
   * @param nome
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isClinicaExistentePorNome(String nome,int id)throws Exception{
    this.bd.prepareStatement("select id from Clinica_MF where nome = ?");
    this.bd.setString(1,nome);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isClinicaExistente;
    if (!resultado.first())
      isClinicaExistente = false;
    else
      if (resultado.getInt(1) == id)
        isClinicaExistente = false;
      else
        isClinicaExistente = true;
    resultado.close();
    return isClinicaExistente;
  }
  
  /**
   * Verifica se há uma clínica com essa localização.
   * @param endereco
   * @return boolean
   * @throws Exception 
   */
  public boolean isClinicaExistente(int idCidade,String bairro,String endereco)throws Exception{
    this.bd.prepareStatement("select count(id) from Clinica_MF where idCidade = ? and bairro = ? and endereco = ?");
    this.bd.setInt(1,idCidade);
    this.bd.setString(2,bairro);
    this.bd.setString(3,endereco);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma clínica com essa localização e com id diferente do passado por parâmetro.
   * @param idCidade
   * @param bairro
   * @param endereco
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isClinicaExistente(int idCidade,String bairro,String endereco,int id)throws Exception{
    this.bd.prepareStatement("select id from Clinica_MF where idCidade = ? and bairro = ? and endereco = ?");
    this.bd.setInt(1,idCidade);
    this.bd.setString(2,bairro);
    this.bd.setString(3,endereco);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isClinicaExistente;
    if (!resultado.first())
      isClinicaExistente = false;
    else
      if (resultado.getInt(1) == id)
        isClinicaExistente = false;
      else
        isClinicaExistente = true;
    resultado.close();
    return isClinicaExistente;
  }
  
  /**
   * Verifica se há uma clínica com esse telefone.
   * @param telefone
   * @return boolean
   * @throws Exception 
   */
  public boolean isClinicaExistentePorTelefone(String telefone)throws Exception{
    this.bd.prepareStatement("select count(id) from Clinica_MF where telefone = ?");
    this.bd.setString(1,telefone);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma clínica com esse telefone e com id diferente do passado por parâmetro.
   * @param telefone
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isClinicaExistentePorTelefone(String telefone,int id)throws Exception{
    this.bd.prepareStatement("select id from Clinica_MF where telefone = ?");
    this.bd.setString(1,telefone);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isClinicaExistente;
    if (!resultado.first())
      isClinicaExistente = false;
    else
      if (resultado.getInt(1) == id)
        isClinicaExistente = false;
      else
        isClinicaExistente = true;
    resultado.close();
    return isClinicaExistente;
  }
  
  public int getId(String nome)throws Exception{
	this.bd.prepareStatement(
	  "select id from Clinica_MF where nome = ?"
	);
	this.bd.setString(1,nome);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe clínica com esse nome;
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}