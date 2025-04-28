import io.documentnode.epub4j.domain.*;
import io.documentnode.epub4j.epub.EpubReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadBook {
    // https://github.com/documentnode/epub4j
    public static StringBuilder Read(String path){
        StringBuilder bookAsString = new StringBuilder();
        try {
            FileInputStream inputStream = new FileInputStream(path);
            EpubReader epubReader = new EpubReader();
            Book book = epubReader.readEpub(inputStream);
            for (SpineReference spineReference : book.getSpine().getSpineReferences()) {
                Resource resource = spineReference.getResource();
                try (InputStream resourceStream = resource.getInputStream()) {
                    String htmlContent = new String(resourceStream.readAllBytes(), StandardCharsets.UTF_8);
                    String text = removeHtmlTags(htmlContent);
                    text = text.replaceAll("\\u00A0", " ");
                    bookAsString.append(text);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookAsString;
    }

    private static String removeHtmlTags(String html) {
        String textWithoutTags = html.replaceAll("<[^>]*>", "");
        String twt = textWithoutTags.replaceAll("\\n", " ");
        twt = twt.replaceAll("\\s+", " ").trim();
        Pattern pattern = Pattern.compile("<[^>]*>");
        Matcher matcher = pattern.matcher(twt);
        return matcher.replaceAll("");
    }
}
