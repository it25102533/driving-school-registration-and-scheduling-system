package lk.ac.sliit.drivingschool.drivingschoolsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StudentAuthInterceptor studentAuthInterceptor;
    private final InstructorAuthInterceptor instructorAuthInterceptor;

    public WebConfig(StudentAuthInterceptor studentAuthInterceptor,
                     InstructorAuthInterceptor instructorAuthInterceptor) {
        this.studentAuthInterceptor = studentAuthInterceptor;
        this.instructorAuthInterceptor = instructorAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(studentAuthInterceptor)
                .addPathPatterns("/student/**")
                .excludePathPatterns(
                        "/login",
                        "/register",
                        "/saveStudent",
                        "/student/subsystem.css",
                        "/js/**",
                        "/images/**",
                        "/css/**"
                );

        registry.addInterceptor(instructorAuthInterceptor)
                .addPathPatterns(
                        "/instructors/**",
                        "/instructors",
                        "/instructor/**",
                        "/progress/**",
                        "/students",
                        "/editStudent",
                        "/deleteStudent",
                        "/updateStudent",
                        "/vehicles/**",
                        "/vehicles"
                )
                .excludePathPatterns(
                        "/login/professional",
                        "/logout/professional",
                        "/js/**",
                        "/images/**",
                        "/css/**"
                );
    }
}