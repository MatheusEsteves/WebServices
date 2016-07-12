///PesquisarPS
///

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paciente;
import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.jws.WebParam;
import javax.websocket.server.PathParam;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

import classesBD.AdaptedPreparedStatement;
import classesDAO.Cidades;
import classesDAO.Clinicas;
import classesDAO.Consultas;
import classesDAO.ConvenioClinicas;
import classesDAO.ConvenioProntoSocorros;
import classesDAO.Convenios;
import classesDAO.Especialidades;
import classesDAO.Estados;
import classesDAO.Horarios;
import classesDAO.MedicoEspecialidades;
import classesDAO.MedicoClinicas;
import classesDAO.MedicoProntoSocorros;
import classesDAO.Medicos;
import classesDAO.Pacientes;
import classesDAO.ProntoSocorros;
import classesDBO.Cidade;
import classesDBO.Clinica;
import classesDBO.Consulta;
import classesDBO.Convenio;
import classesDBO.ConvenioClinica;
import classesDBO.ConvenioProntoSocorro;
import classesDBO.Especialidade;
import classesDBO.Estado;
import classesDBO.Horario;
import classesDBO.LoginPaciente;
import classesDBO.Medico;
import classesDBO.MedicoClinica;
import classesDBO.MedicoProntoSocorro;
import classesDBO.Paciente;
import classesDBO.ProntoSocorro;

/**
 * Web Service para uso restrito do paciente no sistema, seja em qualquer plataforma.
 * Nele, estão os métodos necessários para o paciente tratar os seus dados e de suas 
 * consultas, além de poder visualizar os mesmos.
 * @author medico_facil
 */
@RestController
public class PacienteController {
    private AdaptedPreparedStatement banco;
    private Pacientes pacientes;
    /**
     * O atributo abaixo é um objeto Paciente que guarda os dados do paciente logado no
     * momento. Será utilizado por métodos que dependam diretamente de qual paciente 
     * está logado no sistema.
     */
    private Paciente paciente;
    private Consultas consultas;
    private Clinicas clinicas;
    private Medicos medicos;
    private MedicoClinicas medicoClinicas;
    private MedicoProntoSocorros medicoProntoSocorros;
    private ProntoSocorros prontoSocorros;
    private Convenios convenios;
    private ConvenioClinicas convenioClinicas;
    private ConvenioProntoSocorros convenioProntoSocorros;
    private Especialidades especialidades;
    private Estados estados;
    private Cidades cidades;
    private Horarios horarios;
    private MedicoEspecialidades medicoEspecialidades;
    
    /**
     * Método que retorna os dados do paciente logado em um objeto Paciente.
     * @return Paciente
     */
    @CrossOrigin
    @RequestMapping(value="getPaciente",method=RequestMethod.GET)
    public ResponseEntity<Paciente> getPaciente(){
      return new ResponseEntity<Paciente>(this.paciente,HttpStatus.OK);
    }
    
    /**
     * Método de inclusão de paciente no banco de dados
     * @return Boolean : Retorna true caso o paciente seja incluído com sucesso e false caso haja dados já existentes
     * @param Paciente : Objeto com os dados do paciente a ser incluído.
     */
    @CrossOrigin 
    @RequestMapping(value="incluirPaciente",method = RequestMethod.PUT)
    public ResponseEntity<Boolean> incluirPaciente(@RequestBody(required=true) Paciente paciente) throws Exception {
    	try{
          this.pacientes.incluir(paciente);
        }
        catch (Exception e){
          return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);	
        }
    	return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    /**
     * Metodo para conecção com o banco e inicialização das DAOs que serão utilizadas
     * @return Boolean : Retorna true caso a conexão seja realizada com sucesso e false caso contrário.
     */
    @CrossOrigin
    @RequestMapping(value = "/conectarBd",method = RequestMethod.GET)
    public ResponseEntity<Boolean> conectarBd() throws Exception {
        try{
            this.banco = new AdaptedPreparedStatement(
              "org.postgresql.Driver",
              "jdbc:postgresql://pellefant-02.db.elephantsql.com:5432/pdenoczt", 
              "pdenoczt", 
              "5u3vh6p8J3bRCO9ZbH_lh7iN3VRoeJOn" 
            );
            this.cidades        = new Cidades(this.banco);
            this.estados        = new Estados(this.banco);
            this.especialidades = new Especialidades(this.banco);
            this.pacientes      = new Pacientes(this.banco,this.cidades,this.estados);
            this.medicos        = new Medicos(this.banco,this.estados);
            this.clinicas       = new Clinicas(this.banco,this.cidades,this.estados);
            this.medicoClinicas = new MedicoClinicas(this.banco,this.medicos,this.clinicas);
            this.horarios       = new Horarios(this.banco,this.medicos,this.clinicas,this.medicoClinicas);
            this.consultas      = new Consultas(this.banco,this.medicoClinicas,this.pacientes,this.horarios);
            this.prontoSocorros = new ProntoSocorros(this.banco,this.cidades,this.estados);
            this.medicoProntoSocorros = new MedicoProntoSocorros(this.banco,this.medicos,this.prontoSocorros);
            this.medicoEspecialidades = new MedicoEspecialidades(this.banco);
            this.convenios      = new Convenios(this.banco);
            this.convenioClinicas       = new ConvenioClinicas(this.banco);
            this.convenioProntoSocorros = new ConvenioProntoSocorros(this.banco);
        }catch(Exception ex)
        {
            return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }
    
    /**
     * Método para desconectar com o banco de dados
     * @return Boolean : Retorna true caso o banco de dados seja desconectado com sucesso e false caso não haja conexão aberta para desconectar.
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/desconectarBd",method = RequestMethod.GET)
    public ResponseEntity<Boolean> desconectarBd() throws Exception {
       try{
    	  this.banco.close();
       }
       catch (Exception e){
    	  return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
       }
       return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    /**
     * Método de alteração de paciente no banco de dados.
     * @return Boolean : Retorna true caso o paciente seja alterado com sucesso e false caso haja dados já existentes.
     * @param Paciente : Objeto com os dados do paciente a ser alterado
     */
    @CrossOrigin
    @RequestMapping(value = "/alterarPaciente",method = RequestMethod.POST)
    public ResponseEntity<Boolean> alterarPaciente(@RequestBody(required=true) Paciente paciente) {
        try{
        	paciente.setCpf(this.paciente.getCpf());
        	this.pacientes.alterar(paciente);
        }
        catch (Exception e){
        	return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    /**
     * Método que retorna todas as consultas do paciente logado.
     * @return ArrayList<Consulta>
     */  
    @CrossOrigin
    @RequestMapping(value = "/getConsultas",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Consulta>> getConsultas() throws Exception {
        ArrayList<Consulta> consultas = null;
    	try{
          consultas = this.consultas.getConsultas("idPaciente = "+this.paciente.getId());
        }
        catch (Exception e){
          return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
    }

    /**
     * Método para agendar uma consulta do paciente logado no banco de dados.
     * @return Boolean : Retorna true caso a consulta seja agendada com sucesso e false caso essa consulta já exista.
     * @param Consulta : Objeto com os dados da consulta a ser agendada.
     */
    @CrossOrigin
    @RequestMapping(value = "/agendarConsulta",method = RequestMethod.PUT)
    public ResponseEntity<Boolean> agendarConsulta(@RequestBody(required=true) Consulta consulta) throws Exception {
    	try{
    	  consulta.setPaciente(this.paciente);
          this.consultas.incluir(consulta);
        }
        catch (Exception e){
          return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }
    
    /**
     * Método para alterar os dados de uma consulta do paciente logado no banco de dados.
     * @return Boolean : Retorna true caso a consulta seja alterada com sucesso e false caso a nova consulta já exista.
     * @param Consulta : Objeto com os novos dados da consulta a ser alterada.
     */
    @CrossOrigin
    @RequestMapping(value = "/alterarConsulta",method = RequestMethod.POST)
    public ResponseEntity<Boolean> alterarConsulta(@RequestBody(required=true) Consulta consulta) throws Exception {
        try{
          consulta.setPaciente(paciente);
          this.consultas.alterar(consulta);
        }
        catch (Exception e){
          return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }
    
    /**
     * Método para cancelar uma consulta do paciente logado no banco de dados.
     * @return Boolean : Retorna true caso a consulta seja cancelada com sucesso e false caso contrário
     * @param int : Id da consulta a ser cancelada
     */
    @CrossOrigin
    @RequestMapping(value = "/cancelarConsulta/{idConsulta}",method={RequestMethod.DELETE,RequestMethod.GET})
    public ResponseEntity<Boolean> cancelarConsulta(@PathVariable(value="idConsulta") int idConsulta) throws Exception{
      try{
    	 this.consultas.excluir(idConsulta);
      }
      catch (Exception e){
    	  return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }
    
     /**
     * Método que retorna pronto socorros por parte do seu nome
     * @return  ArrayList<ProntoSocorro>, ordenados pela distância do local até o paciente.
     * @param String nome - Parte do nome do pronto socorro
     * @param float latitude - Latitude da localização do paciente
     * @param float longitude - Longitude da localização do paciente
     */
    @CrossOrigin
    @RequestMapping(value = "/getProntoSocorrosPorNome/{nome}/{latitude}/{longitude}",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<ProntoSocorro>> getProntoSocorrosPorNome(
    		                                          @PathVariable(value="nome")String nome,
    		                                          @PathVariable(value="latitude")float latitude,
    		                                          @PathVariable(value="longitude")float longitude
    		                                        ) throws Exception {
        ArrayList<ProntoSocorro> prontoSocorros = null;
    	try{
           prontoSocorros = this.prontoSocorros.getProntoSocorros("nome like '%"+nome+"%'",latitude,longitude);
    	}
    	catch (Exception e){
    	   return new ResponseEntity<ArrayList<ProntoSocorro>>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<ArrayList<ProntoSocorro>>(prontoSocorros,HttpStatus.OK);
    }
    
    /**
     * Método que retorna clínicas por parte do seu nome.
     * @return  ArrayList<Clinica>, ordenadas pela distância do local até o paciente.
     * @param String nome - Parte do nome da clínica
     * @param float latitude - Latitude da localização do paciente
     * @param float longitude - Longitude da localização do paciente
     */
    @CrossOrigin
    @RequestMapping(value = "/getClinicasPorNome/{nome}/{latitude}/{longitude}",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Clinica>> getClinicasPorNome(
    		                                    @PathVariable(value="nome")String nome,
    		                                    @PathVariable(value="latitude")float latitude,
    		                                    @PathVariable(value="longitude")float longitude
    		                                  )throws Exception {
       ArrayList<Clinica> clinicas;
       try{
    	  clinicas = this.clinicas.getClinicas("nome like '%"+nome+"%'",latitude,longitude);
       }
       catch (Exception e){
    	   return new ResponseEntity<ArrayList<Clinica>>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
       return new ResponseEntity<ArrayList<Clinica>>(clinicas,HttpStatus.OK);
    }
    
    /**
     * Método que retorna clínicas por uma determinada cidade.
     * @return  ArrayList<Clinica>, ordenadas pela distância do local até o paciente.
     * @param String cidade - nome da cidade.
     * @param String uf - sigla do estado no qual a cidade se localiza
     * @param float latitude - Latitude da localização do paciente
     * @param float longitude - Longitude da localização do paciente
     */
    @CrossOrigin
    @RequestMapping(value = "/getClinicasPorCidade/{cidade}/{uf}/{latitude}/{longitude}",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Clinica>> getClinicasPorCidade(
    		                                    @PathVariable(value="cidade")String cidade,
    		                                    @PathVariable(value="uf")String uf,
    		                                    @PathVariable(value="latitude")float latitude,
    		                                    @PathVariable(value="longitude")float longitude
    		                                  )throws Exception{
    	ArrayList<Clinica> lista;
        try{
     	  lista = this.clinicas.getClinicas("idCidade = "+this.cidades.getId(cidade,uf),latitude,longitude);
        }
        catch (Exception e){
     	  return new ResponseEntity<ArrayList<Clinica>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ArrayList<Clinica>>(lista,HttpStatus.OK);
    }
    
    
    /**
     * Metodo que retorna pronto socorros por uma determinada cidade.
     * @return - ArrayList<ProntoSocorro>, ordenados pela distância do paciente até o local.
     * @param String cidade - nome da cidade
     * @param String uf - sigla do estado no qual a cidade se localiza
     * @param float latitude - Latitude da localização do paciente
     * @param float longitude - Longitude da localização do paciente
     */
    @CrossOrigin
    @RequestMapping(value = "/getProntoSocorrosPorCidade/{cidade}/{uf}/{latitude}/{longitude}",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<ProntoSocorro>> getProntoSocorrosPorCidade(
    		                                          @PathVariable(value="cidade") String cidade,
    		                                          @PathVariable(value="uf") String uf,
    		                                          @PathVariable(value="latitude")float latitude,
    	    		                                  @PathVariable(value="longitude")float longitude
    		                                        )throws Exception {
        ArrayList<ProntoSocorro> lista;
        try{
           lista = this.prontoSocorros.getProntoSocorros("idCidade = "+this.cidades.getId(cidade,uf),latitude,longitude);
        }
        catch (Exception e){
           return new ResponseEntity<ArrayList<ProntoSocorro>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ArrayList<ProntoSocorro>>(lista,HttpStatus.OK);
    }
    
    /**
     * Método que retorna clínicas por um determinado convênio médico
     * @return ArrayList<Clinica>, ordenadas pela distância do paciente até o local.
     * @param String convenio - Nome do convênio médico
     * @param float latitude - Latitude da localização do paciente
     * @param float longitude - Longitude da localização do paciente
     */
    @CrossOrigin
    @RequestMapping(value = "/getClinicasPorConvenio/{convenio}/{latitude}/{longitude}",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Clinica>> getClinicasPorConvenio(
    		                                    @PathVariable(value = "convenio") String convenio,
    		                                    @PathVariable(value="latitude")float latitude,
    		                                    @PathVariable(value="longitude")float longitude
    		                                  ) throws Exception {
        ArrayList<Clinica> lista;
        try{
          lista = this.clinicas.getClinicas(
            "id in ("+
              "select idClinica from ConvenioClinica_MF where idConvenio = "+this.convenios.getId(convenio)+
            ")",latitude,longitude
          );
        }
        catch (Exception e){
        	return new ResponseEntity<ArrayList<Clinica>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ArrayList<Clinica>>(lista,HttpStatus.OK);
    }
    
    /**
     * Método que retorna pronto socorros por um determinado convênio médico
     * @param String convenio : nome do convênio médico
     * @param float latitude : Latitude da localização do paciente
     * @param float longitude : Longitude da localização do paciente
     * @return ArrayList<ProntoSocorro>, ordenados pela distância do paciente até o local
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/getProntoSocorrosPorConvenio/{convenio}/{latitude}/{longitude}",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<ProntoSocorro>> getProntoSocorrosPorConvenio(
    		                                          @PathVariable(value = "convenio") String convenio,
    		                                          @PathVariable(value="latitude")float latitude,
    	    		                                  @PathVariable(value="longitude")float longitude
    	    		                                ) throws Exception {
        ArrayList<ProntoSocorro> lista;
        try{
          lista = this.prontoSocorros.getProntoSocorros(
            "id in ("+
              "select idPs from ConvenioPs_MF where idConvenio = "+this.convenios.getId(convenio)+
            ")",latitude,longitude
          );
        }
        catch (Exception e){
        	return new ResponseEntity<ArrayList<ProntoSocorro>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ArrayList<ProntoSocorro>>(lista,HttpStatus.OK);
    }
    
    /**
     * Método que retorna todos os pronto socorros cadastrados no sistema.
     * @param float latitude : Latitude da localização do paciente
     * @param float longitude : Longitude da localização do paciente
     * @return ArrayList<ProntoSocorro>, ordenados pela distância do paciente até o local
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/getProntoSocorros/{latitude}/{longitude}",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<ProntoSocorro>> getProntoSocorros(
    		                                          @PathVariable(value="latitude")float latitude,
    		                                          @PathVariable(value="longitude")float longitude
    		                                        ) throws Exception{
    	ArrayList<ProntoSocorro> lista;
    	try{
    	  lista = this.prontoSocorros.getProntoSocorros(latitude,longitude);
    	}
    	catch (Exception e){
        	return new ResponseEntity<ArrayList<ProntoSocorro>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ArrayList<ProntoSocorro>>(lista,HttpStatus.OK);
    }
    
    /**
     * Método que retorna todas as clínicas cadastradas no sistema
     * @param float latitude : Latitude da localização do paciente
     * @param float longitude : Longitude da localização do paciente
     * @return ArrayList<Clinica>, ordenadas pela distância do paciente até o local
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/getClinicas/{latitude}/{longitude}",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Clinica>> getClinicas(
    		                                          @PathVariable(value="latitude")float latitude,
    		                                          @PathVariable(value="longitude")float longitude
    		                                        ) throws Exception{
    	ArrayList<Clinica> lista;
    	try{
    	  lista = this.clinicas.getClinicas(latitude,longitude);
    	}
    	catch (Exception e){
        	return new ResponseEntity<ArrayList<Clinica>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ArrayList<Clinica>>(lista,HttpStatus.OK);
    }

    /**
     * Método que realiza o login do paciente no sistema e caso seja realizado com sucesso, armazena os dados 
     * do paciente logado em um objeto Paciente.
     * @return Boolean : Retorna true caso cpf e senha coincidirem no banco de dados (usuário existente) e false caso contrário
     * @param LoginPaciente : Objeto que contém como atributos o cpf(String) e a senha(String) do paciente.
     */
    @CrossOrigin
    @RequestMapping(value = "/loginPaciente",method = RequestMethod.POST)
    public ResponseEntity<Boolean> loginPaciente(@RequestBody(required=true) LoginPaciente loginPaciente) throws Exception {
    	ArrayList<Paciente> pacientes = null;
    	try{  		
          pacientes = this.pacientes.getPacientes("cpf = '"+loginPaciente.getCpf()+"' and senha = '"+loginPaciente.getSenha()+"'"); 
        }
        catch (Exception e){
          return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    	if (pacientes.isEmpty())
          return new ResponseEntity<Boolean>(false,HttpStatus.OK);
        this.paciente = pacientes.get(0);
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    /**
     * Método que retorna todas as especialidades de uma determinada clínica.
     * @return ArrayList<Especialidade>
     * @param String clinica : nome da clínica.
     */
    @CrossOrigin
    @RequestMapping(value = "/getEspecialidadesPorClinica/{clinica}",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Especialidade>> getEspecialidadesPorClinica(@PathVariable(value="clinica")String clinica) throws Exception{
        ArrayList<Especialidade> especialidades;
        try{
          especialidades = this.especialidades.getEspecialidades(
            "id in (select idEspecialidade from MedicoEspecialidade_MF where idMedico in ("+
            "select idMedico from MedicoClinica_MF where idClinica = "+this.clinicas.getId(clinica)+"))"
          );
        }
        catch (Exception e){
          return new ResponseEntity<ArrayList<Especialidade>>(HttpStatus.OK);
        }
        return new ResponseEntity<ArrayList<Especialidade>>(especialidades,HttpStatus.OK);
    }
    
    /**
     * Método que retorna todas as especialidades de um determinado pronto socorro.
     * @return  ArrayList<Especialidade>
     * @param String ps : nome do pronto socorro
     */
    @CrossOrigin
    @RequestMapping(value = "/getEspecialidadesPorPs/{ps}",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Especialidade>> getEspecialidadesPorPs(@PathVariable(value="ps") String ps) throws Exception{
        ArrayList<Especialidade> especialidades;
        try{
          especialidades = this.especialidades.getEspecialidades(
            "id in (select idEspecialidade from MedicoEspecialidade_MF where idMedico in ("+
            "select idMedico from MedicoPs_MF where idPs = "+this.prontoSocorros.getId(ps)+"))"
          );
        }
        catch (Exception e){
          return new ResponseEntity<ArrayList<Especialidade>>(HttpStatus.OK);
        }
        return new ResponseEntity<ArrayList<Especialidade>>(especialidades,HttpStatus.OK);
    }

    /**
     * Método que retorna os médicos de uma determinada clínica que trabalham em uma determinada especialidade médica.
     * @return  ArrayList<Medico>
     * @param String especialidade : Nome da especialidade médica
     * @param String clinica : Nome da clínica
     */
    @CrossOrigin
    @RequestMapping(value = "/getMedicosPorEspecialidadeClinica/{especialidade}/{clinica}",method=RequestMethod.GET)
    public ResponseEntity<ArrayList<Medico>> getMedicosPorEspecialidadeClinica(@PathVariable(value = "especialidade")String especialidade, @PathVariable(value = "clinica")String clinica) throws Exception {
        ArrayList<Medico> lista;
    	try
        {
            lista = this.medicos.getMedicos(
               "id in (select idMedico from MedicoClinica_MF " +
               "where idClinica = "+this.clinicas.getId(clinica)+" and "+
               "idMedico in (select idMedico from MedicoEspecialidade_MF "+
               "where idEspecialidade = "+this.especialidades.getId(especialidade)+"))"
            ); 
   
        }catch(Exception e)
        {
           return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    	return new ResponseEntity<ArrayList<Medico>>(lista,HttpStatus.OK);
    }
     
    /**
     * Método que retorna os médicos de um determinado pronto socorro que trabalham em uma determinada especialidade médica
     * @return  ArrayList<Medico>
     * @param String especialidade : Nome da especialidade médica
     * @param String ps : Nome do pronto socorro
     */
    @CrossOrigin
    @RequestMapping(value = "/getMedicosPorEspecialidadePs/{especialidade}/{ps}")
    public ResponseEntity<ArrayList<Medico>> getMedicosPorEspecialidadePs(@PathVariable(value = "especialidade")String especialidade, @PathVariable(value = "ps")String ps)throws Exception {
       ArrayList<Medico> lista;
       try{
         lista = this.medicos.getMedicos(
        	"id in (select idMedico from MedicoPs_MF " +
        	"where idPs = "+this.prontoSocorros.getId(ps)+" and "+
        	"idMedico in (select idMedico from MedicoEspecialidade_MF "+
        	"where idEspecialidade = "+this.especialidades.getId(especialidade)+"))"	   
         ); 
       }
       catch(Exception e){
    	   return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
       return new ResponseEntity<ArrayList<Medico>>(lista,HttpStatus.OK);
    }
    
    
    /**
     * Método que retorna os médicos de um determinado pronto socorro que contenham a String especificada em seu nome
     * @return  ArrayList<Medico>
     * @param String nome : Parte do nome do médico
     * @param String ps : Nome do pronto socorro
     */
    @CrossOrigin
    @RequestMapping(value = "/getMedicosPorNomePs/{nome}/{ps}")
    public ResponseEntity<ArrayList<Medico>> getMedicosPorNomePs(@PathVariable(value = "nome") String nome, @PathVariable(value = "ps")String ps)throws Exception {
       ArrayList<Medico> lista;
       try{
          lista = this.medicos.getMedicos(
        		     "nome like '%" + nome + "%' and "+
                     "id in (select idMedico from MedicoPs_MF " +
                     "where idPs = "+this.prontoSocorros.getId(ps)+" and presente = 'S')"
                  ); 
       }
       catch(Exception e){
    	   return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
       return new ResponseEntity<ArrayList<Medico>>(lista,HttpStatus.OK);
    }
    
    
    /**
     * Método que retorna os médicos de uma determinada clínica que contenham a String especificada em seu nome
     * @return  ArrayList<Medico>
     * @param String nome : Parte do nome do médico
     * @param String clinica : Nome da clínica
     */
    @CrossOrigin
    @RequestMapping(value = "/getMedicosPorNomeClinica/{nome}/{clinica}")
    public ResponseEntity<ArrayList<Medico>> getMedicosPorNomeClinica(@PathVariable(value = "nome") String nome, @PathVariable(value = "clinica")String clinica) throws Exception {
      ArrayList<Medico> lista;
      try{
        lista  = this.medicos.getMedicos(
        		    "nome like '%" + nome + "%' and "+
                    "id in (select idMedico from MedicoClinica_MF "+
                    "where idClinica = "+this.clinicas.getId(clinica)+")"
                 ); 
      }
      catch(Exception e){
    	  return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<ArrayList<Medico>>(lista,HttpStatus.OK);
    }
    
    /**
     * Método que relaciona uma determinada clínica à um determinado convênio no banco de dados.
     * @param String convenio : Nome do convênio médico.
     * @param String clinica : Nome da clínica
     * @return Boolean : Retorna true caso a relação seja incluída com sucesso e false caso já exista no banco de dados
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value="/incluirConvenioClinica/{convenio}/{clinica}",method={RequestMethod.PUT,RequestMethod.GET})
    public ResponseEntity<Boolean> incluirConvenioClinica(@PathVariable(value="convenio")String convenio,@PathVariable(value="clinica")String clinica)throws Exception{
      try{
        this.convenioClinicas.incluir(
          new ConvenioClinica(
            0,
            this.convenios.getId(convenio),
            this.clinicas.getId(clinica)
          )
        );
      }
      catch (Exception e){
        return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }
    
    /**
     * Método que relaciona um determinado pronto socorro à um determinado convênio no banco de dados
     * @param String convenio : Nome do convênio médico
     * @param String ps : Nome do pronto socorro
     * @return Boolean : Retorna true caso a relação seja incluída com sucesso e false caso já exista no banco de dados
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value="/incluirConvenioProntoSocorro/{convenio}/{ps}",method={RequestMethod.PUT,RequestMethod.GET})
    public ResponseEntity<Boolean> incluirConvenioProntoSocorro(@PathVariable(value="convenio")String convenio,@PathVariable(value="ps")String ps)throws Exception{
      try{
        this.convenioProntoSocorros.incluir(
          new ConvenioProntoSocorro(
            0,
            this.convenios.getId(convenio),
            this.prontoSocorros.getId(ps)
          )
        );
      }
      catch (Exception e){
        return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }
    
    /**
     * Método para excluir uma relação entre um determinado convênio e uma determinada clínica
     * @param String convenio : Nome do convênio médico
     * @param String clinica : Nome da clínica
     * @return Boolean : Retorna true caso a relação seja excluída com sucesso e false caso contrário.
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value="/excluirConvenioClinica/{convenio}/{clinica}",method={RequestMethod.DELETE,RequestMethod.GET})
    public ResponseEntity<Boolean> excluirConvenioClinica(@PathVariable(value="convenio")String convenio,@PathVariable(value="clinica")String clinica)throws Exception{
      try{
        this.convenioClinicas.excluir(
          this.convenioClinicas.getId(
            this.convenios.getId(convenio),
            this.clinicas.getId(clinica)
          )
        );
      }
      catch (Exception e){
    	return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }
    
    /**
     * Método para excluir uma relação entre um determinado convênio e um determinado pronto socorro
     * @param String convenio : Nome do convênio médico
     * @param String ps : Nome do pronto socorro
     * @return Boolean : Retorna true caso a relação seja excluída com sucesso e false caso contrário
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value="/excluirConvenioProntoSocorro/{convenio}/{ps}",method={RequestMethod.DELETE,RequestMethod.GET})
    public ResponseEntity<Boolean> excluirConvenioProntoSocorro(@PathVariable(value="convenio")String convenio,@PathVariable(value="ps")String ps)throws Exception{
      try{
        this.convenioProntoSocorros.excluir(
          this.convenioProntoSocorros.getId(
            this.convenios.getId(convenio),
            this.prontoSocorros.getId(ps)
          )
        );
      }
      catch (Exception e){
    	return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }
    
    @CrossOrigin
    @RequestMapping(value="/getHorarios/{idMedico}",method=RequestMethod.GET)
    public ResponseEntity<ArrayList<Horario>> getHorarios(@PathVariable(value="idMedico")int idMedico)throws Exception{
      ArrayList<Horario> horarios;
      try{
        horarios = this.horarios.getHorarios("idMedicoClinica in (select id from MedicoClinica_MF where idMedico = "+idMedico+")");
      }
      catch (Exception e){
    	return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
    }
    
    @CrossOrigin
    @RequestMapping(value="/getHorariosLivres/{idMedico}",method=RequestMethod.GET)
    public ResponseEntity<ArrayList<Horario>> getHorariosLivres(@PathVariable(value="idMedico")int idMedico)throws Exception{
      ArrayList<Horario> horarios;
      try{
        horarios = this.horarios.getHorarios(
          "idMedicoClinica in (select id from MedicoClinica_MF where idMedico = "+idMedico+") and "+
          "not (id in (select idHorario from Consulta_MF))"
        );
      }
      catch (Exception e){
    	return new ResponseEntity<ArrayList<Horario>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<ArrayList<Horario>>(horarios,HttpStatus.OK);
    }
    
    @CrossOrigin
    @RequestMapping(value="/getMedicosPlantao/{idPs}",method=RequestMethod.GET)
    public ResponseEntity<ArrayList<Medico>> getMedicosPlantao(@PathVariable(value="idPs")int idPs)throws Exception{
      ArrayList<Medico> medicos;
      try{
        medicos = this.medicos.getMedicos(
          "id in (select idMedico from MedicoPs_MF where idPs = "+idPs+" and presente = 'S')"
        );
      }
      catch (Exception e){
    	return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
    }
    
    @CrossOrigin
    @RequestMapping(value="/getMedicosPorPs/{idPs}",method=RequestMethod.GET)
    public ResponseEntity<ArrayList<Medico>> getMedicosPorPs(@PathVariable(value="idPs")int idPs)throws Exception{
      ArrayList<Medico> medicos;
      try{
        medicos = this.medicos.getMedicos(
          "id in (select idMedico from MedicoPs_MF where idPs = "+idPs+")"
        );
      }
      catch (Exception e){
    	return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
    }
    
    @CrossOrigin
    @RequestMapping(value="/getMedicosPorClinica/{idClinica}",method=RequestMethod.GET)
    public ResponseEntity<ArrayList<Medico>> getMedicosPorClinica(@PathVariable(value="idClinica")int idClinica)throws Exception{
      ArrayList<Medico> medicos;
      try{
        medicos = this.medicos.getMedicos(
          "id in (select idMedico from MedicoClinica_MF where idClinica = "+idClinica+")"
        );
      }
      catch (Exception e){
    	return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
    }
    
    @CrossOrigin
    @RequestMapping(value="/getMedicosPorNomeIdClinica/{nomeMedico}/{idClinica}",method=RequestMethod.GET)
    public ResponseEntity<ArrayList<Medico>> getMedicosPorNomeClinica(
    		                                   @PathVariable(value="nomeMedico")String nomeMedico,
    		                                   @PathVariable(value="idClinica")int idClinica)throws Exception{
      ArrayList<Medico> medicos;
      try{
        medicos = this.medicos.getMedicos(
          "nome like '%"+nomeMedico+"%' and "+
          "id in (select idMedico from MedicoClinica_MF where idClinica = "+idClinica+")"
        );
      }
      catch (Exception e){
    	return new ResponseEntity<ArrayList<Medico>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<ArrayList<Medico>>(medicos,HttpStatus.OK);
    }
    
    @CrossOrigin
    @RequestMapping(value="/getConsultasPorMedico/{nomeMedico}",method=RequestMethod.GET)
    public ResponseEntity<ArrayList<Consulta>> getConsultasPorMedico(@PathVariable(value="nomeMedico")String nomeMedico)throws Exception{
      ArrayList<Consulta> consultas;
      try{
        consultas = this.consultas.getConsultas(
          "idPaciente = "+this.paciente.getId()+" and "+
          "idHorario in (select id from Horario_MF where "+
          "idMedicoClinica in (select id from MedicoClinica_MF where "+
          "idMedico in (select id from Medico_MF where nome like '%"+nomeMedico+"%')))"	
        );
      }
      catch (Exception e){
    	return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
    }
    
    @CrossOrigin
    @RequestMapping(value="/getConsultasPorClinica/{nomeClinica}",method=RequestMethod.GET)
    public ResponseEntity<ArrayList<Consulta>> getConsultasPorClinica(@PathVariable(value="nomeClinica")String nomeClinica)throws Exception{
      ArrayList<Consulta> consultas;
      try{
        consultas = this.consultas.getConsultas(
          "idPaciente = "+this.paciente.getId()+" and "+
          "idHorario in (select id from Horario_MF where "+
          "idMedicoClinica in (select id from MedicoClinica_MF where "+
          "idClinica in (select id from Clinica_MF where nome like '%"+nomeClinica+"%')))"	
        );
      }
      catch (Exception e){
    	return new ResponseEntity<ArrayList<Consulta>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<ArrayList<Consulta>>(consultas,HttpStatus.OK);
    }
    
    @CrossOrigin
    @RequestMapping(value="/getMedicoClinicas/{idClinica}",method=RequestMethod.GET)
    public ResponseEntity<ArrayList<MedicoClinica>> getMedicoClinicas(@PathVariable(value="idClinica")int idClinica){
      ArrayList<MedicoClinica> lista;
      try{
    	lista = this.medicoClinicas.getMedicoClinicas("idClinica = "+idClinica);
      }
      catch (Exception e){
    	return new ResponseEntity<ArrayList<MedicoClinica>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<ArrayList<MedicoClinica>>(lista,HttpStatus.OK);
    }
    
    @CrossOrigin
    @RequestMapping(value="/getMedicoProntoSocorros/{idPs}",method=RequestMethod.GET)
    public ResponseEntity<ArrayList<MedicoProntoSocorro>> getMedicoProntoSocorros(@PathVariable(value="idPs")int idPs){
      ArrayList<MedicoProntoSocorro> lista;
      try{
    	lista = this.medicoProntoSocorros.getMedicoProntoSocorros("idPs = "+idPs);
      }
      catch (Exception e){
    	return new ResponseEntity<ArrayList<MedicoProntoSocorro>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<ArrayList<MedicoProntoSocorro>>(lista,HttpStatus.OK);
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
     * Método que retorna o id de um convênio médico com nome especificado
     * @param String nome : Nome do convênio médico
     * @return Integer : Id do convênio médico
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value="/getIdConvenio/{nome}",method=RequestMethod.GET)
    public ResponseEntity<Integer> getIdConvenio(
    		                         @PathVariable(value="nome")String nome
    		                       )throws Exception{
      int idConvenio;
      try{
        idConvenio = this.convenios.getId(nome);
      }
      catch (Exception e){
        return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<Integer>(idConvenio,HttpStatus.OK);
    }
    
    /**
     * Método que retorna um objeto de um convênio médico com id especificado
     * @param int id : Id do convênio médico
     * @return Convenio : Objeto de convênio médico
     */
    @CrossOrigin
    @RequestMapping(value="/getConvenio/{id}",method=RequestMethod.GET)
    public ResponseEntity<Convenio> getConvenio(@PathVariable(value="id")int id){
      Convenio convenio;
      try{
    	convenio = this.convenios.getConvenios("id = "+id).get(0);
      }
      catch (Exception e){
    	return new ResponseEntity<Convenio>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<Convenio>(convenio,HttpStatus.OK);
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
    
    /**
     * Retorna o id de um horário, através dos dados informados no parâmetro
     * @param idMedico 
     * @param idClinica
     * @param diaSemana
     * @param horaInicio
     * @param horaFim
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value="/getIdHorario/{idMedico}/{idClinica}/{data}/{horaInicio}/{horaFim}",method=RequestMethod.GET)
    public ResponseEntity<Integer> getIdHorario(
    		                         @PathVariable(value="idMedico")int idMedico,
    		                         @PathVariable(value="idClinica")int idClinica,
    		                         @PathVariable(value="data")Date data,
    		                         @PathVariable(value="horaInicio")Time horaInicio,
    		                         @PathVariable(value="horaFim")Time horaFim
    		                       )throws Exception{
      int idHorario;
      try{
        idHorario = this.horarios.getId(idMedico,idClinica,data,horaInicio,horaFim);
      }
      catch (Exception e){
        return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<Integer>(idHorario,HttpStatus.OK);
    }
    
    /**
     * Retorna um horário com base no seu id passado como parâmetro.
     * @param id
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value="/getHorario/{id}",method=RequestMethod.GET)
    public ResponseEntity<Horario> getHorario(@PathVariable(value="id")int id)throws Exception{
      Horario horario;
      try{
    	horario = this.horarios.getHorarios("id = "+id).get(0);
      }
      catch (Exception e){
        return new ResponseEntity<Horario>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<Horario>(horario,HttpStatus.OK);
    }
    
    /**
     * Retorna o id da relação entre médico e clínica, através dos dados passados como parâmetro.
     * @param idMedico
     * @param idClinica
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value="/getIdMedicoClinica/{idMedico}/{idClinica}",method=RequestMethod.GET)
    public ResponseEntity<Integer> getIdMedicoClinica(
    		                         @PathVariable(value="idMedico")int idMedico,
    		                         @PathVariable(value="idClinica")int idClinica
    		                       )throws Exception{
      int idMedicoClinica;
      try{
        idMedicoClinica = this.medicoClinicas.getId(idMedico,idClinica);
      }
      catch (Exception e){
        return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<Integer>(idMedicoClinica,HttpStatus.OK);
    }
    
    /**
     * Retorna uma relação entre médico e clínica com base no seu id passado como parâmetro.
     * @param id
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value="/getMedicoClinica/{id}",method=RequestMethod.GET)
    public ResponseEntity<MedicoClinica> getMedicoClinica(@PathVariable(value="id")int id)throws Exception{
      MedicoClinica medicoClinica;
      try{
    	medicoClinica = this.medicoClinicas.getMedicoClinicas("id = "+id).get(0);
      }
      catch (Exception e){
        return new ResponseEntity<MedicoClinica>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<MedicoClinica>(medicoClinica,HttpStatus.OK);
    }
    
    /**
     * Retorna o id da relação entre médico e pronto socorro, através dos dados passados como parâmetro.
     * @param idMedico
     * @param idPs
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value="/getIdMedicoPs/{idMedico}/{idPs}",method=RequestMethod.GET)
    public ResponseEntity<Integer> getIdMedicoPs(
    		                         @PathVariable(value="idMedico")int idMedico,
    		                         @PathVariable(value="idPs")int idPs
    		                       )throws Exception{
      int idMedicoPs;
      try{
        idMedicoPs = this.medicoProntoSocorros.getId(idMedico,idPs);
      }
      catch (Exception e){
        return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<Integer>(idMedicoPs,HttpStatus.OK);
    }
    
    /**
     * Retorna uma relação entre médico e pronto socorro com base no seu id passado como parâmetro.
     * @param id
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value="/getMedicoPs/{id}",method=RequestMethod.GET)
    public ResponseEntity<MedicoProntoSocorro> getMedicoProntoSocorro(@PathVariable(value="id")int id)throws Exception{
      MedicoProntoSocorro medicoProntoSocorro;
      try{
    	medicoProntoSocorro = this.medicoProntoSocorros.getMedicoProntoSocorros("id = "+id).get(0);
      }
      catch (Exception e){
        return new ResponseEntity<MedicoProntoSocorro>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<MedicoProntoSocorro>(medicoProntoSocorro,HttpStatus.OK);
    }
}
