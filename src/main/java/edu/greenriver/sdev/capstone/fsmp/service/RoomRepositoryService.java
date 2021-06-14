package edu.greenriver.sdev.capstone.fsmp.service;

import edu.greenriver.sdev.capstone.fsmp.model.Room;
import edu.greenriver.sdev.capstone.fsmp.model.Zone;
import edu.greenriver.sdev.capstone.fsmp.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@AllArgsConstructor
@Service
public class RoomRepositoryService {

    RoomRepository roomRepository;

    public void createInitialRoom(Zone zone) {
        Room initialRoom = Room.builder()
                .zoneId(zone.getId())
                .roomNumber("Unassigned")
                .roomType("Unassigned")
                .squareFootage("Unassigned")
                .build();

        roomRepository.save(initialRoom);
    }

    public Zone addRoom(Zone zone, Room room){

        ArrayList<Room> roomsToCompare = roomRepository.getAllByZoneId(zone.getId());

        for(Room currentRoom : roomsToCompare){
            if(currentRoom.getRoomType().equals(room.getRoomType()) &&
            currentRoom.getRoomNumber().equals(room.getRoomNumber())){
                return null;
            }
        }

        room.setZoneId(zone.getId());

        roomRepository.save(room);

        if (room.getId() != null) {
            return zone;
        } else {
            return null;
        }
    }

    public ArrayList<Room> getRoomsByZoneId(Long id) {
        return roomRepository.getAllByZoneId(id);
    }

    public Room getRoomById(Long id) {
        return roomRepository.getRoomById(id);
    }
}
