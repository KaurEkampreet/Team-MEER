package edu.greenriver.sdev.capstone.fsmp.repository;

import edu.greenriver.sdev.capstone.fsmp.model.Facility;
import edu.greenriver.sdev.capstone.fsmp.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    ArrayList<Zone> getAllByFacilityId(Long id);

    Zone getById(Long id);

    Zone getZoneByFacilityIdAndAndZoneName(Long id, String name);

    Facility getByFacilityId(Long facilityId);
}
