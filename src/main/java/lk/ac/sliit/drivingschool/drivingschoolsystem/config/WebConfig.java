package lk.ac.sliit.drivingschool.drivingschoolsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StudentAuthInterceptor studentAuthInterceptor;

    public WebConfig(StudentAuthInterceptor studentAuthInterceptor) {
        this.studentAuthInterceptor = studentAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(studentAuthInterceptor)
                .addPathPatterns("/student/**")
                .excludePathPatterns(
                        "/login",
                        "/register",
                        "/saveStudent",
                        "/student/subsystem.css", // Path for your CSS file
                        "/js/**",
                        "/images/**",
                        "/css/**"
                );
    }
}