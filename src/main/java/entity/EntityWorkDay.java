package entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Entity
@Getter
@Setter
@NoArgsConstructor
public class EntityWorkDay {


    @Id
    @GeneratedValue
    @Column(name = "hourTableDayId")
    private int hourTableDayId;

    @Column(name = "hourTableFrom")
    private Timestamp hourTableFrom;

    @Column(name = "hourTableTo")
    private Timestamp hourTableTo;

    @Column(name = "hourTableWorkHour")
    private String hourTableWorkHour;

    @Column(name = "hourTableNadgodziny")
    private String hourTableNadgodziny;





    @Override
    public String toString() {
        return "entityWorkDay{" +
                "hourTableDayId=" + hourTableDayId +
                ", hourTableFrom='" + hourTableFrom + '\'' +
                ", hourTableTo='" + hourTableTo + '\'' +
                ", hourTableWorkHour='" + hourTableWorkHour + '\'' +
                ", hourTableNadgodziny='" + hourTableNadgodziny + '\'' +
                '}';
    }
}
