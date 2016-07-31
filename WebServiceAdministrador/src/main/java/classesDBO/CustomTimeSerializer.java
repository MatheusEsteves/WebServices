package classesDBO;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomTimeSerializer extends JsonSerializer<Time>{
  
  @Override
  public void serialize(Time value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException{
    jsonGenerator.writeString(new SimpleDateFormat("HH:mm").format(new Date(value.getTime())));
  }
}
