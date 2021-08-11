package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;

@Data
public class Statement {
    private Long documentId;
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private String fileSize;
}
