package pothole_solution.common.message.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class Base64MultipartFile implements MultipartFile {
    private byte[] content;
    private String name;
    private String originalFilename;
    private String contentType;

    public Base64MultipartFile(String base64String, String originalFilename, String contentType) {
        // Base64 문자열 디코딩
        this.content = Base64.getDecoder().decode(base64String);
        this.name = originalFilename;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(content);
            fos.flush();
        } catch (IOException e) {
            throw new IOException("파일 저장 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
