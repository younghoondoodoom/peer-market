package doodoom.project.peermarket.core.file;

import doodoom.project.peermarket.exception.file.FileLoadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class DiskFileLoader implements FileLoader {

    @Value("${spring.servlet.multipart.location}")
    private String dirPath;

    @Override
    public String upload(MultipartFile file) {
        String uuid =
                UUID.randomUUID().toString().replace("/", "").replace(".",
                        "").replace("\\", "");
        String fileName =
                uuid + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path path = Paths.get(dirPath + File.separator + fileName);

        try {
            Files.copy(file.getInputStream(), path,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.warn("Could not upload file");
            throw new FileLoadException();
        }
        return fileName;
    }
}
