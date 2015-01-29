package griffio.auth0.spring;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;

import java.util.Map;

public final class JWTEncoding {

    private final String clientSecret;
    private final String clientId;

    public JWTEncoding(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String encode(Map<String, Object> map) throws Exception {
        JWTSigner jwtSigner = new JWTSigner(clientSecret);
        return jwtSigner.sign(map);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> decode(String token) throws Exception {
        JWTVerifier jwtVerifier = new JWTVerifier(clientSecret, clientId);
        return jwtVerifier.verify(token);
    }
}