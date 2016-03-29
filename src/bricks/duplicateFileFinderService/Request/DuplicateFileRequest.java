package bricks.duplicateFileFinderService.Request;

import java.util.Map;

public interface DuplicateFileRequest {
    public Map<String, String> getHashedFiles();
}
