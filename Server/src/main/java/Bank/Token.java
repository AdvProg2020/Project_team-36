package Bank;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class Token {
    private String token;
    private long creationDate;
    private String username;

    public Token(String username){
        this.creationDate = new Date().getTime();
        this.token = generateToken();
         this.username = username;
    }

    private String generateToken(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[23];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }

    public boolean isTokenExpired(){
        Date now = new Date(creationDate+3_600_000);
        return now.before(new Date());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token1 = (Token) o;
        return token.equals(token1.token) &&
                creationDate==token1.creationDate &&
                username.equals(token1.username);
    }

}
