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
                description = """
                        Governance-literacy platform backend: civic content, quizzes, forum, regional \
                        tracker, and auth.

                        ### How to call a protected (🔒) endpoint from this page

                        1. Open the **Auth** section below and try `POST /api/auth/login`. Click \
                        "Try it out", use one of the seeded demo accounts \
                        (e.g. `admin@civicbridge.africa` / `admin123` for Admin, or \
                        `youth@civicbridge.africa` / `youth123` for Youth User), and execute.
                        2. Copy the `token` value from the response body (a long string starting \
                        with `eyJ...`).
                        3. Click the green **Authorize** button at the top of this page, paste \
                        `Bearer <token>` (include the word "Bearer" and a space before the token), \
                        and click Authorize.
                        4. Every 🔒 endpoint you now call will send that token automatically. Try \
                        `POST /api/forum` (works for any logged-in role) or, using the Admin \
                        token, `POST /api/tracker`.

                        A token is valid for 24 hours. Using a token whose role doesn't match an \
                        endpoint's requirement returns 403; missing/expired/invalid tokens return 401.
                        """
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
