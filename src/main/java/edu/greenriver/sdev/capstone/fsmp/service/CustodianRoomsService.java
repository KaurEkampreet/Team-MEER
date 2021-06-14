package edu.greenriver.sdev.capstone.fsmp.service;

import edu.greenriver.sdev.capstone.fsmp.model.*;
import edu.greenriver.sdev.capstone.fsmp.repository.RoomRepository;
import edu.greenriver.sdev.capstone.fsmp.repository.ZoneRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@AllArgsConstructor
@Service
public class CustodianRoomsService {

    ZoneRepository zoneRepository;
    RoomRepository roomRepository;

    public ArrayList<Room>  getCustodianRooms(Custodian custodian) {

        Zone zone = zoneRepository.getById(custodian.getZoneId());

        if (zone.getZoneName().equals("Unassigned")) {
            return new ArrayList<>();
        }

        return roomRepository.getAllByZoneId(zone.getId());
    }
}
