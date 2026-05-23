package junsang.cho.catchpro.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;


/**
 * Request 의 파라미터를 한번 읽으면 사라지는 InputStream 의 특성 때문에 Wrapper 로 감싸서 재사용 가능하게 하여 interceptor 에 넘김
 * 무조건 filter 에서 감싸서 넘겨야 한다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ServletLoggingFilter extends OncePerRequestFilter {

    private final MultipartResolver multipartResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //health check 통과
        if ("/".equals(request.getServletPath())){
            filterChain.doFilter(request, response);
            return;
        }

        log.info("======================= Request Log =======================");
        log.info("요청 URL: {} {}", request.getMethod(), request.getRequestURL());

        if (multipartResolver.isMultipart(request)) {
            //멀티 파트의 경우 wrapper 가 아닌 resolve를 통해 request 를 변환해줌
            MultipartHttpServletRequest multipartRequest = multipartResolver.resolveMultipart(request);
            multipartRequest.getFileMap().forEach(this::logFileDetails);

            filterChain.doFilter(multipartRequest, response);

        } else {
            // wrapper 로 감싸서 stream을 열어도 데이터를 잃어버리지 않도록 함.
            HttpServletRequestWrapper wrappedRequest = new RequestServletWrapper(request);

            try {
                if (Objects.equals(request.getMethod(), "POST")) {
                    String contentType = request.getContentType();
                    if(contentType != null && contentType.startsWith("application/json")){
                        Map<String, Object> inputMap = new ObjectMapper().readValue(wrappedRequest.getInputStream(), Map.class);
                        log.info("요청 정보: " + inputMap);
                    }else{
                        log.warn("JSON이 아닌 다른 Content-Type: " + contentType);
                    }
                } else {
                    log.info("요청 정보: " + request.getQueryString());
                }
            } catch (MismatchedInputException ignored) {
                // 불필요한 파라미터를 요청하였을때 에러를 발생시키지 않음
            } catch (Exception e) {
                e.printStackTrace();
            }

            filterChain.doFilter(wrappedRequest, response);
        }
    }

    private void logFileDetails(String name, MultipartFile file) {
        // 로그에 파일 정보 기록
        log.info("name : {}", name);
        log.info("컨텐츠 타입 : {}", file.getContentType());
        log.info("파일명 : {}", file.getOriginalFilename());
        log.info("파일 크기(size) : {}", file.getSize());
    }
}