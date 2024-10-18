package pothole_solution.detection.service.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import pothole_solution.detection.service.api.dto.CheckPotholeResponseDto;

import java.io.IOException;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class PotholeCheckApiService {

    private final RestTemplate restTemplate;

    @Value("${pothole-detection.server.url}")
    private String baseUrl;

    public boolean isPothole(MultipartFile file) {
        ResponseEntity<CheckPotholeResponseDto> response = restTemplate.postForEntity(baseUrl + "/check", getRequest(file), CheckPotholeResponseDto.class);

        CheckPotholeResponseDto responseBody = response.getBody();

        return Objects.requireNonNull(responseBody).getIsPothole();
    }

    private HttpEntity<MultiValueMap<String, Object>> getRequest(MultipartFile file) {
        // 요청 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource fileResource = null;
        try {
            fileResource = getFileResource(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 요청 본문 구성
        MultiValueMap<String, Object> reqBody = new LinkedMultiValueMap<>();
        reqBody.add("file", fileResource);

        return new HttpEntity<>(reqBody, headers);
    }

    private ByteArrayResource getFileResource(MultipartFile file) throws IOException {
        return new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getName();
            }
        };
    }

}
