package kz.iitu.hello.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final String FALLBACK_PATH = "/users";

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String referer = request.getHeader("Referer");
        String targetPath = FALLBACK_PATH;

        if (referer != null && !referer.isBlank()) {
            String contextPath = request.getContextPath() == null ? "" : request.getContextPath();

            if (referer.startsWith("/") && !referer.startsWith("//")) {
                targetPath = referer;
            } else if (!contextPath.isBlank() && referer.contains(contextPath + "/")) {
                int startIndex = referer.indexOf(contextPath + "/");
                targetPath = referer.substring(startIndex + contextPath.length());
            }
        }

        if (targetPath == null || targetPath.isBlank() || !targetPath.startsWith("/")) {
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
