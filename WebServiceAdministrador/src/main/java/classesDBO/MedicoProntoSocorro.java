package classesDBO;

import java.util.Arrays;

/**
 * Representa uma relação entre médico e pronto socorro no qual ele atua.
 * @author Equipe do projeto Médico Fácil
*/
public class MedicoProntoSocorro implements Cloneable{
	private int id; // Id da relação entre médico e pronto socorro.
	private int idMedico; // Id do médico relacionado.
	private int idProntoSocorro; // Id do pronto socorro relacionado.
	private boolean isPresente; // Indica se o médico está em plantão ou não.
	private byte[] biometria; // Dados biométricos do médico para o ponto.
	
	// Retorna um número relacionado à essa classe, para fins externos.
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(biometria);
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

	// Retorna o id do médico.
	public int getIdMedico() {
		return this.idMedico;
	}

	// Seta o id do médico.
	public void setIdMedico(int idMedico)throws Exception{
	    if (idMedico < 0)
	      throw new Exception("Id do médico inválido.");
		this.idMedico = idMedico;
	}

	// Retorna o id do pronto socorro.
	public int getIdProntoSocorro() {
		return this.idProntoSocorro;
	}

	// Seta o id do pronto socorro.
	public void setIdProntoSocorro(int idProntoSocorro)throws Exception{
	    if (idProntoSocorro < 0)
	      throw new Exception("Id do pronto socorro inválido.");
		this.idProntoSocorro = idProntoSocorro;
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
    public MedicoProntoSocorro(int id,int idMedico,int idProntoSocorro,boolean isPresente,byte[] biometria)throws Exception{
	  this.setId(id);
	  this.setIdMedico(idMedico);
	  this.setIdProntoSocorro(idProntoSocorro);
	  this.setPresente(isPresente);
	  this.setBiometria(biometria);
    }
    
    // Construtor default.
    public MedicoProntoSocorro(){
        this.id              = 0;
        this.idMedico        = 0;
        this.idProntoSocorro = 0;
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
    	  this.idMedico,
    	  this.idProntoSocorro,
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
      this.idMedico        = medicoProntoSocorro.idMedico;
      this.idProntoSocorro = medicoProntoSocorro.idProntoSocorro;
      this.isPresente      = medicoProntoSocorro.isPresente;
      this.biometria       = medicoProntoSocorro.biometria;
    }
}
