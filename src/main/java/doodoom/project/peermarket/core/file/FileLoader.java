package doodoom.project.peermarket.core.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileLoader {

    String upload(MultipartFile file);
}
