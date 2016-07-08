package classesDBO;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomTimestampSerializer extends JsonSerializer<Timestamp>{
  
  @Override
  public void serialize(Timestamp value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException{
    jsonGenerator.writeString(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(value));
  }
}
