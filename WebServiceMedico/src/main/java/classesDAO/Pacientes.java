package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDAO.Cidades;
import classesDAO.Estados;
import classesDBO.Cidade;
import classesDBO.Estado;
import classesDBO.Paciente;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados do paciente.
 * @author Equipe do projeto Médico Fácil 
*/
public class Pacientes{
  
  private AdaptedPreparedStatement bd;
  private Cidades cidades;
  private Estados estados;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public Pacientes(AdaptedPreparedStatement bd,Cidades cidades,Estados estados)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
    this.cidades = cidades;
    this.estados = estados;
  }
  
  /**
   * @param paciente
   * @throws Exception 
   */
  public void incluir(Paciente paciente)throws Exception{
    if (paciente == null)
      throw new Exception("Paciente não fornecido para inclusão.");
    
    String cpf   = paciente.getCpf();
    int idCidade = this.cidades.getId(paciente.getCidade(),paciente.getUf());
    
    if (this.isPacienteExistentePorCpf(cpf))
      throw new Exception("Já existe paciente com esse CPF.");
    
    this.bd.prepareStatement("insert into Paciente_MF (idCidade,nome,cpf,endereco,bairro,complemento,telefone,celular,senha,dataNascimento) values (?,?,?,?,?,?,?,?,?,?)");
    this.bd.setInt(1,idCidade);
    this.bd.setString(2,paciente.getNome());
    this.bd.setString(3,cpf);
    this.bd.setString(4,paciente.getEndereco());
    this.bd.setString(5,paciente.getBairro());
    this.bd.setString(6,paciente.getComplemento());
    this.bd.setString(7,paciente.getTelefone());
    this.bd.setString(8,paciente.getCelular());
    this.bd.setString(9,paciente.getSenha());
    this.bd.setDate(10,paciente.getDataNascimento());
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param paciente
   * @throws Exception 
   */
  public void alterar(Paciente paciente)throws Exception{
    if (paciente == null)
      throw new Exception("Paciente não fornecido para alteração.");
    
    int idCidade = this.cidades.getId(paciente.getCidade(),paciente.getUf());
    
    this.bd.prepareStatement(
      "update Paciente_MF set idCidade = ?,nome = ?,endereco = ?,bairro = ?,complemento = ?,telefone = ?,celular = ?,senha = ?,dataNascimento = ? where cpf = ?"
    );
    this.bd.setInt(1,idCidade);
    this.bd.setString(2,paciente.getNome());
    this.bd.setString(3,paciente.getEndereco());
    this.bd.setString(4,paciente.getBairro());
    this.bd.setString(5,paciente.getComplemento());
    this.bd.setString(6,paciente.getTelefone());
    this.bd.setString(7,paciente.getCelular());
    this.bd.setString(8,paciente.getSenha());
    this.bd.setDate(9,paciente.getDataNascimento());
    this.bd.setString(10,paciente.getCpf());
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
      
    this.bd.prepareStatement("delete from Paciente_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return ArrayList<Paciente>
   * @throws Exception 
   */
  private ArrayList<Paciente> getPacientes(ResultSet resultado,ArrayList<Cidade> cidades,ArrayList<Estado> estados)throws Exception{
    ArrayList<Paciente> lista = new ArrayList<Paciente>();
    while (resultado.next()){
      Cidade cidade = this.cidades.getCidades("id = "+resultado.getInt(1)).get(0);
      lista.add(
        new Paciente(
          resultado.getInt(2),
          cidade.getNome(),
          this.estados.getEstados("id = "+cidade.getIdEstado()).get(0).getSigla(),
          resultado.getString(3),
          resultado.getString(4),
          resultado.getString(5),
          resultado.getString(6),
          resultado.getString(7),
          resultado.getString(8),
          resultado.getString(9),
          resultado.getString(10),
          resultado.getDate(11)
        )
      );
    }
    return lista;
  }
  
  /**
   * @param condicao
   * @return ArrayList<Paciente>
   * @throws Exception 
   */
  public ArrayList<Paciente> getPacientes(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    ArrayList<Cidade> cidades = this.cidades.getCidades();
    ArrayList<Estado> estados = this.estados.getEstados();
    this.bd.prepareStatement("select idCidade,id,nome,cpf,endereco,bairro,complemento,telefone,celular,senha,dataNascimento from Paciente_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Paciente> lista = this.getPacientes(resultado,cidades,estados);
    resultado.close();
    return lista;
  }
  
  /**
   * @return ArrayList<Paciente>
   * @throws Exception 
   */
  public ArrayList<Paciente> getPacientes()throws Exception{
	ArrayList<Cidade> cidades = this.cidades.getCidades();
	ArrayList<Estado> estados = this.estados.getEstados();
    this.bd.prepareStatement("select idCidade,id,nome,cpf,endereco,bairro,complemento,telefone,celular,senha,dataNascimento from Paciente_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Paciente> lista = this.getPacientes(resultado,cidades,estados);
    resultado.close();
    return lista;
  }
  
  /**
   * @param id
   * @return boolean
   * @throws Exception 
   */
  public boolean isPacienteExistentePorId(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from Paciente_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * @param cpf
   * @return boolean
   * @throws Exception 
   */
  public boolean isPacienteExistentePorCpf(String cpf)throws Exception{
    this.bd.prepareStatement("select count(id) from Paciente_MF where cpf = ?");
    this.bd.setString(1,cpf);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  public int getId(String cpf)throws Exception{
    this.bd.prepareStatement(
	  "select id from Paciente_MF where cpf = ?"
    );
	this.bd.setString(1,cpf);
    ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe paciente com esse cpf.
    int id = resultado.getInt(1);
    resultado.close();
	return id;
  }
}