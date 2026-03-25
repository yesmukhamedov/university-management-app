package kz.iitu.hello.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final String FALLBACK_PATH = "/users";

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        String referer = request.getHeader("Referer");
        String targetPath = FALLBACK_PATH;

        if (referer != null && !referer.isBlank()) {
            try {
                URI refererUri = URI.create(referer);
                if (refererUri.getPath() != null && !refererUri.getPath().isBlank()) {
                    targetPath = refererUri.getPath();
                }
            } catch (IllegalArgumentException ignored) {
                if (referer.startsWith("/") && !referer.startsWith("//")) {
                    targetPath = referer;
                }
            }
        }

        if (!targetPath.startsWith("/")) {
            targetPath = FALLBACK_PATH;
        }

        String redirectUrl = UriComponentsBuilder
                .fromPath(targetPath)
                .replaceQueryParam("error", "access_denied")
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
