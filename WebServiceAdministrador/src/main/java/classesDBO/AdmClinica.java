package classesDBO;

/**
  * Representa uma relação entre administrador e clínica.
  * @author Equipe do projeto Médico Fácil
*/
public class AdmClinica implements Cloneable{
    
  private int id; // Id da relação entre administrador a clínica.
  private int idAdm; // Id do aministrador.
  private int idClinica; // Id da clínica.
  
  // Retorna um número associado à essa classe.
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + this.id;
    hash = 29 * hash + this.idAdm;
    hash = 29 * hash + this.idClinica;
    return hash;
  }

  // Compara essa classe à outra, em termos de atributos.
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
    final AdmClinica other = (AdmClinica) obj;
    if (this.id != other.id) {
        return false;
    }
    if (this.idAdm != other.idAdm) {
        return false;
    }
    if (this.idClinica != other.idClinica) {
        return false;
    }
    return true;
  }
  
  // Construtor de cópia.
  public AdmClinica(AdmClinica admClinica)throws Exception{
    if (admClinica == null)
      throw new Exception("AdmClinica não fornecido para construtor de cópia.");
    this.id        = admClinica.getId();
    this.idAdm     = admClinica.getIdAdm();
    this.idClinica = admClinica.getIdClinica();
  }
  
  // Retorna uma cópia dessa classe.
  @Override
  public AdmClinica clone(){
    AdmClinica admClinica = null;
    try{
      admClinica = new AdmClinica(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return admClinica;
  }
  
  // Construtor polimórfico.
  public AdmClinica(int id,
                    int idAdm,
                    int idClinica)throws Exception{
    this.setId(id);
    this.setIdAdm(idAdm);
    this.setIdClinica(idClinica);
  }
  
  // Construtor default.
  public AdmClinica(){
    this.id        = 0;
    this.idAdm     = 0;
    this.idClinica = 0;
  }

  // Retorna o id da relação entre administrador e clínica.
  public int getId() {
    return id;
  }

  // Seta o id da relação entre administrador e clínica.
  public void setId(int id) throws Exception{
    if (id < 0)
      throw new Exception("Id do AdmClinica inválido.");
    this.id = id;
  }

  // Retorna o id do administrador.
  public int getIdAdm() {
    return idAdm;
  }

  // Seta o id do administrador.
  public void setIdAdm(int idAdm) throws Exception{
    if (idAdm < 0)
      throw new Exception("Id do administrador inválido.");
    this.idAdm = idAdm;
  }

  // Retorna o id da clínica.
  public int getIdClinica() {
    return idClinica;
  }

  // Seta o id da clínica.
  public void setIdClinica(int idClinica) throws Exception{
    if (idClinica < 0)
      throw new Exception("Id da clínica inválido.");
    this.idClinica = idClinica;
  }
}
