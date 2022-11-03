package doodoom.project.peermarket.core.file;

import doodoom.project.peermarket.exception.file.FileLoadException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiskFileLoaderTest {
    private final String dirPath = "/Users/choiyounghoon/Desktop/study/peer-market/src/test/resources/static/img";
    private final FileLoader fileLoader = new DiskFileLoader(dirPath);

    @Test
    public void uploadFailure() {
        //given
        FileLoader failFileLoader = new DiskFileLoader("anywhere");
        MultipartFile file = new MockMultipartFile("file",
                "test.txt", "text/plain",
                "test file".getBytes(StandardCharsets.UTF_8));

        //when
        //then
        assertThrows(FileLoadException.class,
                () -> failFileLoader.upload(file));
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
        reader.close();

        removeFile(fileName);
    }

    private void removeFile(String fileName) {
        File file = new File(dirPath + File.separator + fileName);
        System.gc();
        file.delete();
    }
}