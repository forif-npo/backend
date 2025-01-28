package forif.univ_hanyang.config;

import forif.univ_hanyang.jwt.RequireJwtIntercepter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final RequireJwtIntercepter requireJWTIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requireJWTIntercepter);
    }
}
