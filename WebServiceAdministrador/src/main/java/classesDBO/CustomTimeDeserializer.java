package classesDBO;

import java.io.IOException;
import java.sql.Time;
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

public class CustomTimeDeserializer extends JsonDeserializer<Time>{
  
  @Override
  public Time deserialize(JsonParser jsonParser,DeserializationContext context)throws IOException,JsonProcessingException{
    Time hora = null;
	try{
	  hora = new Time(new SimpleDateFormat("HH:mm").parse(jsonParser.getText()).getTime());
    }
    catch (Exception e){
      e.printStackTrace();
    }
	return hora;
  }
}
