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
@Getter
@Setter
@ToString
@Component
public class CustodianPOJO {

    @JsonProperty("Assigned Custodian ID")
    String assignedCustodianId;
    @JsonProperty("First Name")
    String firstName;
    @JsonProperty("Last Name")
    String lastName;
    @JsonProperty("Start Date")
    String startDate;
    @JsonProperty("Status")
    String status;
    @JsonProperty("Training")
    String training;
    @JsonProperty("Training Hours")
    String trainingHours;
    @JsonProperty("Class-1")
    String classOne;
    @JsonProperty("Class-2")
    String classTwo;
    @JsonProperty("Class-3")
    String classThree;
    @JsonProperty("Class-4")
    String ClassFour;

    public List<CustodianPOJO> retrievePOJOList() {

        ObjectMapper objectMapper= new ObjectMapper();

        TypeReference<List<CustodianPOJO>> typeTPOJOTypeReference = new TypeReference<>(){};
        InputStream typeTResourceInputStream = TypeReference.class.getResourceAsStream("/static/json/CustodianList.json");
        List<CustodianPOJO> typeTPOJOList = new ArrayList<>();

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
