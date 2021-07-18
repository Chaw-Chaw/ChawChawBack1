package okky.team.chawchaw.user.country;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "country")
@Getter
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    Long id;
    String name;

}
