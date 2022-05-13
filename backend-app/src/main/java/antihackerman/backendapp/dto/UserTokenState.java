package antihackerman.backendapp.dto;

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
    private List<String> roles;

    public UserTokenState() {
    	this.roles=null;
    	this.username=null;
        this.accessToken = null;
        this.expiresIn = null;
    }
    
    public UserTokenState(String accessToken,String username,List<String> roles,Long expiresIn) {
    	this.username=username;
    	this.accessToken=accessToken;
    	this.expiresIn=expiresIn;
    	this.roles=roles;
    }
}
