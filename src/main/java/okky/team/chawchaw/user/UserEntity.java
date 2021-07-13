package okky.team.chawchaw.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import okky.team.chawchaw.utils.RoleAttributeConverter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String web_email;
    @Column(nullable = false)
    private String school;
    private String imageUrl;
    @Column(length = 2000)
    private String content;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String language;
    @Column(nullable = false)
    private String hopeLanguage;
    private String socialUrl;
    @Column(nullable = false, insertable = false, updatable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime regDate;
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long views;
    @Column(nullable = false)
    @Convert(converter = RoleAttributeConverter.class)
    private Role role;

    @Builder
    public UserEntity(String email, String name, String web_email, String school, String imageUrl, String content, String country, String language, String hopeLanguage, String socialUrl, Role role) {
        this.email = email;
        this.name = name;
        this.web_email = web_email;
        this.school = school;
        this.imageUrl = imageUrl;
        this.content = content;
        this.country = country;
        this.language = language;
        this.hopeLanguage = hopeLanguage;
        this.socialUrl = socialUrl;
        this.role = role;
    }
}
