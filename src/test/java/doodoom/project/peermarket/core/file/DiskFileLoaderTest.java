package doodoom.project.peermarket.core.file;

import doodoom.project.peermarket.exception.file.FileLoadException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DiskFileLoaderTest {

    @Autowired
    private FileLoader fileLoader;
    @Value("${spring.servlet.multipart.location}")
    private String dirPath;

    @Test
    public void uploadFailure() {
        //given
        MultipartFile file = new MockMultipartFile("file",
                "test.txt", "image/jpg",
                "test file".getBytes(StandardCharsets.UTF_8));

        //when
        //then
        assertThrows(FileLoadException.class,
                () -> fileLoader.upload(file));
    }

    @Test
    public void uploadSuccess() throws Exception {
        //given
        String originalFilename = "test.txt";
        String content = "test file";
        MultipartFile file = new MockMultipartFile("file",
                originalFilename, "text/plain",
                content.getBytes(StandardCharsets.UTF_8));
        //when
        String fileName = fileLoader.upload(file);

        //then
        assertThat(fileName).isNotNull();
        assertThat(fileName.contains(originalFilename)).isTrue();

        BufferedReader reader = new BufferedReader(
                new FileReader(dirPath + File.separator + fileName));

        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }
        assertThat(sb.toString()).isEqualTo(content);

        removeFile(fileName);
    }

    private static void removeFile(String fileName) throws InterruptedException {
        File file = new File(fileName);
        for (int i = 0; i < 100; i++) {
            if (file.delete()) {
                break;
            } else {
                Thread.sleep(1000);
            }
        }
    }
}