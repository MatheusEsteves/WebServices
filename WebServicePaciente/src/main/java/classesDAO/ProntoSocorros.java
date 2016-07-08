package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDAO.Cidades;
import classesDAO.Estados;
import classesDBO.Cidade;
import classesDBO.Clinica;
import classesDBO.ProntoSocorro;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;

/**
 * Relizará operações no banco de dados com os dados do pronto socorro.
 * @author Equipe do projeto Médico Fácil 
*/
public class ProntoSocorros{
 
  private AdaptedPreparedStatement bd;
  private Cidades cidades;
  private Estados estados;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public ProntoSocorros(AdaptedPreparedStatement bd,Cidades cidades,Estados estados)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
    this.cidades = cidades;
    this.estados = estados;
  }
  
  /**
   * @param prontoSocorro
   * @throws Exception 
   */
  public void incluir(ProntoSocorro prontoSocorro)throws Exception{
    if (prontoSocorro == null)
      throw new Exception("Pronto socorro não fornecido para inclusão.");
    
    int idCidade    = this.cidades.getId(prontoSocorro.getCidade(),prontoSocorro.getUf());
    String bairro   = prontoSocorro.getBairro();
    String nome     = prontoSocorro.getNome();
    String endereco = prontoSocorro.getEndereco();
    String telefone = prontoSocorro.getTelefone();
    
    if (this.isProntoSocorroExistentePorNome(nome))
      throw new Exception("Já existe pronto socorro com esse nome.");
    if (this.isProntoSocorroExistente(idCidade,bairro,endereco))
      throw new Exception("Já existe pronto socorro com essa localização.");
    if (this.isProntoSocorroExistentePorTelefone(telefone))
      throw new Exception("Já existe pronto socorro com esse telefone.");
    
    this.bd.prepareStatement("insert into ProntoSocorro_MF(nome,endereco,bairro,idCidade,telefone,latitude,longitude) values(?,?,?,?,?,?,?)");
    this.bd.setString(1,nome);
    this.bd.setString(2,endereco);
    this.bd.setString(3,bairro);
    this.bd.setInt(4,idCidade);
    this.bd.setString(5,telefone);
    this.bd.setFloat(6,prontoSocorro.getLatitude());
    this.bd.setFloat(7,prontoSocorro.getLongitude());
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param prontoSocorro
   * @throws Exception 
   */
  public void alterar(ProntoSocorro prontoSocorro)throws Exception{
    if (prontoSocorro == null)
      throw new Exception("Pronto socorro não fornecido para alteração.");
    
    int id          = prontoSocorro.getId();
    int idCidade    = this.cidades.getId(prontoSocorro.getNome(),prontoSocorro.getUf());
    String bairro   = prontoSocorro.getBairro();
    String nome     = prontoSocorro.getNome();
    String endereco = prontoSocorro.getEndereco();
    String telefone = prontoSocorro.getTelefone();
    
    if (this.isProntoSocorroExistentePorNome(nome,id))
      throw new Exception("Já existe pronto socorro com esse nome.");
    if (this.isProntoSocorroExistente(idCidade,bairro,endereco,id))
      throw new Exception("Já existe pronto socorro com essa localização.");
    if (this.isProntoSocorroExistentePorTelefone(telefone,id))
      throw new Exception("Já existe pronto socorro com esse telefone.");
    
    this.bd.prepareStatement(
     "update ProntoSocorro_MF set nome = ?,endereco = ?,bairro = ?,idCidade = ?,telefone = ?,latitude = ?,longitude = ? where id = ?"
    );
    this.bd.setString(1,nome);
    this.bd.setString(2,endereco);
    this.bd.setString(3,bairro);
    this.bd.setInt(4,idCidade);
    this.bd.setString(5,telefone);
    this.bd.setFloat(6,prontoSocorro.getLatitude());
    this.bd.setFloat(7,prontoSocorro.getLongitude());
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
    
    this.bd.prepareStatement("delete from ProntoSocorro_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return ArrayList<ProntoSocorro>
   * @throws Exception 
   */
  private ArrayList<ProntoSocorro> getProntoSocorros(ResultSet resultado)throws Exception{
    ArrayList<ProntoSocorro> lista = new ArrayList<ProntoSocorro>();
    while (resultado.next()){
      Cidade cidade = this.cidades.getCidades("id = "+resultado.getInt(1)).get(0);
      lista.add(
        new ProntoSocorro(
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
   * @return ArrayList<ProntoSocorro>
   * @throws Exception 
   */
  public ArrayList<ProntoSocorro> getProntoSocorros(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select idCidade,id,bairro,endereco,nome,telefone,latitude,longitude from ProntoSocorro_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<ProntoSocorro> lista = this.getProntoSocorros(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @param condicao
   * @return ArrayList<ProntoSocorro>
   * @throws Exception 
   */
  public ArrayList<ProntoSocorro> getProntoSocorros(String condicao,float latitudePaciente,float longitudePaciente)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement(
      "select idCidade,id,bairro,endereco,nome,telefone,latitude,longitude "+
      "from getProntoSocorrosOrdenados("+latitudePaciente+","+longitudePaciente+") where "+condicao
    );
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<ProntoSocorro> lista = this.getProntoSocorros(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return ArrayList<ProntoSocorro>
   * @throws Exception 
   */
  public ArrayList<ProntoSocorro> getProntoSocorros()throws Exception{
    this.bd.prepareStatement("select idCidade,id,bairro,endereco,nome,telefone,latitude,longitude from ProntoSocorro_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<ProntoSocorro> lista = this.getProntoSocorros(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return ArrayList<ProntoSocorro>
   * @throws Exception 
   */
  public ArrayList<ProntoSocorro> getProntoSocorros(float latitudePaciente,float longitudePaciente)throws Exception{
    this.bd.prepareStatement(
      "select idCidade,id,bairro,endereco,nome,telefone,latitude,longitude "+
      "from getProntoSocorrosOrdenados("+latitudePaciente+","+longitudePaciente+")"
    );
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<ProntoSocorro> lista = this.getProntoSocorros(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há um pronto socorro com o id passado como parâmetro.
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isProntoSocorroExistentePorId(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from ProntoSocorro_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há um pronto socorro com o mesmo nome passado como parâmetro.
   * @param nome
   * @return boolean
   * @throws Exception 
   */
  public boolean isProntoSocorroExistentePorNome(String nome)throws Exception{
    this.bd.prepareStatement("select count(id) from ProntoSocorro_MF where nome = ?");
    this.bd.setString(1,nome);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há um pronto socorro com id diferente do passado como parâmetro
   * e com o mesmo nome passado como parâmetro.
   * @param nome
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isProntoSocorroExistentePorNome(String nome,int id)throws Exception{
    this.bd.prepareStatement("select id from ProntoSocorro_MF where nome = ?");
    this.bd.setString(1,nome);
    ResultSet resultado = this.bd.executeQuery();
    boolean isProntoSocorroExistente;
    if (!resultado.first())
      isProntoSocorroExistente = false;
    else
      if (resultado.getInt(1) == id)
        isProntoSocorroExistente = false;
      else
        isProntoSocorroExistente = true;
    resultado.close();
    return isProntoSocorroExistente;
  }
  
  /**
   * Verifica se há um pronto socorro existente com a localização passada 
   * como parâmetro.
   * @param endereco
   * @return boolean
   * @throws Exception 
   */
  public boolean isProntoSocorroExistente(int idCidade,String bairro,String endereco)throws Exception{
    this.bd.prepareStatement("select count(id) from ProntoSocorro_MF where idCidade = ? and bairro = ? and endereco = ?");
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
   * Verifica se há um pronto socorro com id diferente do passado como parâmetro
   * e localização igual a passada como parâmetro.
   * @param idCidade
   * @param bairro
   * @param endereco
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isProntoSocorroExistente(int idCidade,String bairro,String endereco,int id)throws Exception{
    this.bd.prepareStatement("select id from ProntoSocorro_MF where idCidade = ? and bairro = ? and endereco = ?");
    this.bd.setInt(1,idCidade);
    this.bd.setString(2,bairro);
    this.bd.setString(3,endereco);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isProntoSocorroExistente;
    if (!resultado.first())
      isProntoSocorroExistente = false;
    else
      if (resultado.getInt(1) == id)
        isProntoSocorroExistente = false;
      else
        isProntoSocorroExistente = true;
    resultado.close();
    return isProntoSocorroExistente;
  }
  
  /**
   * Verifica se há um pronto socorro com o telefone passado como parâmetro.
   * @param telefone
   * @return boolean
   * @throws Exception 
   */
  public boolean isProntoSocorroExistentePorTelefone(String telefone)throws Exception{
    this.bd.prepareStatement("select count(id) from ProntoSocorro_MF where telefone = ?");
    this.bd.setString(1,telefone);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há um pronto socorro com id diferente do passado como parâmetro e
   * telefone igual ao passado como parâmetro.
   * @param telefone
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isProntoSocorroExistentePorTelefone(String telefone,int id)throws Exception{
    this.bd.prepareStatement("select id from ProntoSocorro_MF where telefone = ?");
    this.bd.setString(1,telefone);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isProntoSocorroExistente;
    if (!resultado.first())
      isProntoSocorroExistente = false;
    else
      if (resultado.getInt(1) == id)
        isProntoSocorroExistente = false;
      else
        isProntoSocorroExistente = true;
    resultado.close();
    return isProntoSocorroExistente;
  }
  
  public int getId(String nome)throws Exception{
	this.bd.prepareStatement(
	  "select id from ProntoSocorro_MF where nome = ?"
	);
    this.bd.setString(1,nome);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe pronto socorro com esse nome.
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}