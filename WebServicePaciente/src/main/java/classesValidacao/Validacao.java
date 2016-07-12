package classesValidacao;

import classesDBO.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

/**
  * @author Equipe do projeto Médico Fácil 
  *
  * Classe para a realização das seguintes validações:
  *    CPF
  *    Nome de uma pessoa
  *    Nome de uma instituição
  *    Sigla
  *    Palavra
  *    Endereço
  *    Localização
  *    Complemento
  *    Telefone
  *    Celular
  *    CRM
  *    Data no passado
  *    Data no futuro
  *    Intervalo entre datas
  *    Intervalo entre horários
  *    Dia da semana
*/
public class Validacao{
  
  /** 
  * Verifica se determinada string foi fornecida ou não.
  * Para ser considerada não fornecida, precisa estar nula
  * ou istanciada e não conter nada.
  *
  *@param string String - String para verificação.
  *@return boolean - Indica se a string foi fornecida ou não.
  */
  private static boolean isStringFornecida(String string){
    if (string == null)
      return false;
    if (string.trim().equals(""))
      return false;
    return true;
  }
    
  /** 
  * Verifica se algum caracter da string é diferente de todos os caracteres válidos.
  *
  * @param string String - String para verificação.
  * @param caracteresValidos String - Caracteres válidos.
  * @return boolean - Indica se a string é válida ou não.
  */
  private static boolean isStringValidaA(String string,String caracteresValidos){  
    for (int i = 0; i < string.length(); i++)
      if (!caracteresValidos.contains(string.charAt(i)+""))
        return false;
    return true;
  }
  
  /** 
  * Verifica se algum caracteres da string é igual à algum caracter inválido.
  *
  * @param string String - String para verificação.
  * @param caracteresValidos String - Caracteres inválidos.
  * @return boolean - Indica se a string é válida ou não.
  */
  private static boolean isStringValidaB(String string,String caracteresInvalidos){  
    for (int i = 0; i < string.length(); i++)
      if (caracteresInvalidos.contains(string.charAt(i)+""))
        return false;
    return true;
  }
    
  /** 
  * Verifica se o nome da pessoa é válido ou não. Para ser válido, não pode
  * conter nenhum caracter diferente de número ou letra.
  * 
  * @param nome String - Nome da pessoa.
  * @param maxCaracteres int - Máximo de caracteres para o nome da pessoa.
  * @return boolean - Indica se o nome da pessoa é válido ou não.
  */
  public static boolean isNomePessoaValido(String nome,int maxCaracteres)throws Exception{
    if (maxCaracteres < 1)
      throw new Exception("Número máximo de caracteres inválido."); 
    if (!Validacao.isStringFornecida(nome))
      return false;
    if (nome.length() > maxCaracteres)
      return false;
    return Validacao.isStringValidaB(nome,"\"\\1234567890!@#$%¨&*()-=¹²³£¢¬§+_/?°|₢,.;:`^~{}º[]ª");
  }
  
  /** 
  * Verifica se o nome da instituição é válido ou não. Para ser válido, só
  * poderá conter letras ou números.
  * 
  * @param nome String - Nome da instituição.
  * @param maxCaracteres int - Máximo de caracteres para o nome da instituição.
  * @return boolean - Indica se o nome da instituição é válido ou não.
  */
  public static boolean isNomeInstituicaoValido(String nome,int maxCaracteres)throws Exception{
    if (maxCaracteres < 1)
      throw new Exception("Número máximo de caracteres inválido.");
    if (!Validacao.isStringFornecida(nome))
      return false;
    if (nome.length() > maxCaracteres)
      return false;
    return Validacao.isStringValidaB(nome,"\"\\!@#$%¨&*-=¹²³£¢¬§+_/?°|₢,.;:`^~{}º[]ª");
  }
  
  /**
  * Verifica se uma determinada sigla é válida ou não. Para ser válida, só
  * poderá conter letras, sem acentuação.
  * 
  * @param sigla String - Texto da sigla.
  * @param qtosCaracteres int - Número de caracteres de uma sigla.
  * @return boolean - Indica e o texto da sigla é válido ou não.
  */
  public static boolean isSiglaValida(String sigla,int qtosCaracteres)throws Exception{
    if (!Validacao.isStringFornecida(sigla))
      return false;
    if (sigla.length() != qtosCaracteres)
      return false;
    return Validacao.isStringValidaA(sigla,"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
  }
  
  /** 
  * Verifica se a palavra é válida ou não. Caso tenha
  * algum caracter que não seja letra (maiúscula ou minúscula), será considerado inválida.
  * 
  * @param palavra String - Palavra a ser validada.
  * @param maxCaracteres int - Máximo de caracteres da palavra.
  * @return boolean - Indica se a palavra é válida ou não.
  */
  public static boolean isPalavraValida(String palavra,int maxCaracteres)throws Exception{
    if (!Validacao.isStringFornecida(palavra))
      return false;
    if (palavra.length() > maxCaracteres)
      return false;
    return Validacao.isStringValidaA(
      palavra,
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZÇçÀàòÒèÈìÌùÙÁáéÉíÍóÓúÚãÃõÕâÂÊêîÎÔôûÛ ");
  }
  
  /** 
  * Verifica se o cpf é válido ou não. Não deve ser passado com pontos ou traços.
  * Considera-se inválido o cpf formado por números iguais.
  * Para os demais casos, realizamos o algoritmo para validação de cpf,
  * calculando o primeiro e segundo dígito verificador e comparando com
  * os dígitos do cpf. Se forem iguais, o cpf será válido. Caso contrário,
  * será inválido.
  * 
  * @param cpf String - CPF a ser validado.
  * @return boolean - Indica se o cpf é válido ou não.
  */
  public static boolean isCpfValido(String cpf){
    if (!Validacao.isStringFornecida(cpf))
      return false;
    
    cpf = cpf.replace(".","");
    if (cpf.length() != 11)
      return false;
    if (!Validacao.isStringValidaA(cpf,"0123456789"))
      return false;
    
    if (cpf.charAt(0) == cpf.charAt(1) &&
        cpf.charAt(1) == cpf.charAt(2) &&
        cpf.charAt(2) == cpf.charAt(3) &&
        cpf.charAt(3) == cpf.charAt(4) &&
        cpf.charAt(4) == cpf.charAt(5) &&
        cpf.charAt(5) == cpf.charAt(6) &&
        cpf.charAt(6) == cpf.charAt(7) &&
        cpf.charAt(7) == cpf.charAt(8) &&
        cpf.charAt(8) == cpf.charAt(9) &&
        cpf.charAt(9) == cpf.charAt(10))
      return false; 
    
    // Para os demais casos, realizamos o algoritmo para validação de cpf.
    
    // Calculando o primeiro dígito verificador.
     int soma = Integer.parseInt(cpf.charAt(0)+"") * 10;
    soma += Integer.parseInt(cpf.charAt(1)+"") * 9;
    soma += Integer.parseInt(cpf.charAt(2)+"") * 8;
    
    soma += Integer.parseInt(cpf.charAt(3)+"") * 7;
    soma += Integer.parseInt(cpf.charAt(4)+"") * 6;
    soma += Integer.parseInt(cpf.charAt(5)+"") * 5;
    
    soma += Integer.parseInt(cpf.charAt(6)+"") * 4;
    soma += Integer.parseInt(cpf.charAt(7)+"") * 3;
    soma += Integer.parseInt(cpf.charAt(8)+"") * 2;

    int resto = soma % 11;
    int dig1, dig2;

    if (resto < 2)
        dig1 = 0;
    else
    	dig1 = 11 - resto;

    if (dig1 != Integer.parseInt(cpf.charAt(9)+""))
    	return false;


    soma = Integer.parseInt(cpf.charAt(0)+"") * 11;
    soma += Integer.parseInt(cpf.charAt(1)+"") * 10;
    soma += Integer.parseInt(cpf.charAt(2)+"") * 9;

    soma += Integer.parseInt(cpf.charAt(3)+"") * 8;
    soma += Integer.parseInt(cpf.charAt(4)+"") * 7;
    soma += Integer.parseInt(cpf.charAt(5)+"") * 6;
    
    soma += Integer.parseInt(cpf.charAt(6)+"") * 5;
    soma += Integer.parseInt(cpf.charAt(7)+"") * 4;
    soma += Integer.parseInt(cpf.charAt(8)+"") * 3;
    soma += Integer.parseInt(cpf.charAt(9)+"") * 2;

    resto = soma % 11;

    if (resto < 2)
    	dig2 = 0;
    else
    	dig2 = 11 - resto;

    if (dig2 != Integer.parseInt(cpf.charAt(10)+""))
    	return false;
    return true;
  }
  
  /**
  * Verifica se o endereço é válido ou não. Para ser considerado válido,
  * pode conter letras, números, vírgula e ponto.
  * 
  * @param endereco String - Endereço a ser validado.
  * @param maxCaracteres int - Máximo de caracteres do endereço.
  * @return boolean - Indica se o endereço é válido ou não.
  */
  public static boolean isEnderecoValido(String endereco,int maxCaracteres)throws Exception{
    if (maxCaracteres < 1)
      throw new Exception("Número máximo de caracteres inválido.");
    if (!Validacao.isStringFornecida(endereco))
      return false;
    if (endereco.length() > maxCaracteres)
      return false;
    return Validacao.isStringValidaB(endereco,"\"'!@#$%¨&*()_+=§-¬£³²¹/?°|\\₢;:^~`{}[]");
  }
  
  /**
  * Verifica se o nome de uma determinada localização é válido. Ex: bairro,cidade, região etc. 
  * Para ser considerado válido, pode conter apenas letras ou números.
  * 
  * @param localizacao String - Localização a ser validada.
  * @param maxCaracteres int - Máximo de caracteres da localização.
  * @return boolean - Indica se a localização é válida ou não.
  */ 
  public static boolean isNomeLocalizacaoValido(String localizacao,int maxCaracteres)throws Exception{
    if (maxCaracteres < 1)
      throw new Exception("Número máximo de caracteres inválido.");
    if (!Validacao.isStringFornecida(localizacao))
      return false;
    if (localizacao.length() > maxCaracteres)
      return false;
    return Validacao.isStringValidaB(localizacao,"\"'!@#$%¨&*()_+=§-¬,£³²¹/?°|\\₢;:^~`{}[]");
  }
  
  /** 
  * Verifica se o complemento do endereço é válido ou não. Para ser 
  * considerado válido, pode conter apenas letras, números, vírgula,
  * ponto(.), dois pontos(:) ou parênteses. 
  * 
  * @param complemento String - Complemento do endereço.
  * @param maxCaracteres int - Máximo de caracteres para o complemento.
  * @return boolean - Indica se o complemento do endereço é válido ou não.
  */
  public static boolean isComplementoEnderecoValido(String complemento,int maxCaracteres)throws Exception{
    if (maxCaracteres < 1)
      throw new Exception("Número máximo de caracteres inválido.");
    if (!Validacao.isStringFornecida(complemento))
      return false;
    if (complemento.length() > maxCaracteres)
      return false;
    return Validacao.isStringValidaB(complemento,"\"'!@#$%¨&*_+=§¬£³²¹/?°|\\₢;^~`{}[]");
  }
  
  /**
  * Verifica se o telefone é válido ou não. Para ser considerado
  * válido, deve contém 10 caracteres numéricos.
  * 
  * @param telefone String - Telefone a ser validado.
  * @return boolean - Indica se o telefone é válido ou não.
  */ 
  public static boolean isTelefoneValido(String telefone)throws Exception{
    if (!Validacao.isStringFornecida(telefone))
      return false;
    if (telefone.length() != 10)
      return false;
    return Validacao.isStringValidaA(telefone,"0123456789");
  }
  
  /**
  * Verifica se o celular é válido ou não. Para ser considerado
  * válido, deve conter 11 caracteres numéricos.
  * 
  * @param celular String - Celular a ser validado.
  * @return boolean - Indica se o celular é válido ou não.
  */
  public static boolean isCelularValido(String celular)throws Exception{
    if (!Validacao.isStringFornecida(celular))
      return false;
    if (celular.length() != 11)
      return false;
    return Validacao.isStringValidaA(celular,"0123456789");
  }
  
  /**
  * Verifica se o crm é válido ou não. Para ser considerado
  * válido, só pode conter números em sua composição.
  * 
  * @param crm String - CRM do médico.
  * @return boolean - Indica se o CRM do médico é válido ou não.
  */
  public static boolean isCrmValido(String crm)throws Exception{
    if (!Validacao.isStringFornecida(crm))
      return false;
    return Validacao.isStringValidaA(crm,"0123456789");
  }
  
  /**
  * Verifica se uma data deve que estar no passado já ocorreu. 
  * Ex : Se hoje é dia 13/04/2016 e passamos como parâmetro a 
  * data 14/04/2016, esta será considerada inválida, já que 
  * precisava estar no passado.
  * 
  * @param Date dataAnterior - Data que deve estar no passado.
  * @return boolean - Indica se a data é válida ou não.
  */
  public static boolean isDataAnteriorValida(Date dataAnterior)throws Exception{
    if (dataAnterior == null)
      return false;
    if (dataAnterior.compareTo(new Date(System.currentTimeMillis())) > 0)
      return false;
    return true;
  }
  
  /**
  * Verifica se uma data que deve estar no futuro ainda não ocorreu.
  * Ex: Se hoje é dia 13/04/2016 e passamos como parâmetro a
  * data 12/04/2016, esta será considerada inválida, já que
  * precisava estar no futuro.
  * 
  * @param Date dataPosterior - Data que deve estar no futuro.
  * @return boolean - Indica se a data é válida ou não.
  */
  public static boolean isDataPosteriorValida(Date dataPosterior)throws Exception{
    if (dataPosterior == null)
      return false;
    if (dataPosterior.compareTo(new Date(System.currentTimeMillis())) < 0)
      return false;
    return true;
  }
  
  /**
  * Verifica se o intervalo de datas é válido ou não.
  * A data final deve ser posterior ou igual a data inicial.
  * 
  * @param dataInicio Date - Data de início.
  * @param dataFim Date - Data de término.
  * @return boolean - Indica se o intervalo é válido ou não.
  */
  public static boolean isIntervaloDatasValido(Date dataInicio,Date dataFim)throws Exception{
    if (dataInicio == null || dataFim == null)
      return false;
    if (dataInicio.compareTo(dataFim) > 0)
      return false;
    return true;
  }
  
  /**
  * Verifica se uma data deve que estar no passado já ocorreu. 
  * Ex : Se hoje é dia 13/04/2016 e passamos como parâmetro a 
  * data 14/04/2016, esta será considerada inválida, já que 
  * precisava estar no passado.
  * 
  * @param Timestamp dataAnterior - Data que deve estar no passado.
  * @return boolean - Indica se a data é válida ou não.
  */
  public static boolean isDataAnteriorValida(Timestamp dataAnterior)throws Exception{
    if (dataAnterior == null)
      return false;
    if (dataAnterior.compareTo(new Timestamp((new java.util.Date()).getTime())) > 0)
      return false;
    return true;
  }
  
  /**
  * Verifica se uma data que deve estar no futuro ainda não ocorreu.
  * Ex: Se hoje é dia 13/04/2016 e passamos como parâmetro a
  * data 12/04/2016, esta será considerada inválida, já que
  * precisava estar no futuro.
  * 
  * @param Timestamp dataPosterior - Data que deve estar no futuro.
  * @return boolean - Indica se a data é válida ou não.
  */
  public static boolean isDataPosteriorValida(Timestamp dataPosterior)throws Exception{
    if (dataPosterior == null)
      return false;
    if (dataPosterior.compareTo(new Timestamp((new java.util.Date()).getTime())) < 0)
      return false;
    return true;
  }
  
  /**
  * Verifica se o intervalo de datas é válido ou não.
  * A data final deve ser posterior ou igual a data inicial.
  * 
  * @param dataInicio Timestamp - Data de início.
  * @param dataFim Timestamp - Data de término.
  * @return boolean - Indica se o intervalo é válido ou não.
  */
  public static boolean isIntervaloDatasValido(Timestamp dataInicio,Timestamp dataFim)throws Exception{
    if (dataInicio == null || dataFim == null)
      return false;
    if (dataInicio.compareTo(dataFim) > 0)
      return false;
    return true;
  }
  
  /**
  * Verifica se o intervalo entre os horários é válido ou não. 
  * O horário final deve ser posterior ou igual ao horário inicial.
  * 
  * @param horarioInicio Time - Horário de início.
  * @param horarioFim Time - Horário de término.
  * @return boolean - Indica se o intervalo é válido ou não.
  */
  public static boolean isIntervaloHorariosValido(Time horarioInicio,Time horarioFim)throws Exception{
    if (horarioInicio == null || horarioFim == null)
      return false;
    if (horarioInicio.compareTo(horarioFim) > 0)
      return false;
    return true;
  }
  
  /**
   * Retorna o número relacionado ao dia da semana, conforme o esquema abaixo:
   * Domingo       : 0
   * Segunda-feira : 1
   * Terça-feira   : 2
   * Quarta-feira  : 3
   * Quinta-feira  : 4
   * Sexta-feira   : 5
   * Sábado        : 6
   * Caso o dia passado como parâmetro não se enquadre em nenhum desses, o retorno será -1;
   * @param dia
   * @return
   */
  public static int getNumeroDia(String dia){
	  switch(dia){
        case "DOM" : return 0;
        case "SEG" : return 1;
        case "TER" : return 2;
        case "QUA" : return 3;
        case "QUI" : return 4;
        case "SEX" : return 5;
        case "SÁB" : return 6;
        default    : return -1;
	  }
  }
}
