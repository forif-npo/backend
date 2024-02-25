package fororo.univ_hanyang.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;



@Component
public class JWTValidator {


    public String[] validateToken(String token) {

        String[] s = new String[2];
        try {
            // 토큰의 페이로드 추출
            String[] parts = token.split("\\.");
            String decodedPayload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));

            // Jackson ObjectMapper 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 문자열을 JsonNode로 파싱
            JsonNode jsonNode = objectMapper.readTree(decodedPayload);

            // "email" 필드 값 추출
            String email = jsonNode.get("email").asText();
            String picture = jsonNode.get("picture").asText();
            s[0] = email;
            s[1] = picture;
            // 추출한 정보 출력
            System.out.println("Email: " + email);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }


}
