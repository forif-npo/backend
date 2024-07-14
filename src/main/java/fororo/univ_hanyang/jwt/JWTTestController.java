package fororo.univ_hanyang.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JWTTestController {
    @ResponseBody
    @PostMapping("/v1/debugJWT")
    public Map<String, Object> debugJWT(@RequestBody Map<String, Object> requestBody) {
        String token = (String)requestBody.get("token");
        JWTValidator jwtValidator = new JWTValidator();
        jwtValidator.validateToken(token);
        Map<String, Object> result = new HashMap<>(); result.put("detail", true);
        return result;
    }
}