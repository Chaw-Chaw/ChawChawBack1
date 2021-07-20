package okky.team.chawchaw.user.language;

import lombok.Getter;
import lombok.NoArgsConstructor;
import okky.team.chawchaw.user.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "user_hope_language")
@Getter
@NoArgsConstructor
public class UserHopeLanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_hope_language_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    UserEntity user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    LanguageEntity hopeLanguage;

    public void changeUser(UserEntity userEntity) {
        this.user = userEntity;
        user.getUserHopeLanguageEntities().add(this);
    }

    public UserHopeLanguageEntity(UserEntity user, LanguageEntity hopeLanguage) {
        this.user = user;
        this.hopeLanguage = hopeLanguage;
    }
}
