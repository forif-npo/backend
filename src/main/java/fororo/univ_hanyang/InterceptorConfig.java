package fororo.univ_hanyang;

import fororo.univ_hanyang.jwt.RequireJWTIntercepter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final RequireJWTIntercepter requireJWTIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requireJWTIntercepter);
    }
}
