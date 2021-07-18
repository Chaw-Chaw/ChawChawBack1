package okky.team.chawchaw.user.language;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "language")
@Getter
public class LanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    Long id;
    String name;

}
