package okky.team.chawchaw.user;

import lombok.*;
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
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String web_email;
    @Column(nullable = false)
    private String school;
    @Column(length = 1000)
    private String imageUrl;
    @Column(length = 2000)
    private String content;
    private String facebookUrl;
    private String instagramUrl;
    @Column(nullable = false, insertable = false, updatable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime regDate;
    @Column(nullable = false)
    private Long views = 0L;
    @Column(nullable = false)
    @Convert(converter = RoleAttributeConverter.class)
    private Role role;

    @Builder
    public UserEntity(String email, String password, String name, String web_email, String school, String imageUrl, String content, String facebookUrl, String instagramUrl, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.web_email = web_email;
        this.school = school;
        this.imageUrl = imageUrl;
        this.content = content;
        this.facebookUrl = facebookUrl;
        this.instagramUrl = instagramUrl;
        this.role = role == null ? Role.USER : role;
    }

    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void changeContent(String content) {
        this.content = content;
    }

}
