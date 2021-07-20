package okky.team.chawchaw.user.country;

import lombok.Getter;
import lombok.NoArgsConstructor;
import okky.team.chawchaw.user.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "user_country")
@Getter
@NoArgsConstructor
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

    public void changeUser(UserEntity userEntity) {
        this.user = userEntity;
        user.getUserCountryEntities().add(this);
    }

    public UserCountryEntity(UserEntity user, CountryEntity country) {
        this.user = user;
        this.country = country;
    }
}
