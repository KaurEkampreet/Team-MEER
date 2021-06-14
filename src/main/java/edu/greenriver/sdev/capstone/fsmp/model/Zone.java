package edu.greenriver.sdev.capstone.fsmp.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zoneName;

    /*@ManyToOne*/
    private Long facilityId;

    /*@OneToMany(mappedBy = "zone", cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();*/

    public String toString() {
        return "Zone(id=" + this.getId() + ", zoneName=" + this.getZoneName() + ", facilityId=" + this.getFacilityId() +")";
    }
}
