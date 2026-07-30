package africa.civicbridge.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Civic Bridge Africa API",
                version = "v1",
                description = "Governance-literacy platform backend: civic content, quizzes, forum, "
                        + "regional tracker, and auth. GET endpoints are public; write endpoints marked "
                        + "with a lock require a Bearer JWT from /api/auth/login or /api/auth/signup, "
                        + "and are further role-gated server-side (see README)."
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@Configuration
public class OpenApiConfig {
}
