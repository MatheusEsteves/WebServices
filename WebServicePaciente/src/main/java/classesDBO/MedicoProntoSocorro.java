package classesDBO;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Representa uma relação entre médico e pronto socorro no qual ele atua.
 * @author Equipe do projeto Médico Fácil
*/
public class MedicoProntoSocorro implements Cloneable,Serializable{
	private int id; // Id da relação entre médico e pronto socorro.
	private Medico medico; // Id do médico relacionado.
	private ProntoSocorro prontoSocorro; // Id do pronto socorro relacionado.
	private boolean isPresente; // Indica se o médico está em plantão ou não.
	private byte[] biometria; // Dados biométricos do médico para o ponto.
	
	// Retorna um número relacionado à essa classe, para fins externos.
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.biometria);
		result = prime * result + Objects.hashCode(this.medico);
		result = prime * result + Objects.hashCode(this.prontoSocorro);
		return result;
	}

	// Compara essa classe com outra, em termos de atributos.
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MedicoProntoSocorro other = (MedicoProntoSocorro) obj;
		if (!Arrays.equals(biometria, other.biometria))
			return false;
		if (!Objects.equals(this.medico,other.medico))
			return false;
		if (!Objects.equals(this.prontoSocorro,other.prontoSocorro))
			return false;
		return true;
	}
	
	// Retorna os dados biométricos do médico.
	public byte[] getBiometria() {
		return biometria;
	}

	// Seta os dados biométricos do médico.
	public void setBiometria(byte[] biometria)throws Exception{
	    if (biometria == null)
	      throw new Exception("Biometria não fornecida.");
	    if (biometria.length == 0)
	      throw new Exception("Biometria não fornecida.");
		this.biometria = biometria;
	}

	// Retorna o id da relação entre médico e pronto socorro.
    public int getId() {
		return this.id;
	}

    // Seta o id da relação entre médico e pronto socorro.
	public void setId(int id)throws Exception{
	    if (id < 0)
	      throw new Exception("Id da relação entre médico e pronto socorro inválido.");
		this.id = id;
	}

	// Retorna o médico dessa relação.
	public Medico getMedico() {
		return this.medico;
	}

	// Seta o médico dessa relação.
	public void setMedico(Medico medico)throws Exception{
	    if (medico == null)
	      throw new Exception("Médico não fornecido.");
		this.medico = medico;
	}

	// Retorna o pronto socorro dessa relação.
	public ProntoSocorro getProntoSocorro() {
		return this.prontoSocorro;
	}

	// Seta o pronto socorro dessa relação.
	public void setProntoSocorro(ProntoSocorro prontoSocorro)throws Exception{
	    if (prontoSocorro == null)
	      throw new Exception("Pronto socorro não fornecido.");
		this.prontoSocorro = prontoSocorro;
	}

	// Retorna o se o médico está em plantão ou não.
	public boolean isPresente() {
		return this.isPresente;
	}

	// Seta se o médico está em plantão ou não.
	public void setPresente(boolean isPresente){
		this.isPresente = isPresente;
	}
  
	// Construtor polimórfico.
    public MedicoProntoSocorro(
    		          int id,
    		          Medico medico,
    		          ProntoSocorro prontoSocorro,
    		          boolean isPresente,
    		          byte[] biometria
    		        )throws Exception{
	  this.setId(id);
	  this.setMedico(medico);
	  this.setProntoSocorro(prontoSocorro);
	  this.setPresente(isPresente);
	  this.setBiometria(biometria);
    }
    
    // Construtor default.
    public MedicoProntoSocorro(){
        this.id              = 0;
        this.medico          = null;
        this.prontoSocorro   = null;
        this.isPresente      = false;
        this.biometria       = null;
    }
    
    // Retorna uma cópia dessa classe.
    @Override
    public MedicoProntoSocorro clone(){
      MedicoProntoSocorro medicoProntoSocorro = null;
      try{
    	medicoProntoSocorro = new MedicoProntoSocorro(
    	  this.id,
    	  this.medico,
    	  this.prontoSocorro,
    	  this.isPresente,
    	  this.biometria
    	);
      }
      catch (Exception e){
    	e.printStackTrace();
      }
      return medicoProntoSocorro;
    }
    
    // Construtor de cópia.
    public MedicoProntoSocorro(MedicoProntoSocorro medicoProntoSocorro)throws Exception{
      if (medicoProntoSocorro == null)
    	throw new Exception("MedicoProntoSocorro não fornecido para construtor de cópia.");
      this.id              = medicoProntoSocorro.id;
      this.medico          = medicoProntoSocorro.medico;
      this.prontoSocorro   = medicoProntoSocorro.prontoSocorro;
      this.isPresente      = medicoProntoSocorro.isPresente;
      this.biometria       = medicoProntoSocorro.biometria;
    }
}
