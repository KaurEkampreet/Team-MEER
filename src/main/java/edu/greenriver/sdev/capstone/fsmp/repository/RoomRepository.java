package edu.greenriver.sdev.capstone.fsmp.repository;

import edu.greenriver.sdev.capstone.fsmp.model.Custodian;
import edu.greenriver.sdev.capstone.fsmp.model.Room;
import edu.greenriver.sdev.capstone.fsmp.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    ArrayList<Room> getAllByZoneId(Long id);
    Room getByRoomType(String type);
    Room getRoomByZoneIdAndAndRoomType(Long zoneId, String name);

    Room getRoomById(Long id);

}
