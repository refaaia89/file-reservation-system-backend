package fifth.year.backendinternetapplication.dto.response.fullData;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fifth.year.backendinternetapplication.dto.response.LogResponse;
import fifth.year.backendinternetapplication.model.Log;

import java.io.IOException;

@JsonSerialize(using = FullLogResponse.LogSerializer.class)
public class FullLogResponse {
    private LogResponse logResponse;

    public FullLogResponse(Log log) {
        this.logResponse = new LogResponse(log.getId(), log.getType().getType(), log.getData(), log.getUserId(), log.getCreated_at(), log.getUpdated_at());
    }

    public FullLogResponse() {
    }

    static class LogSerializer extends JsonSerializer<FullLogResponse> {

        @Override
        public void serialize(FullLogResponse fullLogResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("logId", fullLogResponse.logResponse.logId());
            jsonGenerator.writeStringField("type", fullLogResponse.logResponse.type());
            jsonGenerator.writeStringField("data", fullLogResponse.logResponse.data());
            jsonGenerator.writeNumberField("user_id", fullLogResponse.logResponse.userId());
            jsonGenerator.writeObjectField("created_at", fullLogResponse.logResponse.created_at());
            jsonGenerator.writeObjectField("update_at", fullLogResponse.logResponse.updated_at());
            jsonGenerator.writeEndObject();
        }
    }
}
