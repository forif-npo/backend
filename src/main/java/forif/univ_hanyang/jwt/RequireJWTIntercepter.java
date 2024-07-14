package forif.univ_hanyang.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequireJWTIntercepter implements HandlerInterceptor {
    private final JWTValidator jwtValidator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(RequireJWT.class) || handlerMethod.getBeanType().isAnnotationPresent(RequireJWT.class)) {
                String token = request.getHeader("Authorization");
                // JWT 토큰 검증 로직을 수행하고, 토큰이 유효하지 않다면 예외를 발생시킵니다.
                if (token == null){
                    return false;
                }
                jwtValidator.validateToken(token);
            }
        }
        return true;
    }
}