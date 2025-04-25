import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String epubFilePath = "/home/soren/eBooks/pg3069-images-3.epub";
        StringBuilder kBook = ReadBook.Read(epubFilePath);
        List<String> book = Arrays.asList(kBook.toString().split("\\."));

        System.out.printf("Test_DefaultHash: ");
        Processing.Test_DefaultHash(book);
        System.out.printf("Test_CustomHash: ");
        Processing.Test_CustomHash(book);
        System.out.printf("Test_CuckooHash: ");
        Processing.Test_CuckooHash(book);
        //System.out.printf("Test_PerfectHash: "); // todo 1;
        //Processing.Test_PerfectHash(book);

        System.out.printf("Build_DefaultTrie: ");
        Processing.Build_DefaultTrie(book);
        System.out.printf("Build_CustomTrie: ");
        Processing.Build_CustomTrie(book);
        //System.out.printf("Build_CuckooTrie: "); // todo 1;
        //Processing.Build_CuckooTrie(book);
        System.out.printf("Build_PerfectTrie: ");
        Processing.Build_PerfectTrie(book);

        System.out.printf("Build_DefaultHashDAWG: ");
        Processing.Build_DefaultHashDAWG(book);
        System.out.printf("Build_CustomHashDAWG: ");
        Processing.Build_CustomHashDAWG(book);
        //System.out.printf("Build_CuckooHashDAWG: "); // todo 1;
        //Processing.Build_CuckooHashDAWG(book);
        //System.out.printf("Build_PerfectHashDAWG: "); // todo 1; divide by zero
        //Processing.Build_PerfectHashDAWG(book);

    }
}
