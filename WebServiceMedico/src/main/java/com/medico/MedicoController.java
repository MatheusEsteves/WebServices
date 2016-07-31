package com.medico;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import classesBD.AdaptedPreparedStatement;
import classesBD.AdaptedResultSet;
import classesDAO.Cidades;
import classesDAO.Clinicas;
import classesDAO.Consultas;
import classesDAO.Convenios;
import classesDAO.Especialidades;
import classesDAO.Estados;
import classesDAO.Horarios;
import classesDAO.MedicoClinicas;
import classesDAO.Medicos;
import classesDAO.Pacientes;
import classesDBO.Clinica;
import classesDBO.Consulta;
import classesDBO.Convenio;
import classesDBO.CustomTimeDeserializer;
import classesDBO.Especialidade;
import classesDBO.Horario;
import classesDBO.LoginMedico;
import classesDBO.Medico;
import classesDBO.MedicoClinica;
import classesValidacao.Validacao;

@RestController
public class MedicoController {
	
    private AdaptedPreparedStatement bd;
    private Medico medico; // Médico logado.
    private Clinica clinica; // Clínica na qual o médico logado trabalha.
    private MedicoClinica medicoClinica; // Relação entre médico e clínica do médico logado.
    private Horarios horarios;
    private Medicos medicos;
    private Clinicas clinicas;
    private Estados estados;
    private Cidades cidades;
    private MedicoClinicas medicoClinicas;
    private Pacientes pacientes;
    private Consultas consultas;
    private Especialidades especialidades;
    private Convenios convenios;
    
   /**
    * Retorna os dados do médico que está logado.
   */
   @CrossOrigin
   @RequestMapping(value="/getMedico",method=RequestMethod.GET)
   public ResponseEntity<Medico> getMedico(){
	 return new ResponseEntity<Medico>(this.medico,HttpStatus.OK);
   }
   
   /**
    * Retorna os dados da clínica na qual o médico logado trabalha.
   */
   @CrossOrigin
   @RequestMapping(value="/getClinica",method=RequestMethod.GET)
   public ResponseEntity<Clinica> getClinica(){
	 return new ResponseEntity<Clinica>(this.clinica,HttpStatus.OK);
   }
    
    /**
    * Este método funciona como um construtor, criando uma conexão com o banco de dados
    * e inicializando as váriaveis que farão acesso ao mesmo.
    **/
   @CrossOrigin
   @RequestMapping(value="/conectarBd",method = RequestMethod.GET)
   public ResponseEntity<Boolean> conectarBd() {
       try
	   {
         this.bd = new AdaptedPreparedStatement(
           "org.postgresql.Driver",
           "jdbc:postgresql://pellefant-02.db.elephantsql.com:5432/pdenoczt", 
           "pdenoczt", 
           "5u3vh6p8J3bRCO9ZbH_lh7iN3VRoeJOn" 
         );  
         this.estados  = new Estados(this.bd);
         this.cidades  = new Cidades(this.bd);
         this.medicos  = new Medicos(this.bd,this.estados);
         this.clinicas = new Clinicas(this.bd,this.cidades,this.estados);
         this.medicoClinicas = new MedicoClinicas(this.bd,this.medicos,this.clinicas);
         this.horarios = new Horarios(this.bd,this.medicos,this.clinicas,this.medicoClinicas);
         this.pacientes = new Pacientes(this.bd,this.cidades,this.estados);
         this.consultas = new Consultas(this.bd,this.medicoClinicas,this.pacientes,this.horarios);
         this.especialidades = new Especialidades(this.bd);
         this.convenios = new Convenios(this.bd);
	   }
	   catch(Exception e)
	   {
		 return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR); 
	   }
       return new ResponseEntity<Boolean>(true,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/desconectarBd",method = RequestMethod.GET)
   public ResponseEntity<Boolean> desconectarBd(){
	  try{
		this.bd.close();
	  }
	  catch (Exception e){
		return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	  return new ResponseEntity<Boolean>(true,HttpStatus.OK);
   }

   /**
      * Este método verifica se existe um médico com o Crm e senha digitados, caso exista  retorna true
      * caso contrário false.
   **/
   @CrossOrigin
   @RequestMapping(value="/isMedicoClinica",method = RequestMethod.POST)
   public ResponseEntity<Boolean> isMedicoClinica(@RequestBody(required=true) LoginMedico loginMedico){
     ArrayList<Medico> medicos = null;
   	 try{
       medicos = this.medicos.getMedicos(
    	"crm = '"+loginMedico.getCrm()+"' and id in (select idMedico from MedicoClinica_MF where senha = '"+loginMedico.getSenha()+"')"
       );
     }
     catch (Exception e){
       return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
     }
   	 if (medicos.isEmpty())
       return new ResponseEntity<Boolean>(false,HttpStatus.OK);
   	 this.medico = medicos.get(0);
     return new ResponseEntity<Boolean>(true,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/isConsultasExistentes/{idHorario}",method=RequestMethod.GET)
   public ResponseEntity<Boolean> isConsultasExistentes(@PathVariable("idHorario")int idHorario){
	 try{
	   return new ResponseEntity<Boolean>(this.consultas.isConsultaExistentePorMedico(idHorario),HttpStatus.OK);
	 }
	 catch (Exception e){
	   return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
	 }
   }
   
   @CrossOrigin
   @RequestMapping(value="/getClinicas",method=RequestMethod.POST)
   public ResponseEntity<ArrayList<Clinica>> getClinicas(@RequestBody(required=true) LoginMedico loginMedico){
	 ArrayList<Clinica> clinicas = null;
	 try{
	   clinicas = this.clinicas.getClinicas(
		 "id in (select idClinica from MedicoClinica_MF where senha = '"+loginMedico.getSenha()+"' and "+
	     "idMedico in (select id from Medico_MF where crm = '"+loginMedico.getCrm()+"'))"	   
	   );
	 }
	 catch (Exception e){
	   return new ResponseEntity<ArrayList<Clinica>>(HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	 return new ResponseEntity<ArrayList<Clinica>>(clinicas,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/loginMedico/{idClinica}",method=RequestMethod.GET)
   public ResponseEntity<Boolean> loginMedico(@PathVariable(value="idClinica")int idClinica){
	 try{
       this.clinica = this.clinicas.getClinicas("id = "+idClinica).get(0);
       this.medicoClinica = this.medicoClinicas.getMedicoClinicas(
    	 "idMedico = "+this.medico.getId()+" and idClinica = "+idClinica
       ).get(0);
     }
	 catch (Exception e){
	   return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	 return new ResponseEntity<Boolean>(true,HttpStatus.OK);
   }
   
   /**
    * Este método altera os dados do médico passado como parâmetro, isto é utiliza-se o id como condição
    * de alteração, e todos os outros campos são alerados
    **/
   @CrossOrigin
   @RequestMapping(value = "/alterarMedico", method = RequestMethod.POST)
   public ResponseEntity<Boolean> alterarMedico(@RequestBody(required=true) Medico medico)
   {
	  try
	  {
		medico.setCrm(this.medico.getCrm());
		this.medicos.alterar(medico);
	  }
	  catch(Exception e)
	  {
	    return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);   
	  }
      return new ResponseEntity<Boolean>(true,HttpStatus.OK);
   }
   
   /**
    * Este método permite o médico cadastar vários horários
    **/
   @CrossOrigin
   @RequestMapping(value = "/cadastrarHorario/{dia}/{dataInicio}/{dataFim}/{horaInicio}/{periodoDuracao}/{periodoIntervalo}/{qtdConsultas}", method = {RequestMethod.PUT,RequestMethod.GET})
   public ResponseEntity<Boolean> cadastrarHorario(
		                           @PathVariable(value="dia") String dia,
		                           @PathVariable(value="dataInicio")Date dataInicio,
		                           @PathVariable(value="dataFim")Date dataFim,
		                           @PathVariable(value="horaInicio")Time horaInicio, 
		                           @PathVariable(value="periodoDuracao")Time periodoDuracao, 
		                           @PathVariable(value="periodoIntervalo")Time periodoIntervalo,
		                           @PathVariable(value="qtdConsultas") int qtdConsultas
		                          )
   {
	   int numeroDia = Validacao.getNumeroDia(dia);
	   long tempoDataInicio = dataInicio.getTime();
	   long tempoDataFim    = dataFim.getTime();
	   long tempoUmDia      = 24*60*60*1000;
	   for (long tempoData = tempoDataInicio; tempoData <= tempoDataFim; tempoData += tempoUmDia){
		 Date data = new Date(tempoData);
	     if (data.getDay() == numeroDia){
           //guarda o horário de início do médico, ou seja, o momento em que o médico
           //inicia a consulta (em minutos. Ex. Se o médico começa consultar às 8:20 
           //inicioTrabalho
           //terá o valor 500, porque 8*60+20 = 500 minutos)
           int inicioTrabalho = horaInicio.getHours()*60+horaInicio.getMinutes();   
       
           //o intervalo entre duas consultados (em minutos)
           int intervaloConsulta = periodoIntervalo.getHours()*60+periodoIntervalo.getMinutes();
       
           //a duração de uma consulta (em minutos)
           int duracaoConsulta = periodoDuracao.getHours()*60+periodoDuracao.getMinutes();     
       
           Time horaInicioConsulta = null;
           Time horaFimConsulta    = null;
           int inicioConsulta, fimConsulta;
       
           //gera todos os registros de horário a ser cadastrado
           for(int i = 0; i < qtdConsultas; i++)
           {       
             //calcula qual será o horario de início e de fim de cada consulta
             inicioConsulta = inicioTrabalho + i*(duracaoConsulta +intervaloConsulta);
             fimConsulta = inicioConsulta+duracaoConsulta;
         
             int horas,minutos;
             // Converte a hora de início da consulta de minutos para horas e minutos.
             minutos = inicioConsulta%60;
             horas   = (inicioConsulta-minutos)/60;
             horaInicioConsulta = new Time(horas,minutos,0);
         
             // Converte a hora de término da consulta de minutos para horas e minutos.
             minutos = fimConsulta%60;
             horas   = (fimConsulta-minutos)/60;
             horaFimConsulta = new Time(horas,minutos,0);
         
             try
             {
        	   Horario horario = new Horario(0,this.medico,this.clinica,data,horaInicioConsulta,horaFimConsulta);
         	   this.horarios.incluir(horario);
             }
             catch(Exception e)
             {
        	   return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
             }
           }
         }    
	   }
       return new ResponseEntity<Boolean>(true,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value = "/alterarHorario", method = RequestMethod.POST)
   public ResponseEntity<Boolean> alterarHorario(@RequestBody(required=true) Horario horario){
	   try
	   {
		 horario.setMedico(this.medico);
		 horario.setClinica(this.clinica);
		 this.horarios.alterar(horario);
	   }
	   catch(Exception e)
	   {
		 return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
	   }
       return new ResponseEntity<Boolean>(true,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/excluirHorario/{idHorario}",method={RequestMethod.DELETE,RequestMethod.GET})
   public ResponseEntity<Boolean> excluirHorario(@PathVariable(value="idHorario") int idHorario){
	  try{
		this.horarios.excluir(idHorario);
	  }
	  catch (Exception e){
    	return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
      }
	  return new ResponseEntity<Boolean>(true,HttpStatus.OK);
   }

   @CrossOrigin
   @RequestMapping(value="/getHorariosPorData/{data}",method = RequestMethod.GET)
   public ResponseEntity<ArrayList<Horario>> getHorarios(@PathVariable(value="data") Date data)
   {
       ArrayList<Horario> horarios;
       try{
    	  horarios = this.horarios.getHorarios("data = '"+data.toString()+"'");
       }
       catch (Exception e){
    	  return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
       return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getHorariosPorDia/{dia}",method = RequestMethod.GET)
   public ResponseEntity<ArrayList<Horario>> getHorarios(@PathVariable(value="dia") String dia){
     ArrayList<Horario> horarios;
     int numeroDia = Validacao.getNumeroDia(dia);
     if (numeroDia == -1)
       return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
     try{
       horarios = this.horarios.getHorarios("extract(dow from data) = "+numeroDia);
     }
     catch (Exception e){
       return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
     return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getConsultas",method=RequestMethod.GET)
   public ResponseEntity<ArrayList<Consulta>> getConsultas(){
	 ArrayList<Consulta> consultas;
	 try{
	   consultas = this.consultas.getConsultas("idHorario in (select id from Horario_MF where idMedicoClinica = "+this.medicoClinica.getId()+")");
	 }
	 catch (Exception e){
	   return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	 return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getConsultasPorData/{data}",method=RequestMethod.GET)
   public ResponseEntity<ArrayList<Consulta>> getConsultas(@PathVariable(value="data")Date data){
	 ArrayList<Consulta> consultas;
	 try{
	   consultas = this.consultas.getConsultas(
	     "idHorario in (select id from Horario_MF where idMedicoClinica = "+this.medicoClinica.getId()+" and "+
	     "data = '"+data.toString()+"')"
	   );
	 }
	 catch (Exception e){
	   return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	 return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getConsultasPorDia/{dia}",method=RequestMethod.GET)
   public ResponseEntity<ArrayList<Consulta>> getConsultas(@PathVariable(value="dia")String dia){
	 int numeroDia = Validacao.getNumeroDia(dia);
	 if (numeroDia == -1)
	   return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
	 ArrayList<Consulta> consultas;
	 try{
	   consultas = this.consultas.getConsultas(
	     "idHorario in (select id from Horario_MF where idMedicoClinica = "+this.medicoClinica.getId()+" and "+
	     "extract(dow from data) = "+numeroDia+")"
	   );
	 }
	 catch (Exception e){
	   return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	 return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getConsulta/{idHorario}",method=RequestMethod.GET)
   public ResponseEntity<Consulta> getConsulta(@PathVariable(value="idHorario")int idHorario){
	 ArrayList<Consulta> consultas;
	 Consulta consulta;
	 try{
	   consultas = this.consultas.getConsultas("idHorario = "+idHorario);
	   if (consultas.isEmpty())
		 consulta = null;
	   else
		 consulta = consultas.get(0);
	 }
	 catch (Exception e){
	   return new ResponseEntity<Consulta>(HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	 return new ResponseEntity<Consulta>(consulta,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getHorariosLivres",method=RequestMethod.GET)
   public ResponseEntity<ArrayList<Horario>> getHorariosLivres()throws Exception{
     ArrayList<Horario> horarios;
     try{
       horarios = this.horarios.getHorarios(
         "idMedicoClinica = "+this.medicoClinica.getId()+" and "+
         "not (id in (select idHorario from Consulta_MF))"
       );
     }
     catch (Exception e){
   	   return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
     return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getHorariosLivresPorData/{data}",method=RequestMethod.GET)
   public ResponseEntity<ArrayList<Horario>> getHorariosLivres(@PathVariable(value="data") Date data)throws Exception{
     ArrayList<Horario> horarios;
     try{
       horarios = this.horarios.getHorarios(
         "idMedicoClinica = "+this.medicoClinica.getId()+" and "+
         "not (id in (select idHorario from Consulta_MF)) and "+
         "data = '"+data.toString()+"'"
       );
     }
     catch (Exception e){
   	   return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
     return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getHorariosLivresPorDia/{dia}",method=RequestMethod.GET)
   public ResponseEntity<ArrayList<Horario>> getHorariosLivres(@PathVariable(value="dia") String dia)throws Exception{
	 int numeroDia = Validacao.getNumeroDia(dia);
	 if (numeroDia == -1)
	   return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
	 ArrayList<Horario> horarios;
     try{
       horarios = this.horarios.getHorarios(
         "idMedicoClinica = "+this.medicoClinica.getId()+" and "+
         "not (id in (select idHorario from Consulta_MF)) and "+
         "extract(dow from data) = "+numeroDia
       );
     }
     catch (Exception e){
   	   return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
     return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getHorariosOcupados",method=RequestMethod.GET)
   public ResponseEntity<ArrayList<Horario>> getHorariosOcupados()throws Exception{
     ArrayList<Horario> horarios;
     try{
       horarios = this.horarios.getHorarios(
         "idMedicoClinica = "+this.medicoClinica.getId()+" and "+
         "id in (select idHorario from Consulta_MF)"
       );
     }
     catch (Exception e){
   	   return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
     return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getHorariosOcupadosPorData/{data}",method=RequestMethod.GET)
   public ResponseEntity<ArrayList<Horario>> getHorariosOcupados(@PathVariable(value="data")Date data)throws Exception{
     ArrayList<Horario> horarios;
     try{
       horarios = this.horarios.getHorarios(
         "idMedicoClinica = "+this.medicoClinica.getId()+" and "+
         "id in (select idHorario from Consulta_MF) and "+
         "data = '"+data.toString()+"'"
       );
     }
     catch (Exception e){
   	   return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
     return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getHorariosOcupadosPorDia/{dia}",method=RequestMethod.GET)
   public ResponseEntity<ArrayList<Horario>> getHorariosOcupados(@PathVariable(value="dia")String dia)throws Exception{
	 int numeroDia = Validacao.getNumeroDia(dia);
     if (numeroDia == -1)
	   return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
	 ArrayList<Horario> horarios;
     try{
       horarios = this.horarios.getHorarios(
         "idMedicoClinica = "+this.medicoClinica.getId()+" and "+
         "id in (select idHorario from Consulta_MF) and "+
         "extract(dow from data) = "+numeroDia
       );
     }
     catch (Exception e){
   	   return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
     return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/isHorarioOcupado/{idHorario}",method=RequestMethod.GET)
   public ResponseEntity<Boolean> isHorarioOcupado(@PathVariable(value="idHorario")int idHorario){
	 try{
       return new ResponseEntity<Boolean>(this.consultas.isConsultaExistentePorMedico(idHorario),HttpStatus.OK);
	 }
	 catch (Exception e){
	   return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
	 }
   }
   
   @CrossOrigin
   @RequestMapping(value="/getEspecialidades",method=RequestMethod.GET)
   public ResponseEntity<ArrayList<Especialidade>> getEspecialidades(){
     ArrayList<Especialidade> especialidades;
     try{
       especialidades = this.especialidades.getEspecialidades("id in (select idEspecialidade from MedicoEspecialidade_MF where idMedico = "+this.medico.getId()+")");
     }
     catch (Exception e){
	   return new ResponseEntity<ArrayList<Especialidade>>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
	 return new ResponseEntity<ArrayList<Especialidade>>(especialidades,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getHorarios",method = RequestMethod.GET)
   public ResponseEntity<ArrayList<Horario>> getHorarios(){
	   ArrayList<Horario> horarios;
	   try
	   {
		  horarios = this.horarios.getHorarios(
		    "idMedicoClinica in (select id from MedicoClinica_MF where "+
		    "idMedico = "+this.medico.getId()+" and idClinica = "+this.clinica.getId()+")"
		  );
	   }
	   catch(Exception e)
	   {
		  return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
	   }
	   return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getConvenios",method = RequestMethod.GET)
   public ResponseEntity<ArrayList<Convenio>> getConvenios(){
	  ArrayList<Convenio> convenios;
	  try{
	    convenios = this.convenios.getConvenios(
	      "id in (select idConvenio from ConvenioClinica_MF where idClinica = "+this.clinica.getId()+")"
	    );
	  }
	  catch (Exception e){
	    return new ResponseEntity<ArrayList<Convenio>>(HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	  return new ResponseEntity<ArrayList<Convenio>>(convenios,HttpStatus.OK);
   }
   
   @Autowired
   private HttpServletRequest request;
   
   @CrossOrigin
   @RequestMapping(value="/getRemoteAddr",method=RequestMethod.GET)
   public ResponseEntity<String> getRemoteAddr(){
     return new ResponseEntity<String>(request.getRemoteAddr(),HttpStatus.OK);
   }
   
   @CrossOrigin
   @RequestMapping(value="/getLocalAddr",method=RequestMethod.GET)
   public ResponseEntity<String> getLocalAddr(){
     return new ResponseEntity<String>(request.getLocalAddr(),HttpStatus.OK);
   }
}
