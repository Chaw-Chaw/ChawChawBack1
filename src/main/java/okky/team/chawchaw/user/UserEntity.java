package okky.team.chawchaw.user;

import lombok.*;
import okky.team.chawchaw.follow.FollowEntity;
import okky.team.chawchaw.user.country.UserCountryEntity;
import okky.team.chawchaw.user.language.UserHopeLanguageEntity;
import okky.team.chawchaw.user.language.UserLanguageEntity;
import okky.team.chawchaw.utils.RoleAttributeConverter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String repCountry;
    private String repLanguage;
    private String repHopeLanguage;
    private String refreshToken;
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime lastLogout;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserCountryEntity> userCountrys = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserLanguageEntity> userLanguages = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserHopeLanguageEntity> userHopeLanguages = new ArrayList<>();
    @OneToMany(mappedBy = "userFrom")
    private List<FollowEntity> followFrom = new ArrayList<>();
    @OneToMany(mappedBy = "userTo")
    private List<FollowEntity> followTo = new ArrayList<>();

    @Column(nullable = false, insertable = false, updatable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime regDate;
    @Column(nullable = false)
    private Long views = 0L;
    @Column(nullable = false)
    @Convert(converter = RoleAttributeConverter.class)
    private Role role;

    @Builder
    public UserEntity(String email, String password, String name, String web_email, String school, String imageUrl, String content, String facebookUrl, String instagramUrl, Role role, String repCountry, String repLanguage, String repHopeLanguage) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.web_email = web_email;
        this.school = school;
        this.imageUrl = imageUrl;
        this.content = content == null ? "" : content;
        this.facebookUrl = facebookUrl == null ? "" : facebookUrl;
        this.instagramUrl = instagramUrl == null ? "" : instagramUrl;
        this.role = role == null ? Role.GUEST : role;
        this.repCountry = repCountry == null ? "" : repCountry;
        this.repLanguage = repLanguage == null ? "" : repLanguage;
        this.repHopeLanguage= repHopeLanguage == null ? "" : repHopeLanguage;
    }

    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl != null ? imageUrl : this.imageUrl;
    }
    public void changeContent(String content) {
        this.content = content != null ? content : this.content;
    }
    public void changeFacebookUrl(String url) {
        this.facebookUrl = url != null ? url : this.facebookUrl;
    }
    public void changeInstagramUrl(String url) {
        this.instagramUrl = url != null ? url : this.instagramUrl;
    }
    public void changeRepCountry(String repCountry) {
        this.repCountry = repCountry != null ? repCountry : this.repCountry;
    }
    public void changeRepLanguage(String repLanguage) {
        this.repLanguage = repLanguage != null ? repLanguage : this.repLanguage;
    }
    public void changeRepHopeLanguage(String repHopeLanguage) {
        this.repHopeLanguage = repHopeLanguage != null ? repHopeLanguage : this.repHopeLanguage;
    }
    public void changeRole(Role role) { this.role = role; }
    public void changeRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public void changeViews(Long views) { this.views = views; }
    public void changeLastLogout(LocalDateTime lastLogout) {  this.lastLogout = lastLogout; }
    public void plusViews() {
        this.views += 1;
    }
}
