package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Statement {
    private UUID documentId;
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private String fileSize;
}
