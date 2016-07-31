package classesDBO;

/**
 * Representa uma relação entre administrador e pronto socorro.
 * @author Equipe do projeto Médico Fácil
*/
public class AdmProntoSocorro implements Cloneable{
  
  private int id; // Código da relação entre administrador e pronto socorro.
  private int idAdm; // Código do administrador.
  private int idProntoSocorro; // Código do pronto socorro.
 
  // Retorna um número relacionado à essa classe para fins externos.
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 89 * hash + this.id;
    hash = 89 * hash + this.idAdm;
    hash = 89 * hash + this.idProntoSocorro;
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
    final AdmProntoSocorro other = (AdmProntoSocorro) obj;
    if (this.id != other.id) {
        return false;
    }
    if (this.idAdm != other.idAdm) {
        return false;
    }
    if (this.idProntoSocorro != other.idProntoSocorro) {
        return false;
    }
    return true;
  }
  
  // Construtor de cópia.
  public AdmProntoSocorro(AdmProntoSocorro admProntoSocorro)throws Exception{
    if (admProntoSocorro == null)
      throw new Exception("AdmProntoSocorro não fornecido em construtor de cópia.");
    this.id              = admProntoSocorro.getId();
    this.idAdm           = admProntoSocorro.getIdAdm();
    this.idProntoSocorro = admProntoSocorro.getIdProntoSocorro();
  }
  
  // Retorna uma cópia desse AdmProntoSocorro.
  @Override
  public AdmProntoSocorro clone(){
    AdmProntoSocorro admProntoSocorro = null;
    try{
      admProntoSocorro = new AdmProntoSocorro(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return admProntoSocorro;
  }
  
  // Construtor polimorfico.
  public AdmProntoSocorro(int id,
                          int idAdm,
                          int idProntoSocorro)throws Exception{
    this.setId(id);
    this.setIdAdm(idAdm);
    this.setIdProntoSocorro(idProntoSocorro);
  }
  
  // Construtor default.
  public AdmProntoSocorro(){
    this.id              = 0;
    this.idAdm           = 0;
    this.idProntoSocorro = 0;
  }
  
  // Seta o código da relação entre administrador e pronto socorro.
  public void setId(int id)throws Exception{
    if (id < 0)
      throw new Exception("Id do AdmProntoSocorro inválido.");
    this.id = id;
  }
  
  // Seta o códio do administrador.
  public void setIdAdm(int idAdm)throws Exception{
    if (idAdm < 0)
      throw new Exception("Id do administrador.");
    this.idAdm = idAdm;
  }
  
  // Seta o código do pronto socorro.
  public void setIdProntoSocorro(int idProntoSocorro)throws Exception{
   if (idProntoSocorro < 0)
     throw new Exception("Id do pronto socorro.");
   this.idProntoSocorro = idProntoSocorro;
  }
  
  // Retorna o código da relação entre administrador e pronto socorro.
  public int getId(){
    return this.id;
  }
  
  // Retorna o código do administrador.
  public int getIdAdm(){
    return this.idAdm;
  }
  
  // Retorna o código do pronto socorro.
  public int getIdProntoSocorro(){
    return this.idProntoSocorro;
  }
}
