package okky.team.chawchaw.statistics.log;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_log")
@Getter
@NoArgsConstructor
public class SearchLogEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_log_id")
    Long id;

    Long userId;
    String languageAbbr;
    @Column(nullable = false, insertable = false, updatable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    LocalDateTime regDate;

    public SearchLogEntity(Long userId, String languageAbbr) {
        this.userId = userId;
        this.languageAbbr = languageAbbr;
    }

}
