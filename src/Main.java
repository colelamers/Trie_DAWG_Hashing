import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String epubFilePath = "/home/soren/eBooks/pg3069-images-3.epub";
        StringBuilder kBook = ReadBook.Read(epubFilePath);
        List<String> book = Arrays.asList(kBook.toString().split("\\."));

        System.out.printf("\n___ Build_DefaultHash ___\n");
        Processing.Build_DefaultHash(book);
        System.out.printf("\n___ Build_CustomHash ___\n");
        Processing.Build_CustomHash(book);
        System.out.printf("\n___ Build_CuckooHash ___\n");
        Processing.Build_CuckooHash(book);
        System.out.printf("\n___ Test_PerfectHash ___\n");
        //Processing.Test_PerfectHash(book);// todo 1;

        System.out.printf("\n___ Build_DefaultTrie ___\n");
        Processing.Build_DefaultTrie(book);
        System.out.printf("\n___ Build_CustomTrie ___\n");
        Processing.Build_CustomTrie(book);
        System.out.printf("\n___ Build_CuckooTrie ___\n");
        //Processing.Build_CuckooTrie(book);// todo 1;
        System.out.printf("\n___ Build_PerfectTrie ___\n");
        Processing.Build_PerfectTrie(book);

        System.out.printf("\n___ Build_DefaultHashDAWG ___\n");
        Processing.Build_DefaultHashDAWG(book);
        System.out.printf("\n___ Build_CustomHashDAWG ___\n");
        Processing.Build_CustomHashDAWG(book);
        System.out.printf("\n___ Build_CuckooHashDAWG ___\n");
        //Processing.Build_CuckooHashDAWG(book);// todo 1;
        System.out.printf("\n___ Build_PerfectHashDAWG ___\n");
        Processing.Build_PerfectHashDAWG(book);

    }
}
