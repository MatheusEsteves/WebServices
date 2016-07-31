package classesDBO;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomTimestampDeserializer extends JsonDeserializer<Timestamp>{
  
  @Override
  public Timestamp deserialize(JsonParser jsonParser,DeserializationContext context)throws IOException,JsonProcessingException{
    Timestamp dataHora = null;
	try{
	  dataHora = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(jsonParser.getText()).getTime());
    }
    catch (Exception e){
      e.printStackTrace();
    }
	return dataHora;
  }
}
