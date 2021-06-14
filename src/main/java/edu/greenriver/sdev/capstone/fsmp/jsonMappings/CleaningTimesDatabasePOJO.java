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
public class CleaningTimesDatabasePOJO {
    @JsonProperty("Level Of Cleaning Times")
    String levelOfCleaningTimes;
    @JsonProperty("Building Number")
    String buildingNumber;
    @JsonProperty("Clean or Not ID")
    String cleanOrNotId;
    @JsonProperty("Rotation Eligilble ID")
    String rotationEligibleId;
    @JsonProperty("Rotation Days ID")
    String rotationDaysId;
    @JsonProperty("Assigned Custodian ID")
    String assignedCustodianId;
    @JsonProperty("Colored assigned to Area")
    String colorAssignedToArea;
    @JsonProperty("Room Number")
    String roomNumber;
    @JsonProperty("Room Letter")
    String  roomLetter;
    @JsonProperty("Space Type")
    String spaceType;
    @JsonProperty( "Secondary Description")
    String secondaryDescription;
    @JsonProperty("Total Sqft")
    String totalSqft;
    @JsonProperty("Vacuuming Carpet")
    String vacuumingCarpet;
    @JsonProperty("Dust Mopping Hard Surfaces 48\"")
    String dustMoppingHardSurfaceFourtyEight;
    @JsonProperty("Dust Mopping Hard Surfaces 60\"")
    String dustMoppingHardSurfaceSixty;
    @JsonProperty("Moping Hard Surfaces")
    String moppingHardSurfaces;
    @JsonProperty("Spot Mopping Hard Surfaces")
    String spotMoppingHardSurfaces;
    @JsonProperty("Number of Dispensers to clean, change or empty: Toilet Paper, So")
    String numberOfDispensers;
    @JsonProperty("Number of fixtures that need to be cleaned: Sinks, Urinals, Toil")
    String numberOfFixtures;
    @JsonProperty("Number of cans that need to emptied or relined: Trash, Recycle,")
    String numberOfCans;
    @JsonProperty("Entry Windows in doors")
    String entryWindowsInDoors;
    @JsonProperty("Flat surfaces to be cleaned: Health room beds, Tables, Desks")
    String flatSurfaces;
    @JsonProperty("Auto scrubbing Floors")
    String autoScrubbingFloors;
    @JsonProperty("Number of Stairs to Clean")
    String numberOfStairs;
    @JsonProperty("Dusting along the edges: about a 1/4 of the room")
    String dustingAlongEdges;
    @JsonProperty("Total Cleaning Time")
    String totalCleaningTime;
    @JsonProperty("Actual Time")
    String actualTime;
    @JsonProperty("Rotating Cleaning Times")
    String rotatingCleaningTimes;
    @JsonProperty("Rotating Actual Time")
    String rotatingActualTime;

    public List<CleaningTimesDatabasePOJO> retrievePOJOList() {

        ObjectMapper objectMapper= new ObjectMapper();

        TypeReference<List<CleaningTimesDatabasePOJO>> typeTPOJOTypeReference = new TypeReference<>(){};
        InputStream typeTResourceInputStream = TypeReference.class.getResourceAsStream("/static/json/CleaningTimesDatabase.json");
        List<CleaningTimesDatabasePOJO> typeTPOJOList = new ArrayList<>();

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
