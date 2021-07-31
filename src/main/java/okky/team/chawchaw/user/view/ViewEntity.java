package okky.team.chawchaw.user.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import okky.team.chawchaw.user.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "views")
@Getter
@NoArgsConstructor
public class ViewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_from")
    private UserEntity userFrom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_to")
    private UserEntity userTo;

    public ViewEntity(UserEntity userFrom, UserEntity userTo) {
        this.userFrom = userFrom;
        this.userTo = userTo;
    }
}
