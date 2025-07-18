package forif.univ_hanyang.common.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import forif.univ_hanyang.common.security.jwt.RequireJwtIntercepter;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private RequireJwtIntercepter jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**");
    }
}
