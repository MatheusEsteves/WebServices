package com.administrador;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import classesDBO.MedicoClinica;
import classesDBO.MedicoProntoSocorro;
import classesDBO.Paciente;
import classesDBO.ProntoSocorro;
import classesValidacao.Validacao;
import classesBD.AdaptedPreparedStatement;
import classesDAO.AdmClinicas;
import classesDAO.AdmProntoSocorros;
import classesDAO.Adms;
import classesDAO.Cidades;
import classesDAO.Clinicas;
import classesDAO.Consultas;
import classesDAO.Convenios;
import classesDAO.Especialidades;
import classesDAO.Estados;
import classesDAO.Horarios;
import classesDAO.MedicoClinicas;
import classesDAO.MedicoProntoSocorros;
import classesDAO.Medicos;
import classesDAO.Pacientes;
import classesDAO.ProntoSocorros;
import classesDBO.Adm;
import classesDBO.AdmClinica;
import classesDBO.AdmProntoSocorro;
import classesDBO.Cidade;
import classesDBO.Clinica;
import classesDBO.Consulta;
import classesDBO.Convenio;
import classesDBO.Especialidade;
import classesDBO.Estado;
import classesDBO.Horario;
import classesDBO.InclusaoMedicoClinica;
import classesDBO.InclusaoMedicoProntoSocorro;
import classesDBO.LoginAdm;
import classesDBO.Medico;

@RestController
public class AdministradorController{
	
  private AdaptedPreparedStatement bd;
  private Adms adms;
  private AdmProntoSocorros admProntoSocorros;
  private AdmProntoSocorro admProntoSocorro; // Administrador do pronto socorro logado.
  private AdmClinicas admClinicas;
  private AdmClinica admClinica; // Administrador da clínica logado.
  private Adm adm;// Administrador logado.
  private Clinica clinica; // Clínica na qual o administrador trabalha;
  private ProntoSocorro prontoSocorro; // Pronto socorro no qual o administrador trabalha.
  private Medicos medicos;
  private MedicoClinicas medicoClinicas;
  private MedicoProntoSocorros medicoProntoSocorros;
  private Pacientes pacientes;
  private Consultas consultas;
  private Horarios horarios;
  private Clinicas clinicas;
  private ProntoSocorros prontoSocorros;
  private Estados estados;
  private Cidades cidades;
  private Especialidades especialidades;
  private Convenios convenios;
  
  @CrossOrigin
  @RequestMapping(value="/conectarBd",method = RequestMethod.GET)
  public ResponseEntity<Boolean> conectarBd() {
    try
	{
      this.bd = new AdaptedPreparedStatement(
       "org.postgresql.Driver",
       "jdbc:postgresql://localhost/MedicoFacilDataBase",
       "postgres",
       "112358"
      );  
      this.estados           = new Estados(this.bd);
      this.cidades           = new Cidades(this.bd);
      this.adms              = new Adms(this.bd);
      this.admProntoSocorros = new AdmProntoSocorros(this.bd);
      this.admClinicas       = new AdmClinicas(this.bd);
      this.medicos           = new Medicos(this.bd,this.estados);
      this.clinicas          = new Clinicas(this.bd,this.cidades,this.estados);
      this.medicoClinicas    = new MedicoClinicas(this.bd,this.medicos,this.clinicas);
      this.pacientes         = new Pacientes(this.bd,this.cidades,this.estados);
      this.horarios          = new Horarios(this.bd,this.medicos,this.clinicas,this.medicoClinicas);
      this.consultas         = new Consultas(this.bd,this.medicoClinicas,this.pacientes,this.horarios);
      this.prontoSocorros    = new ProntoSocorros(this.bd,this.cidades,this.estados);
      this.especialidades    = new Especialidades(this.bd);
      this.convenios         = new Convenios(this.bd);
	}
	catch(Exception e)
	{
      return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR); 
	}
    return new ResponseEntity<Boolean>(true,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/desconectarBd",method=RequestMethod.GET)
  public ResponseEntity<Boolean> desconectarBd(){
    try{
      this.bd.close();
    }
    catch (Exception e){
      return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Boolean>(true,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getAdmPorClinica",method=RequestMethod.GET)
  public ResponseEntity<Adm> getAdmPorClinica(){
    Adm adm = null;
    try{
	  adm = this.adms.getAdms("id = "+this.admClinica.getIdAdm()).get(0);
    }
    catch (Exception e){
      return new ResponseEntity<Adm>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Adm>(adm,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getAdmPorPs",method=RequestMethod.GET)
  public ResponseEntity<Adm> getAdmPorPs(){
    Adm adm = null;
    try{
	  adm = this.adms.getAdms("id = "+this.admProntoSocorro.getIdAdm()).get(0);
    }
    catch (Exception e){
      return new ResponseEntity<Adm>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Adm>(adm,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getAdmClinica",method=RequestMethod.GET)
  public ResponseEntity<AdmClinica> getAdmClinica(){
	return new ResponseEntity<AdmClinica>(this.admClinica,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getAdmPs",method=RequestMethod.GET)
  public ResponseEntity<AdmProntoSocorro> getAdmPs(){
	return new ResponseEntity<AdmProntoSocorro>(this.admProntoSocorro,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/isAdmClinica",method = RequestMethod.POST)
  public ResponseEntity<Boolean> isAdmClinica(@RequestBody(required=true) LoginAdm loginAdm)throws Exception{
	ArrayList<AdmClinica> admClinicas = null;
	try{
      admClinicas = this.admClinicas.getAdmClinicas(
        "idAdm in (select id from Adm_MF where usuario = '"+loginAdm.getUsuario()+"' and senha = '"+loginAdm.getSenha()+"')"
      );
    }
    catch (Exception e){
      return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
    }
	if (admClinicas.isEmpty())
      return new ResponseEntity<Boolean>(false,HttpStatus.OK);
	this.adm = this.adms.getAdms("usuario = '"+loginAdm.getUsuario()+"' and senha = '"+loginAdm.getSenha()+"'").get(0);
    return new ResponseEntity<Boolean>(true,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/isAdmPs",method = RequestMethod.POST)
  public ResponseEntity<Boolean> isAdmPs(@RequestBody(required=true) LoginAdm loginAdm)throws Exception{
    ArrayList<AdmProntoSocorro> admProntoSocorros = null;
    try{
      admProntoSocorros = this.admProntoSocorros.getAdmProntoSocorros(
        "idAdm in (select id from Adm_MF where usuario = '"+loginAdm.getUsuario()+"' and senha = '"+loginAdm.getSenha()+"')"
      );
    }
    catch (Exception e){
      return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    if (admProntoSocorros.isEmpty())
      return new ResponseEntity<Boolean>(false,HttpStatus.OK);
    this.adm = this.adms.getAdms("usuario = '"+loginAdm.getUsuario()+"' and senha = '"+loginAdm.getSenha()+"'").get(0);
    return new ResponseEntity<Boolean>(true,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getClinicas",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Clinica>> getClinicas(){
	ArrayList<Clinica> lista;
	try{
	  lista = this.clinicas.getClinicas(
	    "id in (select idClinica from AdmClinica_MF where idAdm in ("+
	    "select id from Adm_MF where usuario = '"+this.adm.getUsuario()+"' and senha = '"+this.adm.getSenha()+"'))"
	  );
	}
	catch (Exception e){
      return new ResponseEntity<ArrayList<Clinica>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Clinica>>(lista,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getProntoSocorros",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<ProntoSocorro>> getProntoSocorros(){
	ArrayList<ProntoSocorro> lista;
	try{
	  lista = this.prontoSocorros.getProntoSocorros(
	    "id in (select idPs from AdmPs_MF where idAdm in ("+
	    "select id from Adm_MF where usuario = '"+this.adm.getUsuario()+"' and senha = '"+this.adm.getSenha()+"'))"
	  );
	}
	catch (Exception e){
      return new ResponseEntity<ArrayList<ProntoSocorro>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<ProntoSocorro>>(lista,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/loginAdmClinica/{idClinica}",method=RequestMethod.GET)
  public ResponseEntity<Boolean> loginAdmClinica(@PathVariable(value="idClinica")int idClinica)throws Exception{
	try{
	  this.admClinica = this.admClinicas.getAdmClinicas(
	   "idClinica = "+idClinica+" and idAdm in (select id from Adm_MF where usuario = '"+
	    this.adm.getUsuario()+"' and senha = '"+this.adm.getSenha()+"')"
	  ).get(0);
	}
	catch (Exception e){
	  return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<Boolean>(true,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/loginAdmPs/{idPs}",method=RequestMethod.GET)
  public ResponseEntity<Boolean> loginAdmPs(@PathVariable(value="idPs")int idPs)throws Exception{
	try{
	  this.admProntoSocorro = this.admProntoSocorros.getAdmProntoSocorros(
	   "idPs = "+idPs+" and idAdm in (select id from Adm_MF where usuario = '"+
	    this.adm.getUsuario()+"' and senha = '"+this.adm.getSenha()+"')"
	  ).get(0);
	}
	catch (Exception e){
	  return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<Boolean>(true,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getConveniosPorClinica/{idClinica}",method = RequestMethod.GET)
  public ResponseEntity<ArrayList<Convenio>> getConveniosPorClinica(@PathVariable(value="idClinica")int idClinica){
	  ArrayList<Convenio> convenios;
	  try{
	    convenios = this.convenios.getConvenios(
	      "id in (select idConvenio from ConvenioClinica_MF where idClinica = "+idClinica+")"
	    );
	  }
	  catch (Exception e){
	    return new ResponseEntity<ArrayList<Convenio>>(HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	  return new ResponseEntity<ArrayList<Convenio>>(convenios,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getConveniosPorPs/{idPs}",method = RequestMethod.GET)
  public ResponseEntity<ArrayList<Convenio>> getConveniosPorPs(@PathVariable(value="idPs")int idPs){
	  ArrayList<Convenio> convenios;
	  try{
	    convenios = this.convenios.getConvenios(
	      "id in (select idConvenio from ConvenioPs_MF where idPs = "+idPs+")"
	    );
	  }
	  catch (Exception e){
	    return new ResponseEntity<ArrayList<Convenio>>(HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	  return new ResponseEntity<ArrayList<Convenio>>(convenios,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getEspecialidades/{idMedico}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Especialidade>> getEspecialidades(@PathVariable(value="idMedico")int idMedico){
    ArrayList<Especialidade> especialidades;
    try{
      especialidades = this.especialidades.getEspecialidades("id in (select idEspecialidade from MedicoEspecialidade_MF where idMedico = "+idMedico+")");
    }
    catch (Exception e){
	   return new ResponseEntity<ArrayList<Especialidade>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
	 return new ResponseEntity<ArrayList<Especialidade>>(especialidades,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/incluirMedicoClinica",method=RequestMethod.PUT)
  public ResponseEntity<Boolean> incluirMedico(@RequestBody(required=true) InclusaoMedicoClinica medicoClinica){
    try{
      Medico medico = medicoClinica.getMedico();
      if (!this.medicos.isMedicoExistente(medico.getCrm(),this.estados.getId(medico.getUf())))
        this.medicos.incluir(medicoClinica.getMedico());
      Medico medicoIncluido = this.medicos.getMedicos("crm = '"+medicoClinica.getMedico().getCrm()+"'").get(0);
      this.medicoClinicas.incluir(
        new MedicoClinica(
          0,medicoIncluido,this.clinicas.getClinicas("id = "+this.admClinica.getIdClinica()).get(0),medicoClinica.getSenha()
        )
      );
    }
    catch (Exception e){
      return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Boolean>(true,HttpStatus.OK);
  }
 
  @CrossOrigin
  @RequestMapping(value="/incluirMedicoPs",method=RequestMethod.PUT)
  public ResponseEntity<Boolean> incluirMedico(@RequestBody(required=true) InclusaoMedicoProntoSocorro medicoPs){
	try{
	  Medico medico = medicoPs.getMedico();
	  if (!this.medicos.isMedicoExistente(medico.getCrm(),this.estados.getId(medico.getUf())))
        this.medicos.incluir(medicoPs.getMedico());
      Medico medicoIncluido = this.medicos.getMedicos("crm = '"+medicoPs.getMedico().getCrm()+"'").get(0);
      this.medicoProntoSocorros.incluir(
        new MedicoProntoSocorro(
    	  0,medicoIncluido.getId(),this.admProntoSocorro.getIdProntoSocorro(),medicoPs.isPresente(),medicoPs.getBiometria()
    	)
      );
    }
    catch (Exception e){
  	  return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
  	}
	return new ResponseEntity<Boolean>(true,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/alterarMedico",method=RequestMethod.POST)
  public ResponseEntity<Boolean> alterarMedico(@RequestBody(required=true)Medico medico)throws Exception{
	try{
	  this.medicos.alterar(medico);
	}
	catch (Exception e){
	  return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<Boolean>(true,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/excluirMedicoClinica/{idMedico}",method={RequestMethod.GET,RequestMethod.DELETE})
  public ResponseEntity<Boolean> excluirMedicoClinica(@PathVariable(value="idMedico")int idMedico)throws Exception{
    try{
      this.medicoClinicas.excluir(
    	this.medicoClinicas.getId(idMedico,this.admClinica.getIdClinica())	  
      );
    }
    catch (Exception e){
      return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Boolean>(true,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/excluirMedicoPs/{idMedico}",method={RequestMethod.GET,RequestMethod.DELETE})
  public ResponseEntity<Boolean> excluirMedicoPs(@PathVariable(value="idMedico")int idMedico)throws Exception{
    try{
      this.medicoProntoSocorros.excluir(
    	this.medicoProntoSocorros.getId(idMedico,this.admProntoSocorro.getIdProntoSocorro())	  
      );
    }
    catch (Exception e){
      return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Boolean>(true,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getMedicosPs",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Medico>> getMedicosPs(){
    ArrayList<Medico> medicos = null;
	try{
	  medicos = this.medicos.getMedicos(
	    "id in (select idMedico from MedicoPs_MF where idPs = "+this.admProntoSocorro.getIdProntoSocorro()+")"  
	  );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getMedicosPlantao",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Medico>> getMedicosPlantao(){
    ArrayList<Medico> medicos = null;
	try{
	  medicos = this.medicos.getMedicos(
	    "id in (select idMedico from MedicoPs_MF where idPs = "+this.admProntoSocorro.getIdProntoSocorro()+" and presente = 'S')"  
	  );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/isMedicoPresente/{idMedico}",method=RequestMethod.GET)
  public ResponseEntity<Boolean> isMedicoPresente(@PathVariable(value="idMedico")int idMedico){
	ArrayList<Medico> medicos = null;
	try{
	  medicos = this.medicos.getMedicos(
	    "id = "+idMedico+" and id in (select idMedico from MedicoPs_MF where idPs = "+
	    this.admProntoSocorro.getIdProntoSocorro()+" and presente = 'S')"		  
	  );
	}
	catch (Exception e){
      return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	if (medicos.isEmpty())
      return new ResponseEntity<Boolean>(false,HttpStatus.OK);
	return new ResponseEntity<Boolean>(true,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getMedicosClinica",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Medico>> getMedicosClinica(){
    ArrayList<Medico> medicos = null;
	try{
	  medicos = this.medicos.getMedicos(
	    "id in (select idMedico from MedicoClinica_MF where idClinica = "+this.admClinica.getIdClinica()+")"  
	  );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getMedicosPorPaciente/{idPaciente}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Medico>> getMedicosPorPaciente(@PathVariable(value="idPaciente")int idPaciente){
    ArrayList<Medico> medicos = null;
	try{
	  medicos = this.medicos.getMedicos(
	    "id in (select idMedico from MedicoClinica_MF where id in ("+
	    "select idMedicoClinica from Horario_MF where id in ("+
	    "select idHorario from Consulta_MF where idPaciente = "+idPaciente+")) "+
	    "and idClinica = "+this.admClinica.getIdClinica()+")"  
	  );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getPacientesPorMedico/{idMedico}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Paciente>> getPacientesPorMedico(@PathVariable(value="idMedico")int idMedico){
    ArrayList<Paciente> pacientes = null;
    try{
      pacientes = this.pacientes.getPacientes(
        "id in (select idPaciente from Consulta_MF where idHorario in ("+
        "select id from Horario_MF where idMedicoClinica in ("+
        "select id from MedicoClinica_MF where idMedico = "+idMedico+" and idClinica = "+this.admClinica.getIdClinica()+")))"		  
      );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Paciente>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Paciente>>(pacientes,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getMedicosPorCrmPs/{crm}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Medico>> getMedicosPorCrmPs(@PathVariable(value="crm") String crm){
    ArrayList<Medico> medicos = null;
	try{
      medicos = this.medicos.getMedicos(
        "crm = '"+crm+"' and id in (select idMedico from MedicoPs_MF where idPs = "+this.admProntoSocorro.getIdProntoSocorro()+")"  
      );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getMedicosPorNomePs/{nome}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Medico>> getMedicosPorNomePs(@PathVariable(value="nome") String nome){
    ArrayList<Medico> medicos = null;
	try{
      medicos = this.medicos.getMedicos(
        "nome like '%"+nome+"%' and id in (select idMedico from MedicoPs_MF where idPs = "+this.admProntoSocorro.getIdProntoSocorro()+")"
      );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getMedicosPorEspecialidadePs/{idEspecialidade}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Medico>> getMedicosPorEspecialidadePs(@PathVariable(value="idEspecialidade") int idEspecialidade){
	ArrayList<Medico> medicos = null;
    try{
	  medicos = this.medicos.getMedicos(
	    "id in (select idMedico from MedicoEspecialidade_MF where idEspecialidade = "+idEspecialidade+") and id in (select idMedico from MedicoPs_MF where idPs = "+this.admProntoSocorro.getIdProntoSocorro()+")"
	  );
	}
	catch (Exception e){
	  return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getMedicosPorCrmClinica/{crm}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Medico>> getMedicosPorCrmClinica(@PathVariable(value="crm") String crm){
    ArrayList<Medico> medicos = null;
	try{
      medicos = this.medicos.getMedicos(
        "crm = '"+crm+"' and id in (select idMedico from MedicoClinica_MF where idClinica = "+this.admClinica.getIdClinica()+")"  
      );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getMedicosPorNomeClinica/{nome}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Medico>> getMedicosPorNomeClinica(@PathVariable(value="nome") String nome){
    ArrayList<Medico> medicos = null;
	try{
      medicos = this.medicos.getMedicos(
        "nome like '%"+nome+"%' and id in (select idMedico from MedicoClinica_MF where idClinica = "+this.admClinica.getIdClinica()+")"
      );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getMedicosPorEspecialidadeClinica/{idEspecialidade}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Medico>> getMedicosPorEspecialidadeClinica(@PathVariable(value="idEspecialidade") int idEspecialidade){
	ArrayList<Medico> medicos = null;
    try{
	  medicos = this.medicos.getMedicos(
        "id in (select idMedico from MedicoEspecialidade_MF where idEspecialidade = "+idEspecialidade+") and id in (select idMedico from MedicoClinica_MF where idClinica = "+this.admClinica.getIdClinica()+")"
	  );
	}
	catch (Exception e){
	  return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getPacientes",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Paciente>> getPacientes(){
	ArrayList<Paciente> pacientes = null;
	try{
      pacientes = this.pacientes.getPacientes(
        "id in ("+
          "select idPaciente from Consulta_MF where idHorario in ("+
            "select id from Horario_MF where idMedicoClinica in (select id from MedicoClinica_MF where idClinica = "+this.admClinica.getIdClinica()+")))"
      );
    }
	catch (Exception e){
	  return new ResponseEntity<ArrayList<Paciente>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Paciente>>(pacientes,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getPacientesPorNome/{nome}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Paciente>> getPacientesPorNome(@PathVariable(value="nome") String nome){
	ArrayList<Paciente> pacientes = null;
	try{
      pacientes = this.pacientes.getPacientes(
        "nome like '%"+nome+"%' and id in ("+
          "select idPaciente from Consulta_MF where idHorario in ("+
            "select id from Horario_MF where idMedicoClinica in (select id from MedicoClinica_MF where idClinica = "+this.admClinica.getIdClinica()+")))"
      );
    }
	catch (Exception e){
	  return new ResponseEntity<ArrayList<Paciente>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Paciente>>(pacientes,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getPacientesPorCpf/{cpf}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Paciente>> getPacientesPorCpf(@PathVariable(value="cpf") String cpf){
	ArrayList<Paciente> pacientes = null;
	try{
      pacientes = this.pacientes.getPacientes(
        "cpf = '"+cpf+"' and id in ("+
          "select idPaciente from Consulta_MF where idHorario in ("+
            "select id from Horario_MF where idMedicoClinica in (select id from MedicoClinica_MF idClinica = "+this.admClinica.getIdClinica()+")))"
      );
    }
	catch (Exception e){
	  return new ResponseEntity<ArrayList<Paciente>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Paciente>>(pacientes,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getConsultas",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Consulta>> getConsultas(){
    ArrayList<Consulta> consultas = null;
    try{
      consultas = this.consultas.getConsultas(
    	"idHorario in ("+
          "select id from Horario_MF where idMedicoClinica in ("+
    	  "select id from MedicoClinica_MF where "+
    	    "idClinica = "+this.admClinica.getIdClinica()+"))" 
      );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getConsultasPorMedico/{nome}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Consulta>> getConsultasPorMedico(@PathVariable(value="nome") String nome){
    ArrayList<Consulta> consultas = null;
    try{
      consultas = this.consultas.getConsultas(
    	"idHorario in ("+
          "select id from Horario_MF where idMedicoClinica in ("+
    	  "select id from MedicoClinica_MF where "+
    	    "idClinica = "+this.admClinica.getIdClinica()+" and "+
    	    "idMedico in (select id from Medico_MF where nome like '%"+nome+"%')))" 
      );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getConsultasPorIdMedico/{idMedico}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Consulta>> getConsultasPorMedico(@PathVariable(value="idMedico") int idMedico){
    ArrayList<Consulta> consultas = null;
    try{
      consultas = this.consultas.getConsultas(
    	"idHorario in ("+
    	  "select id from Horario_MF where idMedicoClinica in ("+
    	  "select id from MedicoClinica_MF where "+
    	    "idClinica = "+this.admClinica.getIdClinica()+" and "+
    	    "idMedico = "+idMedico+"))" 
      );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getConsultasPorPaciente/{nome}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Consulta>> getConsultasPorPaciente(@PathVariable(value="nome") String nome){
    ArrayList<Consulta> consultas = null;
    try{
      consultas = this.consultas.getConsultas(
    	"idPaciente in (select id from Paciente_MF where nome like '%"+nome+"%') and "+
        "idHorario in (select id from Horario_MF where "+
        "idMedicoClinica in (select id from MedicoClinica_MF where idClinica = "+this.admClinica.getIdClinica()+"))"
      );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getConsultasPorIdPaciente/{idPaciente}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Consulta>> getConsultasPorIdPaciente(@PathVariable(value="idPaciente")int idPaciente){
    ArrayList<Consulta> consultas = null;
    try{
      consultas = this.consultas.getConsultas(
    	"idPaciente = "+idPaciente+" and "+
        "idHorario in (select id from Horario_MF where "+
        "idMedicoClinica in (select id from MedicoClinica_MF where idClinica = "+this.admClinica.getIdClinica()+"))"
      );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getConsultasPorPacienteMedico/{idPaciente}/{idMedico}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Consulta>> getConsultas(
		                                             @PathVariable(value="idPaciente")int idPaciente,
		                                             @PathVariable(value="idMedico")int idMedico){
    ArrayList<Consulta> consultas = null;
    try{
      consultas = this.consultas.getConsultas(
    	"idPaciente = "+idPaciente+" and "+
        "idHorario in (select id from Horario_MF where "+
        "idMedicoClinica in (select id from MedicoClinica_MF where idMedico = "+idMedico+" and idClinica = "+this.admClinica.getIdClinica()+"))"
      );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getConsultasPorPacienteMedicoData/{idPaciente}/{idMedico}/{data}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Consulta>> getConsultas(
		                                             @PathVariable(value="idPaciente")int idPaciente,
		                                             @PathVariable(value="idMedico")int idMedico,
		                                             @PathVariable(value="data")Date data){
    ArrayList<Consulta> consultas = null;
    try{
      consultas = this.consultas.getConsultas(
    	"idPaciente = "+idPaciente+" and "+
        "idHorario in (select id from Horario_MF where data = '"+data.toString()+"' and "+
        "idMedicoClinica in (select id from MedicoClinica_MF where idMedico = "+idMedico+" and idClinica = "+this.admClinica.getIdClinica()+"))"
      );
    }
    catch (Exception e){
      return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getHorarios",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Horario>> getHorarios(){
	ArrayList<Horario> horarios = null;
	try{
	  horarios = this.horarios.getHorarios(
	    "idMedicoClinica in (select id from MedicoClinica_MF where idClinica = "+this.admClinica.getIdClinica()+")"
	  );
	}
	catch (Exception e){
	  return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getHorariosPorCrm/{crm}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Horario>> getHorariosPorCrm(@PathVariable(value="crm") String crm){
	ArrayList<Horario> horarios = null;
	try{
	  horarios = this.horarios.getHorarios(
	    "idMedicoClinica in (select id from MedicoClinica_MF where "+
	    "idMedico in (select id from Medico_MF where crm = '"+crm+"') and "+
	    "idClinica = "+this.admClinica.getIdClinica()+")"
	  );
	}
	catch (Exception e){
	  return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getHorariosPorMedico/{idMedico}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Horario>> getHorariosPorMedico(@PathVariable(value="idMedico")int idMedico){
	ArrayList<Horario> horarios = null;
	try{
	  horarios = this.horarios.getHorarios(
	    "idMedicoClinica in (select id from MedicoClinica_MF where "+
	    "idMedico = "+idMedico+" and "+
	    "idClinica = "+this.admClinica.getIdClinica()+")"
	  );
	}
	catch (Exception e){
	  return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getHorariosPorMedicoData/{idMedico}/{data}",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Horario>> getHorariosPorMedicoData(
		                                       @PathVariable(value="idMedico")int idMedico,
		                                       @PathVariable(value="data")Date data){
	ArrayList<Horario> horarios = null;
	try{
	  horarios = this.horarios.getHorarios(
	    "data = '"+data.toString()+"' and "+
	    "idMedicoClinica in (select id from MedicoClinica_MF where "+
	    "idMedico = "+idMedico+" and "+
	    "idClinica = "+this.admClinica.getIdClinica()+")"
	  );
	}
	catch (Exception e){
	  return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getEstados",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Estado>> getEstados(){
	ArrayList<Estado> estados;
	try{
	  estados = this.estados.getEstados();
	}
	catch (Exception e){
	  return new ResponseEntity<ArrayList<Estado>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Estado>>(estados,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getCidades/idEstado",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Cidade>> getCidades(@PathVariable(value="idEstado")int idEstado){
	ArrayList<Cidade> cidades;
	try{
	  cidades = this.cidades.getCidades("idEstado = "+idEstado);
	}
	catch (Exception e){
	  return new ResponseEntity<ArrayList<Cidade>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Cidade>>(cidades,HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value="/getEspecialidades",method=RequestMethod.GET)
  public ResponseEntity<ArrayList<Especialidade>> getEspecialidades(){
	ArrayList<Especialidade> especialidades;
	try{
	  especialidades = this.especialidades.getEspecialidades();
	}
	catch (Exception e){
	  return new ResponseEntity<ArrayList<Especialidade>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<ArrayList<Especialidade>>(especialidades,HttpStatus.OK);
  }
  
  /**
   * Método que retorna o id de uma cidade com nome e estado especificados.
   * @param String nome : Nome da cidade
   * @param String uf : Sigla do estado no qual a cidade se localiza
   * @return Integer : Id da cidade
   * @throws Exception
   */
  @CrossOrigin
  @RequestMapping(value="/getIdCidade/{nome}/{uf}",method=RequestMethod.GET)
  public ResponseEntity<Integer> getIdCidade(
  		                         @PathVariable(value="nome")String nome,
  		                         @PathVariable(value="uf")String uf
  		                       )throws Exception{
    int idCidade;
    try{
      idCidade = this.cidades.getId(nome,uf);
    }
    catch (Exception e){
      return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Integer>(idCidade,HttpStatus.OK);
  }
  
  /**
   * Método que retorna um objeto de uma cidade com id especificado
   * @param int id : Id da cidade
   * @return Cidade : Objeto de cidade
   */
  @CrossOrigin
  @RequestMapping(value="/getCidade/{id}",method=RequestMethod.GET)
  public ResponseEntity<Cidade> getCidade(@PathVariable(value="id")int id){
    Cidade cidade;
    try{
  	cidade = this.cidades.getCidades("id = "+id).get(0);
    }
    catch (Exception e){
  	return new ResponseEntity<Cidade>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Cidade>(cidade,HttpStatus.OK);
  }
  
  /**
   * Método que retorna o id de uma clínica com nome especificado
   * @param String nome : Nome da clínica
   * @return Integer : Id da clínica
   * @throws Exception
   */
  @CrossOrigin
  @RequestMapping(value="/getIdClinica/{nome}",method=RequestMethod.GET)
  public ResponseEntity<Integer> getIdCidade(
  		                         @PathVariable(value="nome")String nome
  		                       )throws Exception{
    int idClinica;
    try{
      idClinica = this.clinicas.getId(nome);
    }
    catch (Exception e){
      return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Integer>(idClinica,HttpStatus.OK);
  }
  
  /**
   * Método que retorna um objeto de uma clínica com id especificado
   * @param int id : Id da clínica
   * @return Clinica : Objeto de clínica
   */
  @CrossOrigin
  @RequestMapping(value="/getClinica/{id}",method=RequestMethod.GET)
  public ResponseEntity<Clinica> getClinica(@PathVariable(value="id")int id){
    Clinica clinica;
    try{
  	clinica = this.clinicas.getClinicas("id = "+id).get(0);
    }
    catch (Exception e){
  	return new ResponseEntity<Clinica>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Clinica>(clinica,HttpStatus.OK);
  }
  
  /**
   * Método que retorna um objeto de uma consulta com id especificado
   * @param int id : Id da consulta
   * @return Consulta : Objeto de consulta
   */
  @CrossOrigin
  @RequestMapping(value="/getConsulta/{id}",method=RequestMethod.GET)
  public ResponseEntity<Consulta> getConsulta(@PathVariable(value="id")int id){
    Consulta consulta;
    try{
  	consulta = this.consultas.getConsultas("id = "+id).get(0);
    }
    catch (Exception e){
  	return new ResponseEntity<Consulta>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Consulta>(consulta,HttpStatus.OK);
  }
  
  /**
   * Método que retorna o id de uma especialidade médica com nome especificado
   * @param String nome : Nome da especialidade médica
   * @return Integer : Id da especialidade médica
   * @throws Exception
   */
  @CrossOrigin
  @RequestMapping(value="/getIdEspecialidade/{nome}",method=RequestMethod.GET)
  public ResponseEntity<Integer> getIdEspecialidade(
  		                         @PathVariable(value="nome")String nome
  		                       )throws Exception{
    int idEspecialidade;
    try{
      idEspecialidade = this.especialidades.getId(nome);
    }
    catch (Exception e){
      return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Integer>(idEspecialidade,HttpStatus.OK);
  }
  
  /**
   * Método que retorna um objeto de uma especialidade médica com id especificado
   * @param int id : Id da especialidade médica
   * @return Especialidade : Objeto de especialidade médica
   */
  @CrossOrigin
  @RequestMapping(value="/getEspecialidade/{id}",method=RequestMethod.GET)
  public ResponseEntity<Especialidade> getEspecialidade(@PathVariable(value="id")int id){
    Especialidade especialidade;
    try{
  	especialidade = this.especialidades.getEspecialidades("id = "+id).get(0);
    }
    catch (Exception e){
  	return new ResponseEntity<Especialidade>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Especialidade>(especialidade,HttpStatus.OK);
  }
  
  /**
   * Método que retorna o id de um estado com sigla informada
   * @param String uf : Sigla do estado
   * @return Integer : Id do estado
   * @throws Exception
   */
  @CrossOrigin
  @RequestMapping(value="/getIdEstado/{uf}",method=RequestMethod.GET)
  public ResponseEntity<Integer> getIdEstado(
  		                         @PathVariable(value="uf")String uf
  		                       )throws Exception{
    int idEstado;
    try{
      idEstado = this.estados.getId(uf);
    }
    catch (Exception e){
      return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Integer>(idEstado,HttpStatus.OK);
  }
  
  /**
   * Método que retorna um objeto de um estado com id especificado
   * @param int id : Id do estado
   * @return Estado : Objeto de estado
   */
  @CrossOrigin
  @RequestMapping(value="/getEstado/{id}",method=RequestMethod.GET)
  public ResponseEntity<Estado> getEstado(@PathVariable(value="id")int id){
    Estado estado;
    try{
  	estado = this.estados.getEstados("id = "+id).get(0);
    }
    catch (Exception e){
  	return new ResponseEntity<Estado>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Estado>(estado,HttpStatus.OK);
  }
  
  /**
   * Método que retorna o id de um médico com crm especificado
   * @param String crm : CRM do médico
   * @return Integer : Id do médico
   * @throws Exception
   */
  @CrossOrigin
  @RequestMapping(value="/getIdMedico/{crm}",method=RequestMethod.GET)
  public ResponseEntity<Integer> getIdMedico(
  		                         @PathVariable(value="crm")String crm
  		                       )throws Exception{
    int idMedico;
    try{
      idMedico = this.medicos.getId(crm);
    }
    catch (Exception e){
      return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Integer>(idMedico,HttpStatus.OK);
  }
  
  /**
   * Método que retorna um objeto de um médico com id especificado
   * @param int id : Id do médico
   * @return Medico : Objeto de médico
   */
  @CrossOrigin
  @RequestMapping(value="/getMedico/{id}",method=RequestMethod.GET)
  public ResponseEntity<Medico> getMedico(@PathVariable(value="id")int id){
    Medico medico;
    try{
  	medico = this.medicos.getMedicos("id = "+id).get(0);
    }
    catch (Exception e){
  	return new ResponseEntity<Medico>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Medico>(medico,HttpStatus.OK);
  }
  
  /**
   * Método que retorna o id de um paciente com cpf especificado
   * @param String cpf : CPF do paciente
   * @return Integer : Id do paciente
   * @throws Exception
   */
  @CrossOrigin
  @RequestMapping(value="/getIdPaciente/{cpf}",method=RequestMethod.GET)
  public ResponseEntity<Integer> getIdPaciente(
  		                         @PathVariable(value="cpf")String cpf
  		                       )throws Exception{
    int idPaciente;
    try{
      idPaciente = this.pacientes.getId(cpf);
    }
    catch (Exception e){
      return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Integer>(idPaciente,HttpStatus.OK);
  }
  
  /**
   * Método que retorna um objeto de paciente com id especificado
   * @param int id : Id do paciente
   * @return Paciente : Objeto de paciente
   */
  @CrossOrigin
  @RequestMapping(value="/getPacientePorId/{id}",method=RequestMethod.GET)
  public ResponseEntity<Paciente> getPacientePorId(@PathVariable(value="id")int id){
    Paciente paciente;
    try{
  	paciente = this.pacientes.getPacientes("id = "+id).get(0);
    }
    catch (Exception e){
  	return new ResponseEntity<Paciente>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Paciente>(paciente,HttpStatus.OK);
  }
  
  /**
   * Método que retorna o id de um pronto socorro com nome especificado
   * @param String nome : Nome do pronto socorro
   * @return Integer : Id do pronto socorro
   * @throws Exception
   */
  @CrossOrigin
  @RequestMapping(value="/getIdProntoSocorro/{nome}",method=RequestMethod.GET)
  public ResponseEntity<Integer> getIdProntoSocorro(
  		                         @PathVariable(value="nome")String nome
  		                       )throws Exception{
    int idProntoSocorro;
    try{
      idProntoSocorro = this.prontoSocorros.getId(nome);
    }
    catch (Exception e){
      return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Integer>(idProntoSocorro,HttpStatus.OK);
  }
  
  /**
   * Método que retorna um objeto de pronto socorro com id especificado
   * @param int id : Id do paciente
   * @return ProntoSocorro : Objeto de pronto socorro
   */
  @CrossOrigin
  @RequestMapping(value="/getProntoSocorro/{id}",method=RequestMethod.GET)
  public ResponseEntity<ProntoSocorro> getProntoSocorro(@PathVariable(value="id")int id){
    ProntoSocorro prontoSocorro;
    try{
  	prontoSocorro = this.prontoSocorros.getProntoSocorros("id = "+id).get(0);
    }
    catch (Exception e){
  	return new ResponseEntity<ProntoSocorro>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ProntoSocorro>(prontoSocorro,HttpStatus.OK);
  }
}