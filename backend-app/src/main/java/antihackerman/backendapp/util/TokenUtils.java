package antihackerman.backendapp.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import antihackerman.backendapp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtils {
	
	@Value("backend")
	private String APP_NAME;

	@Value("somesecret")
	public String SECRET;

	@Value("90000")
	private int EXPIRES_IN;
	
	@Value("Authorization")
	private String AUTH_HEADER;
	
	@Value("Cookie")
    private String COOKIE_HEADER;
	
	private static final String AUDIENCE_WEB = "web";

	public SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
	
	private final SecureRandom secureRandom = new SecureRandom();
	
	public String generateToken(User user) {
		Claims claims = Jwts.claims().setSubject(user.getUsername());
		claims.put("email", user.getEmail());
	    claims.put("roles", Arrays.asList(user.getRoles()));
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuer(APP_NAME)
				.setAudience(generateAudience())
				.setIssuedAt(new Date())
				.setExpiration(generateExpirationDate())
				.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
	}
	
	public String generateFingerprint() {
        byte[] randomFgp = new byte[50];
        this.secureRandom.nextBytes(randomFgp);
        return DatatypeConverter.printHexBinary(randomFgp);
    }

    private String generateFingerprintHash(String userFingerprint) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] userFingerprintDigest = digest.digest(userFingerprint.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(userFingerprintDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
	
	private String generateAudience() {
		return AUDIENCE_WEB;
	}

	private Date generateExpirationDate() {
		return new Date(new Date().getTime() + EXPIRES_IN);
	}

	public String getToken(HttpServletRequest request) {
		String authHeader = getAuthHeaderFromHeader(request);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7); 
		}

		return null;
	}
	
	public String getFingerprintFromCookie(HttpServletRequest request) {
        String userFingerprint = null;
        if (request.getCookies() != null && request.getCookies().length > 0) {
            List<Cookie> cookies = Arrays.stream(request.getCookies()).collect(Collectors.toList());
            Optional<Cookie> cookie = cookies.stream().filter(c -> "Fingerprint".equals(c.getName())).findFirst();

            if (cookie.isPresent()) {
                userFingerprint = cookie.get().getValue();
            }
        }
        return userFingerprint;
    }
	
	public String getUsernameFromToken(String token) {
		String username;
		
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			username = claims.getSubject();
		} catch (ExpiredJwtException ex) {
			throw ex;
		} catch (Exception e) {
			username = null;
		}
		
		return username;
	}

	public Date getIssuedAtDateFromToken(String token) {
		Date issueAt;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			issueAt = claims.getIssuedAt();
		} catch (ExpiredJwtException ex) {
			throw ex;
		} catch (Exception e) {
			issueAt = null;
		}
		return issueAt;
	}

	public String getAudienceFromToken(String token) {
		String audience;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			audience = claims.getAudience();
		} catch (ExpiredJwtException ex) {
			throw ex;
		} catch (Exception e) {
			audience = null;
		}
		return audience;
	}

	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (ExpiredJwtException ex) {
			throw ex;
		} catch (Exception e) {
			expiration = null;
		}
		
		return expiration;
	}
	
	private String getFingerprintFromToken(String token) {
        String fingerprint;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            fingerprint = claims.get("userFingerprint", String.class);
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            fingerprint = null;
        }
        return fingerprint;
    }
	
	private String getIssuerFromToken(String token) {
        String issuer;

        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issuer = claims.getIssuer();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            issuer = null;
        }
        return issuer;
    }


    private String getAlgorithmFromToken(String token) {
        String algorithm;
        try {
            algorithm = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getHeader()
                    .getAlgorithm();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            algorithm = null;
        }
        return algorithm;
    }

	private Claims getAllClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException ex) {
			throw ex;
		} catch (Exception e) {
			claims = null;
		}
		
		return claims;
	}
	
	public Boolean validateToken(String token, UserDetails userDetails, String fingerprint) {
        User user = (User) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);

        boolean isUsernameValid = username != null 
                && username.equals(userDetails.getUsername())
                && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate());

        boolean isFingerprintValid = false;
        boolean isAlgorithmValid = false;
        if (fingerprint != null) {
            isFingerprintValid = validateTokenFingerprint(fingerprint, token);
            isAlgorithmValid = SIGNATURE_ALGORITHM.getValue().equals(getAlgorithmFromToken(token));
        }
        return isUsernameValid && isFingerprintValid && isAlgorithmValid;
    }
	
	private boolean validateTokenFingerprint(String fingerprint, String token) {
        String fingerprintHash = generateFingerprintHash(fingerprint);
        String fingerprintFromToken = getFingerprintFromToken(token);
        return fingerprintFromToken.equals(fingerprintHash);
    }
	
	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	public int getExpiredIn() {
		return EXPIRES_IN;
	}

	public String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(AUTH_HEADER);
	}

}
