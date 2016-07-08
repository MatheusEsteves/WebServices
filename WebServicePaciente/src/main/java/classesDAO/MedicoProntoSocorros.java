package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.Medico;
import classesDBO.MedicoClinica;
import classesDBO.MedicoProntoSocorro;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados da relação entre o médico e clínica.
 * @author Equipe do projeto Médico Fácil 
*/
public class MedicoProntoSocorros{
  
  private AdaptedPreparedStatement bd;
  private Medicos medicos;
  private ProntoSocorros prontoSocorros;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public MedicoProntoSocorros(AdaptedPreparedStatement bd,Medicos medicos,ProntoSocorros prontoSocorros)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd             = bd;
    this.medicos        = medicos;
    this.prontoSocorros = prontoSocorros;
  }
  
  /**
   * @param medicoProntoSocorro
   * @throws Exception 
   */
  public void incluir(MedicoProntoSocorro medicoProntoSocorro)throws Exception{
    if (medicoProntoSocorro == null)
      throw new Exception("Relação entre médico e pronto socorro não fornecida.");
    
    int idMedico        = this.medicos.getId(medicoProntoSocorro.getMedico().getCrm());
    int idProntoSocorro = this.prontoSocorros.getId(medicoProntoSocorro.getProntoSocorro().getNome());
    
    if (this.isMedicoProntoSocorroExistente(idMedico,idProntoSocorro))
      throw new Exception("Esse médico já atua nesse pronto socorro.");
    
    this.bd.prepareStatement("insert into MedicoPs_MF (idMedico,idPs,presente,biometria) values (?,?,?,?)");
    this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idProntoSocorro);
    this.bd.setString(3,medicoProntoSocorro.isPresente()?"S":"N");
    this.bd.setBytes(4,medicoProntoSocorro.getBiometria());
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param medicoProntoSocorro
   * @throws Exception 
   */
  public void alterar(MedicoProntoSocorro medicoProntoSocorro)throws Exception{
    if (medicoProntoSocorro == null)
      throw new Exception("Relação entre médico e pronto socorro não fornecida.");
    
    int id              = medicoProntoSocorro.getId();
    int idMedico        = this.medicos.getId(medicoProntoSocorro.getMedico().getCrm());
    int idProntoSocorro = this.prontoSocorros.getId(medicoProntoSocorro.getProntoSocorro().getNome());
    
    if (this.isMedicoProntoSocorroExistente(idMedico,idProntoSocorro,id))
      throw new Exception("Esse médico já atua nesse pronto socorro.");
    
    this.bd.prepareStatement("update MedicoPs_MF set idMedico = ?,idPs = ?,presente = ?,biometria = ? where id = ?");
    this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idProntoSocorro);
    this.bd.setString(3,medicoProntoSocorro.isPresente()?"S":"N");
    this.bd.setBytes(4,medicoProntoSocorro.getBiometria());
    this.bd.setInt(5,id);
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
    
    this.bd.prepareStatement("delete from MedicoPs_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return
   * @throws Exception 
   */
  private ArrayList<MedicoProntoSocorro> getMedicoProntoSocorros(ResultSet resultado)throws Exception{
    ArrayList<MedicoProntoSocorro> lista = new ArrayList<MedicoProntoSocorro>();
    while (resultado.next())
      lista.add(
        new MedicoProntoSocorro(
          resultado.getInt(1),
          this.medicos.getMedicos("id = "+resultado.getInt(2)).get(0),
          this.prontoSocorros.getProntoSocorros("id = "+resultado.getInt(3)).get(0),
          resultado.getString(4).equals("S"),
          resultado.getBytes(5)
        )      
      );
    return lista;
  }
  
  /**
   * @param condicao
   * @return
   * @throws Exception 
   */
  public ArrayList<MedicoProntoSocorro> getMedicoProntoSocorros(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select id,idMedico,idPs,presente,biometria from MedicoPs_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<MedicoProntoSocorro> lista = this.getMedicoProntoSocorros(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return
   * @throws Exception 
   */
  public ArrayList<MedicoProntoSocorro> getMedicoProntoSocorros()throws Exception{
    this.bd.prepareStatement("select id,idMedico,idPs,presente,biometria from MedicoPs_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<MedicoProntoSocorro> lista = this.getMedicoProntoSocorros(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há uma relação entre médico e pronto socorro com o id passado como
   * parâmetro.
   * @param id
   * @return
   * @throws Exception 
   */
  public boolean isMedicoProntoSocorroExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from MedicoPs_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma relação entre médico e pronto socorro pelos dados passados como
   * parâmetro.
   * @param idMedico
   * @param idProntoSocorro
   * @return
   * @throws Exception 
   */
  public boolean isMedicoProntoSocorroExistente(int idMedico,int idProntoSocorro)throws Exception{
    this.bd.prepareStatement("select count(id) from MedicoPs_MF where idMedico = ? and idPs = ?");
    this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idProntoSocorro);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se há uma relação entre médico e pronto socorro pelos dados passados como parâmetro
   * e se o id dela é diferente do passado pelo parâmetro.
   * @param idMedico
   * @param idProntoSocorro
   * @param id
   * @return
   * @throws Exception 
   */
  public boolean isMedicoProntoSocorroExistente(int idMedico,int idProntoSocorro,int id)throws Exception{
    this.bd.prepareStatement("select id from MedicoPs_MF where idMedico = ? and idPs = ?");
    this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idProntoSocorro);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isMedicoProntoSocorroExistente;
    if (!resultado.first())
      isMedicoProntoSocorroExistente = false;
    else
      if (resultado.getInt(1) == id)
        isMedicoProntoSocorroExistente = false;
      else
        isMedicoProntoSocorroExistente = true;
    resultado.close();
    return isMedicoProntoSocorroExistente;
  }
  
  public int getId(int idMedico,int idPs)throws Exception{
	this.bd.prepareStatement(
	  "select id from MedicoPs_MF where idMedico = ? and idPs = ?"
	);
	this.bd.setInt(1,idMedico);
    this.bd.setInt(2,idPs);
	ResultSet resultado = this.bd.executeQuery();
	if (!resultado.first())
	  return 0; // Não existe relação entre médico e pronto socorro com esses dados.
	int id = resultado.getInt(1);
	resultado.close();
	return id;
  }
}