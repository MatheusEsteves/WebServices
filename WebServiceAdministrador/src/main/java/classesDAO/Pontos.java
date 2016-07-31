package classesDAO;

import classesBD.AdaptedPreparedStatement;
import classesDBO.Ponto;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Relizará operações no banco de dados com os dados do ponto.
 * @author Equipe do projeto Médico Fácil 
*/
public class Pontos{
  
  private AdaptedPreparedStatement bd;
  
  /**
   * @param bd
   * @throws Exception 
   */
  public Pontos(AdaptedPreparedStatement bd)throws Exception{
    if (bd == null)
      throw new Exception("BD não fornecido.");
    this.bd = bd;
  }
  
  /**
   * @param ponto
   * @throws Exception 
   */
  public void incluir(Ponto ponto)throws Exception{
    if (ponto == null)
      throw new Exception("Ponto não fornecido para inclusão.");
    
    int idMedicoProntoSocorro = ponto.getIdMedicoProntoSocorro();
    Timestamp dataHoraInicio   = ponto.getDataHoraInicio();
    Timestamp dataHoraFim      = ponto.getDataHoraFim();
    
    if (this.isPontoExistente(idMedicoProntoSocorro, dataHoraInicio, dataHoraFim)) 
      throw new Exception("Esse médico já estará em atendimento em alguma parte do intervalo especificado.");
    
    this.bd.prepareStatement("insert into Ponto_MF (idMedicoPs,dataHoraEntrada,dataHoraSaida) values (?,?,?)");
    this.bd.setInt(1,idMedicoProntoSocorro);
    this.bd.setTimestamp(2,dataHoraInicio);
    this.bd.setTimestamp(3,dataHoraFim);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param ponto
   * @throws Exception 
   */
  public void alterar(Ponto ponto)throws Exception{
    if (ponto == null)
      throw new Exception("Ponto não fornecido para alteração.");
    
    int id                    = ponto.getId();
    int idMedicoProntoSocorro = ponto.getIdMedicoProntoSocorro();
    Timestamp dataHoraInicio  = ponto.getDataHoraInicio();
    Timestamp dataHoraFim     = ponto.getDataHoraFim();
    
    if (this.isPontoExistente(idMedicoProntoSocorro, dataHoraInicio, dataHoraFim,id)) 
      throw new Exception("Esse médico já estará em atendimento em alguma parte do intervalo especificado.");
    
    this.bd.prepareStatement(
      "update Ponto_MF set dataHoraEntrada = ?,dataHoraSaida = ? where id = ?"
    );
    this.bd.setTimestamp(1,dataHoraInicio);
    this.bd.setTimestamp(2,dataHoraFim);
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
      
    this.bd.prepareStatement("delete from Ponto_MF where id = ?");
    this.bd.setInt(1,id);
    this.bd.executeUpdate();
    this.bd.commit();
  }
  
  /**
   * @param resultado
   * @return
   * @throws Exception 
   */
  private ArrayList<Ponto> getPontos(ResultSet resultado)throws Exception{  
    ArrayList<Ponto> lista = new ArrayList<Ponto>();
    while (resultado.next())
      lista.add(
        new Ponto(
          resultado.getInt(1),
          resultado.getInt(2),
          resultado.getTimestamp(3),
          resultado.getTimestamp(4)
        )
      );
    return lista;
  }
  
  /**
   * @param condicao
   * @return
   * @throws Exception 
   */
  public ArrayList<Ponto> getPontos(String condicao)throws Exception{
    if (condicao == null)
      throw new Exception("Condição não fornecida.");
    
    this.bd.prepareStatement("select id,idMedicoPs,dataHoraEntrada,dataHoraSaida from Ponto_MF where "+condicao);
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Ponto> lista = this.getPontos(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * @return
   * @throws Exception 
   */
  public ArrayList<Ponto> getPontos()throws Exception{
    this.bd.prepareStatement("select id,idMedicoPs,dataHoraEntrada,dataHoraSaida from Ponto_MF");
    ResultSet resultado = this.bd.executeQuery();
    ArrayList<Ponto> lista = this.getPontos(resultado);
    resultado.close();
    return lista;
  }
  
  /**
   * Verifica se há um ponto com esse id.
   * @param id
   * @return
   * @throws Exception 
   */
  public boolean isPontoExistente(int id)throws Exception{
    this.bd.prepareStatement("select count(id) from Ponto_MF where id = ?");
    this.bd.setInt(1,id);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se já existe ponto pelos dados informados como parâmetro.
   * Caso o médico com o id passado como parâmetro já esteja em trabalho em alguma
   * parte do período especificado (dataHoraInicio até dataHoraFim), não poderá 
   * cadatrar ponto com esse período.
   * 
   * @param idMedicoProntoSocorro
   * @param dataHoraInicio
   * @param dataHoraFim
   * @return
   * @throws Exception 
   */
  public boolean isPontoExistente(int idMedicoProntoSocorro,Timestamp dataHoraInicio,Timestamp dataHoraFim)throws Exception{
    this.bd.prepareStatement(
       "select count(id) from Ponto_MF where "+
       "idMedicoPs = ? and "+
       "not"+
       "("+
         "? >= dataHoraFim or "+
         "? <= dataHoraInicio"+ 
       ")"
    );
    this.bd.setInt(1,idMedicoProntoSocorro);
    this.bd.setTimestamp(2,dataHoraInicio);
    this.bd.setTimestamp(3,dataHoraFim);
    ResultSet resultado = this.bd.executeQuery();
    resultado.first();
    int qtos = resultado.getInt(1);
    resultado.close();
    return qtos > 0;
  }
  
  /**
   * Verifica se já existe ponto pelos dados informados como parâmetro e se o seu
   * id é diferente do informado pelo parâmetro.
   * Caso o médico com o id passado como parâmetro já esteja em trabalho em alguma
   * parte do período especificado (dataHoraInicio até dataHoraFim), não poderá 
   * cadatrar ponto com esse período.
   * 
   * @param idMedicoProntoSocorro
   * @param dataHoraInicio
   * @param dataHoraFim
   * @return
   * @throws Exception 
   */
  public boolean isPontoExistente(int idMedicoProntoSocorro,Timestamp dataHoraInicio,Timestamp dataHoraFim,int id)throws Exception{
    this.bd.prepareStatement(
       "select id from Ponto_MF where "+
       "idMedicoPs = ? and "+
       "not"+
       "("+
         "? >= dataHoraFim or "+
         "? <= dataHoraInicio"+ 
       ")"
    );
    this.bd.setInt(1,idMedicoProntoSocorro);
    this.bd.setTimestamp(2,dataHoraInicio);
    this.bd.setTimestamp(3,dataHoraFim);
    
    ResultSet resultado = this.bd.executeQuery();
    boolean isPontoExistente;
    if (!resultado.first())
      isPontoExistente = false;
    else
      if (resultado.getInt(1) == id)
        isPontoExistente = false;
      else
        isPontoExistente = true;
    resultado.close();
    return isPontoExistente;
  }
}