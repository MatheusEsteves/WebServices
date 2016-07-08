package classesDBO;

import java.io.Serializable;

/**
  * Representa uma relação entre um convênio e um pronto socorro.
  * @author Equipe do projeto Médico Fácil
*/
public class ConvenioProntoSocorro implements Cloneable,Serializable{
    
  private int id;  // Código da relação entre convênio e pronto socorro.
  private int idConvenio; // Código do convênio. 
  private int idProntoSocorro; // Código do pronto socorro.
  
  // Retorna um número relacionado à essa classe para fins externos.
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + this.id;
    hash = 19 * hash + this.idConvenio;
    hash = 19 * hash + this.idProntoSocorro;
    return hash;
  }

  // Compara essa classe com outra, em termos de atributos.
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
    final ConvenioProntoSocorro other = (ConvenioProntoSocorro) obj;
    if (this.id != other.id) {
        return false;
    }
    if (this.idConvenio != other.idConvenio) {
        return false;
    }
    if (this.idProntoSocorro != other.idProntoSocorro) {
        return false;
    }
    return true;
  }
  
  // Construtor de cópia.
  public ConvenioProntoSocorro(ConvenioProntoSocorro convenioProntoSocorro)throws Exception{
    if (convenioProntoSocorro == null)
      throw new Exception("ConvenioProntoSocorro não fornecido para construtor de cópia.");
    this.id              = convenioProntoSocorro.getId();
    this.idConvenio      = convenioProntoSocorro.getIdConvenio();
    this.idProntoSocorro = convenioProntoSocorro.getIdProntoSocorro();
  }
  
  // Retorna uma cópia desse ConvenioProntoSocorro.
  @Override
  public ConvenioProntoSocorro clone(){
    ConvenioProntoSocorro convenioProntoSocorro = null;
    try{
      convenioProntoSocorro = new ConvenioProntoSocorro(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return convenioProntoSocorro;
  }
  
  // Construtor polimórfico.
  public ConvenioProntoSocorro(int id,
                               int idConvenio,
                               int idProntoSocorro)throws Exception{
    this.setId(id);
    this.setIdConvenio(idConvenio);
    this.setIdProntoSocorro(idProntoSocorro);
  }
  
  // Construtor default.
  public ConvenioProntoSocorro(){
    this.id              = 0;
    this.idConvenio      = 0;
    this.idProntoSocorro = 0;
  }
  
  // Seta o código da relação entre convênio e pronto socorro.
  public void setId(int id)throws Exception{
    if (id < 0)
      throw new Exception("Código do ConvenioProntoSocorro inválido.");
    this.id = id;
  }
  
  // Seta o código do convênio.
  public void setIdConvenio(int idConvenio)throws Exception{
    if (idConvenio < 0)
      throw new Exception("Código do convênio inválido.");
    this.idConvenio = idConvenio;
  }
  
  // Seta o código do pronto socorro.
  public void setIdProntoSocorro(int idProntoSocorro)throws Exception{
    if (idProntoSocorro < 0)
      throw new Exception("Código do pronto socorro inválido.");
    this.idProntoSocorro = idProntoSocorro;
  }
  
  // Retorna o código da relação entre convênio e pronto socorro.
  public int getId(){
    return this.id;
  }
  
  // Retorna o código do convênio.
  public int getIdConvenio(){
    return this.idConvenio;
  }
  
  // Retorna o código do pronto socorro.
  public int getIdProntoSocorro(){
    return this.idProntoSocorro;
  }
}
