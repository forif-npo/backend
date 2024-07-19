package forif.univ_hanyang.jwt;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 3600_000; // 1 hour
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 30 * 24 * 60 * 60 * 1000L; // 30 days
    private static final SecretKey key = Jwts.SIG.HS256.key().build(); // HS256 알고리즘에 사용할 SecretKey 생성
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // 액세스 토큰 발급
    public String generateAccessToken(String userId) {
        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // 리프레시 토큰 발급
    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            logger.error("토큰 유효성 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    // 토큰에서 사용자명 가져오기
    public String getUserIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    public static boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}
