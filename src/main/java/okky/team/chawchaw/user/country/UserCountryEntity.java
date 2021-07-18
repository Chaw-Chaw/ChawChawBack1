package okky.team.chawchaw.user.country;

import lombok.Getter;
import okky.team.chawchaw.user.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "user_country")
@Getter
public class UserCountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_country_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    UserEntity user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    CountryEntity country;

}
