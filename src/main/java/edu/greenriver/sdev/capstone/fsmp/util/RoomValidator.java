package edu.greenriver.sdev.capstone.fsmp.util;


import edu.greenriver.sdev.capstone.fsmp.service.ZoneRepositoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomValidator {
    private ZoneRepositoryService zoneRepositoryService;

    public boolean hasValidZone(long facilityId, long zoneId) {
        return zoneRepositoryService.getZoneById(zoneId).getFacilityId().equals(facilityId);
    }

    public boolean hasValidRoomType(String roomType){
        //TODO: if we add the priority list, check that the roomtype exists in the list
        return (!roomType.isEmpty());
    }

    public boolean hasValidRoomNumber(String roomNumber){
        return (!roomNumber.isEmpty());
    }

    public boolean hasValidSquareFootage(String squareFootage){
        return (!squareFootage.isEmpty());
    }



}
