package antihackerman.backendapp.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserTokenState {

	private String accessToken,username;
    private Long expiresIn;
    private ArrayList<String> roles;

    public UserTokenState() {
        this.accessToken = null;
        this.expiresIn = null;
        this.username=null;
        this.roles=new ArrayList<String>();
    }
    
}
