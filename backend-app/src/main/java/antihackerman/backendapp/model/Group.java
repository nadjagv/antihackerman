package antihackerman.backendapp.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@SQLDelete(sql
        = "UPDATE groups "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "groupsOwning", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> owners=new ArrayList<User>();

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RealEstate> realEstates=new ArrayList<RealEstate>();

    @ManyToMany(mappedBy = "groupsTenanting", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> tenants=new ArrayList<User>();


}
