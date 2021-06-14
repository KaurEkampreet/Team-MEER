package edu.greenriver.sdev.capstone.fsmp.service;

import edu.greenriver.sdev.capstone.fsmp.model.Custodian;
import edu.greenriver.sdev.capstone.fsmp.model.Facility;
import edu.greenriver.sdev.capstone.fsmp.model.Room;
import edu.greenriver.sdev.capstone.fsmp.model.Zone;
import edu.greenriver.sdev.capstone.fsmp.repository.CustodianRepository;
import edu.greenriver.sdev.capstone.fsmp.repository.RoomRepository;
import edu.greenriver.sdev.capstone.fsmp.repository.ZoneRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ZoneRepositoryService {
    ZoneRepository zoneRepository;
    RoomRepositoryService roomRepositoryService;
    RoomRepository roomRepository;
    CustodianRepository custodianRepository;

    public void createInitialZone(Facility facility) {
        Zone initialZone = Zone.builder()
                .facilityId(facility.getId())
                .zoneName("Unassigned")
                .build();

        zoneRepository.save(initialZone);

        roomRepositoryService.createInitialRoom(initialZone);
    }

    public Zone addZone(Facility facility, Zone zone){

        ArrayList<Zone> zonesToCompare = zoneRepository.getAllByFacilityId(facility.getId());

        for(Zone currentZone : zonesToCompare){
            if(currentZone.getZoneName().equals(zone.getZoneName())){
                return null;
            }
        }

        /*
        zone.getRooms().add(Room.builder()
                .roomType("Unassigned")
                .zone(zone)
                .build());
        */

        zone.setFacilityId(facility.getId());

        zone = zoneRepository.save(zone);

        roomRepositoryService.createInitialRoom(zone);

        if (zone.getId() != null) {
            return zone;
        } else {
            return null;
        }
    }

    public Zone getUnassignedZone(Long id) {
        return zoneRepository.getZoneByFacilityIdAndAndZoneName(id, "Unassigned");
    }

    public ArrayList<Zone> getAllZonesByFacilityID(Long id) {
        return zoneRepository.getAllByFacilityId(id);
    }

    public Zone getZoneByFacilityIdAndZoneName(Long id, String zoneName) {
        return zoneRepository.getZoneByFacilityIdAndAndZoneName(id, zoneName);
    }

    public Zone getZoneById(Long id) {
        return zoneRepository.getById(id);
    }

    public boolean deleteZoneAndRooms(Long id) {

        List<Room> rooms = roomRepository.getAllByZoneId(id);

        for (Room room : rooms) {
            roomRepository.deleteById(room.getId());
        }

        zoneRepository.deleteById(id);
        return zoneRepository.getById(id) == null;
    }

    public boolean deleteZone(Zone zone) {
        List<Room> rooms = roomRepository.getAllByZoneId(zone.getId());
        List<Custodian> custodians = custodianRepository.getCustodiansByZoneId(zone.getId());

        Zone unassignedZone = getZoneByFacilityIdAndZoneName(zone.getFacilityId(), "Unassigned");

        for (Room room: rooms) {
            if (!room.getRoomType().equals("Unassigned")) {
                room.setZoneId(unassignedZone.getId());
                roomRepository.save(room);
            } else {
                roomRepository.deleteById(room.getId());
            }
        }

        for (Custodian custodian: custodians) {
            custodian.setZoneId(unassignedZone.getId());
            custodianRepository.save(custodian);
        }

        zoneRepository.deleteById(zone.getId());
        return zoneRepository.getById(zone.getId()) == null;
    }

    public Facility getZoneByFacilityId(Long facilityId) {
        return zoneRepository.getByFacilityId(facilityId);
    }
}
