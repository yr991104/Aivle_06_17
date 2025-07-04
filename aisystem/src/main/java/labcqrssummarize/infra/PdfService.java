package labcqrssummarize.infra;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

@Service
public class PdfService {

    private String removeEmojis(String text) {
        return text.replaceAll("[\\p{So}\\p{Cn}]+", "");
    }
    
    public byte[] createPdfFromText(String title, String content) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, new File("src/main/resources/fonts/NanumGothic.ttf"));

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(font, 18);
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText(removeEmojis(title));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(50, 670);
                contentStream.showText(removeEmojis(content));
                contentStream.endText();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    public void savePdfFile(byte[] pdfBytes, String filePath) throws IOException {
        File pdfFile = new File(filePath);
        pdfFile.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            fos.write(pdfBytes);
        }
    }
}

