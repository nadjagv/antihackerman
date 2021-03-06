package antihackerman.backendapp.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy=InheritanceType.JOINED)
@SQLDelete(sql
        = "UPDATE users "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails{
	
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
	
	@Column(name = "deleted", nullable = false)
    private boolean deleted;
    
    @Column(name = "username", nullable = false,unique = true)
	private String username;
    
    @Column(name = "email", nullable = false,unique = true)
	private String email;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "wrong_logins", nullable = false)
	private Integer wrongLogins;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "users_groups_owning",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
	private Set<Group> groupsOwning =new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "users_groups_tenanting",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
	private Set<Group> groupsTenanting =new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "users_realestates_tenanting",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "realestate_id", referencedColumnName = "id"))
	private Set<RealEstate> realestatesTenanting =new HashSet<>();
	
	@Column(name = "last_password_reset_date")
    private Timestamp lastPasswordResetDate; 
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles=new ArrayList<Role>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> permissions = new ArrayList<>(20);
        for (Role role : this.roles) {
            permissions.addAll(role.getPrivileges());
        }
        return permissions;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}
	
	public void setPassword(String password) {
        Timestamp now = new Timestamp(new Date().getTime());
        this.setLastPasswordResetDate(now);
        this.password = password;
    }

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		if(this.wrongLogins>4) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
