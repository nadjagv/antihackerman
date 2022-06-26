package antihackerman.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "realestates")
@SQLDelete(sql
        = "UPDATE realestates "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RealEstate {
	
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    @ManyToMany(mappedBy = "realestatesTenanting", fetch = FetchType.LAZY)
    private Set<User> tenants=new HashSet<>();

    @OneToMany(mappedBy = "realestate", fetch = FetchType.LAZY)
    private Set<Device> devices=new HashSet<>();

}
