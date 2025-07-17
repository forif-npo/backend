package forif.univ_hanyang.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import forif.univ_hanyang.common.security.jwt.RequireJwtIntercepter;

@RequiredArgsConstructor
@Configuration  
public class InterceptorConfig implements WebMvcConfigurer {

    private final RequireJwtIntercepter requireJWTIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requireJWTIntercepter);
    }
}
