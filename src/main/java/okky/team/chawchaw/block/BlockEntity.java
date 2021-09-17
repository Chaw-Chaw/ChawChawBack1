package okky.team.chawchaw.block;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import okky.team.chawchaw.user.UserEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "block")
@Getter
@DynamicInsert @DynamicUpdate
@NoArgsConstructor
@ToString
public class BlockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long id;
    @Column(nullable = false, insertable = false, updatable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_from")
    private UserEntity userFrom;
    @JoinColumn(name = "user_to")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userTo;

    public BlockEntity(UserEntity userFrom, UserEntity userTo) {
        this.userFrom = userFrom;
        this.userTo = userTo;
    }
}
