package pothole_solution.detection.service.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import pothole_solution.detection.service.api.dto.CheckPotholeResponseDto;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class PotholeCheckApiService {

    private final RestTemplate restTemplate;

    @Value("${pothole-detection.server.uri}")
    private String baseUri;

    public Boolean isPothole(MultipartFile file) {
        ResponseEntity<CheckPotholeResponseDto> response = restTemplate.postForEntity(baseUri + "/check", getRequest(file), CheckPotholeResponseDto.class);

        CheckPotholeResponseDto responseBody = response.getBody();

        log.info("response : {}", responseBody.toString());
        return true;

//        return responseBody.getIsPothole();
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
