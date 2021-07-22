package okky.team.chawchaw.follow;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import okky.team.chawchaw.user.UserEntity;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "follow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Cacheable
public class FollowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;
    @Column(nullable = false, insertable = false, updatable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_from")
    private UserEntity userFrom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_to")
    private UserEntity userTo;

    public FollowEntity(UserEntity userFrom, UserEntity userTo) {
        this.userFrom = userFrom;
        this.userTo = userTo;
    }
}
