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
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@ManyToOne*/
    private Long zoneId;

    private String roomNumber;

    private String roomType;

    private String squareFootage;

    /*@ManyToMany
    private List<Custodian> custodians = new ArrayList<>();*/

    public String toString() {
        return "Room(id=" + this.getId() + ", zone=" + this.getZoneId() + ", roomNumber=" + this.getRoomNumber() + ", roomType=" + this.getRoomType() + ", squareFootage=" + this.getSquareFootage() + ")";
    }
}
