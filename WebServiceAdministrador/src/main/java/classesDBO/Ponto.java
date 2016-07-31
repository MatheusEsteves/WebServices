package classesDBO;

import java.sql.Timestamp;
import java.util.Objects;
import classesValidacao.Validacao;

/**
  * Representa um determinado ponto que o médico assina por
* meio de leitor biométrico, no qual consta os horários
* de início e de término de trabalho do médico.
* 
* @author Equipe do projeto Médico Fácil
*/
public class Ponto implements Cloneable{
    
  private Timestamp dataHoraInicio; // Início das atividades do médico.
  private Timestamp dataHoraFim; // Fim das atividades do médico.
  private int id; // Id do ponto.
  private int idMedicoProntoSocorro; // Id da relação entre médico e pronto socorro em que atua. 
  
  // Retorna um número associado à essa classe.
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.dataHoraInicio);
    hash = 97 * hash + Objects.hashCode(this.dataHoraFim);
    hash = 97 * hash + this.id;
    hash = 97 * hash + this.idMedicoProntoSocorro;
    return hash;
  }

  // Compara esse ponto com outro, em termos de atributos.
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null) {
        return false;
    }
    if (getClass() != obj.getClass()) {
        return false;
    }
    final Ponto other = (Ponto) obj;
    if (this.id != other.id) {
        return false;
    }
    if (this.idMedicoProntoSocorro != other.idMedicoProntoSocorro) {
        return false;
    }
    if (!Objects.equals(this.dataHoraInicio, other.dataHoraInicio)) {
        return false;
    }
    if (!Objects.equals(this.dataHoraFim, other.dataHoraFim)) {
        return false;
    }
    return true;
  }
  
  // Construtor de cópia.
  public Ponto(Ponto ponto)throws Exception{
    if (ponto == null)
      throw new Exception("Ponto não fornecido para construtor de cópia.");
    this.id = ponto.getId();
    this.idMedicoProntoSocorro = ponto.getIdMedicoProntoSocorro();
    this.dataHoraInicio = ponto.getDataHoraInicio();
    this.dataHoraFim = ponto.getDataHoraFim();
  }
  
  // Retorna uma cópia desse ponto.
  @Override
  public Ponto clone(){
    Ponto ponto = null;
    try{
      ponto = new Ponto(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return ponto;
  }
  
  // Construtor polimórfico.
  public Ponto(int id,
               int idMedicoProntoSocorro,
               Timestamp dataHoraInicio,
               Timestamp dataHoraFim)throws Exception{
    this.setId(id);
    this.setIdMedicoProntoSocorro(idMedicoProntoSocorro);
    this.setDataHoraInicio(dataHoraInicio);
    this.setDataHoraFim(dataHoraFim);
  }
  
  // Construtor default.
  public Ponto(){
    this.id                    = 0;
    this.idMedicoProntoSocorro = 0;
    this.dataHoraInicio        = null;
    this.dataHoraFim           = null;
  }

  // Retorna o período de ínicio das atividades do médico.
  public Timestamp getDataHoraInicio() {
    return dataHoraInicio;
  }

  // Seta o período de ínicio das atividades do médico.
  public void setDataHoraInicio(Timestamp dataHoraInicio) throws Exception{
    if (this.dataHoraFim != null)
      if (!Validacao.isIntervaloDatasValido(dataHoraInicio,this.dataHoraFim))
        throw new Exception("Período de início inválido.");
    if (dataHoraInicio == null)
      throw new Exception("Período de início inválido.");
    this.dataHoraInicio = dataHoraInicio;
  }

  // Retorna o período de fim das atividades do médico.
  public Timestamp getDataHoraFim() {
    return dataHoraFim;
  }

  // Seta o período de fim das atividades do médico.
  public void setDataHoraFim(Timestamp dataHoraFim) throws Exception{
    if (this.dataHoraInicio != null)
      if (!Validacao.isIntervaloDatasValido(this.dataHoraInicio,dataHoraFim))
        throw new Exception("Período de término inválido.");
    if (dataHoraFim == null)
      throw new Exception("Período de término inválido.");
    this.dataHoraFim = dataHoraFim;
  }

  // Retorna o id do ponto.
  public int getId() {
    return id;
  }

  // Seta o id do ponto.
  public void setId(int id)throws Exception{
    if (id <= 0)
      throw new Exception("Id do ponto inválido.");
    this.id = id;
  }

  // Retorna o id da relação entre médico e pronto socorro.
  public int getIdMedicoProntoSocorro() {
    return idMedicoProntoSocorro;
  }

  // Seta o id da relação entre médico e pronto socorro.
  public void setIdMedicoProntoSocorro(int idMedicoProntoSocorro) throws Exception{
    if (idMedicoProntoSocorro <= 0)
      throw new Exception("Id da relação entre médico e pronto socorro não fornecido.");
    this.idMedicoProntoSocorro = idMedicoProntoSocorro;
  }
}
