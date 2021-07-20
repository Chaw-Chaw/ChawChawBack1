package okky.team.chawchaw.user.language;

import lombok.Getter;
import lombok.NoArgsConstructor;
import okky.team.chawchaw.user.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "user_language")
@Getter
@NoArgsConstructor
public class UserLanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_language_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    UserEntity user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    LanguageEntity language;

    public void changeUser(UserEntity userEntity) {
        this.user = userEntity;
        user.getUserLanguageEntities().add(this);
    }

    public UserLanguageEntity(UserEntity user, LanguageEntity language) {
        this.user = user;
        this.language = language;
    }
}
