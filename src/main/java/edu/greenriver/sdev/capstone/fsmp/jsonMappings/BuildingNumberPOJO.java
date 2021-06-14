package edu.greenriver.sdev.capstone.fsmp.jsonMappings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@ToString
@Component
public class BuildingNumberPOJO {

    @JsonProperty("Number")
    String number;

    @JsonProperty("Name")
    String name;

    public List<BuildingNumberPOJO> retrievePOJOList() {

        ObjectMapper objectMapper= new ObjectMapper();

        TypeReference<List<BuildingNumberPOJO>> typeTPOJOTypeReference = new TypeReference<>(){};
        InputStream typeTResourceInputStream = TypeReference.class.getResourceAsStream("/static/json/BuildingNumber.json");
        List<BuildingNumberPOJO> typeTPOJOList = new ArrayList<>();

        try {
            typeTPOJOList = objectMapper.readValue(typeTResourceInputStream,typeTPOJOTypeReference);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return typeTPOJOList;
    }
}
