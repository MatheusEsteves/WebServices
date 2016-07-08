package classesDBO;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa uma relação entre médico e clínica na qual ele atua.
 * @author Equipe do projeto Médico Fácil
*/
public class MedicoClinica implements Cloneable,Serializable{
      
  private int id; // Id da relação entre médico e clínica.
  private Medico medico;
  private Clinica clinica;
  private String senha; // Senha para o médico acessar o sistema de clínica.
  
  private final int MAX_SENHA =  15; // Máximo de caracteres para a senha.
  
  // Retorna um número relacionado à essa classe, para fins externos.
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + this.id;
    hash = 97 * hash + Objects.hashCode(this.medico);
    hash = 97 * hash + Objects.hashCode(this.clinica);
    hash = 97 * hash + Objects.hashCode(this.senha);
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
    final MedicoClinica other = (MedicoClinica) obj;
    if (this.id != other.id) {
        return false;
    }
    if (!this.medico.equals(other.medico)){
        return false;
    }
    if (!this.clinica.equals(other.clinica)){
        return false;
    }
    if (!this.senha.equals(other.senha)){
        return false;
    }
    return true;
  }
  
  // Construtor de cópia.
  public MedicoClinica(MedicoClinica medicoClinica)throws Exception{
    if (medicoClinica == null)
      throw new Exception("MedicoClinica não fornecido para construtor de cópia.");
    this.id        = medicoClinica.getId();
    this.medico    = medicoClinica.getMedico();
    this.clinica   = medicoClinica.getClinica();
    this.senha     = medicoClinica.getSenha();
  }
  
  // Construtor polimórfico.
  public MedicoClinica(int id,
                       Medico medico,
                       Clinica clinica,
                       String senha)throws Exception{
    this.setId(id);
    this.setMedico(medico);
    this.setClinica(clinica);
    this.setSenha(senha);
  }
  
  // Construtor default.
  public MedicoClinica(){
    this.id        = 0;
    this.medico    = null;
    this.clinica   = null;
    this.senha     = "";
  }
  
  // Retorna uma cópia dessa classe.
  @Override
  public MedicoClinica clone(){
    MedicoClinica medicoClinica = null;
    try{
      medicoClinica = new MedicoClinica(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return medicoClinica;
  }

  // Retorna o id da relação entre médico e clínica.
  public int getId(){
    return id;
  }

  // Seta o id da relação entre médico e clínica.
  public void setId(int id)throws Exception{
    if (id < 0)
      throw new Exception("Id da relação entre médico e clínica inválido.");
    this.id = id;
  }

  // Retorna o médico dessa relação.
  public Medico getMedico(){
    return this.medico;
  }

  // Seta o médico dessa relação.
  public void setMedico(Medico medico)throws Exception{
    if (medico == null)
      throw new Exception("Médico não fornecido.");
    this.medico = medico;
  }

  // Retorna a clínica dessa relação.
  public Clinica getClinica(){
    return this.clinica;
  }

  // Seta a clínica dessa relação.
  public void setClinica(Clinica clinica)throws Exception{
    if (clinica == null)
      throw new Exception("Clínica não fornecida.");
    this.clinica = clinica;
  }
  
  // Retorna a senha do médico para acessar o sistema da clínica.
  public String getSenha(){
    return this.senha;
  }
  
  // Seta a senha do médico para acessar o sistema da clínica.
  public void setSenha(String senha)throws Exception{
    if (senha == null)
      throw new Exception("Senha não fornecida.");
    if (senha.trim().equals(""))
      throw new Exception("Senha não fornecida.");
    if (senha.length() > this.MAX_SENHA)
      throw new Exception("Senha com número de caracteres não fornecido.");
    
    this.senha = senha;
  }
}
