package classesDBO;

import java.io.Serializable;

public class InclusaoMedicoProntoSocorro implements Serializable{

	private Medico medico;
	private boolean isPresente;
	private byte[] biometria;
	
	public InclusaoMedicoProntoSocorro(){
	  this.medico     = null;
	  this.isPresente = false;
	  this.biometria  = null;
	}
	
	public InclusaoMedicoProntoSocorro(Medico medico,boolean isPresente,byte[] biometria)throws Exception{
	  this.setMedico(medico);
	  this.setPresente(isPresente);
	  this.setBiometria(biometria);
	}
	
	public void setMedico(Medico medico)throws Exception{
	  if (medico == null)
	    throw new Exception("Médico não fornecido.");
	  this.medico = medico;
	}
	
	public void setPresente(boolean isPresente){
	  this.isPresente = isPresente;
	}
	
    public void setBiometria(byte[] biometria)throws Exception{
      if (biometria == null)
    	throw new Exception("Biometria não fornecida.");
      this.biometria = biometria;
    }
    
    public Medico getMedico(){
      return this.medico;
    }
    
    public boolean isPresente(){
      return this.isPresente;
    }
    
    public byte[] getBiometria(){
      return this.biometria;
    }
}