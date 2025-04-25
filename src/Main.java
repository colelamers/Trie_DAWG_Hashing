import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] gutenbergBooks = new String[] {
                "plv1.epub", "plv2.epub", "plv3.epub", "plv4.epub",
                "how12.epub", "how34.epub", "how56.epub", };
        //StringBuilder kBook = ReadBook.Read("/home/soren/eBooks/pg3069-images-3.epub");
        //List<String> book = Arrays.asList(kBook.toString().split("\\.")); // split at period
        List<String> books = new ArrayList<String>(); // split at period
        for (String aBook : gutenbergBooks){
            StringBuilder tBook = ReadBook.Read("Books/" + aBook);
            books.addAll(Arrays.asList(tBook.toString().split("\\.")));
        }

        System.out.printf("\n___ Build_DefaultHash ___\n");
        Processing.Build_DefaultHash(books);
        System.out.printf("\n___ Build_CustomHash ___\n");
        Processing.Build_CustomHash(books);
        System.out.printf("\n___ Build_CuckooHash ___\n");
        Processing.Build_CuckooHash(books);
        System.out.printf("\n___ Test_PerfectHash ___\n");
        Processing.Build_PerfectHash(books);

        System.out.printf("\n___ Build_DefaultTrie ___\n");
        Processing.Build_DefaultTrie(books);
        System.out.printf("\n___ Build_CustomTrie ___\n");
        Processing.Build_CustomTrie(books);
        System.out.printf("\n___ Build_CuckooTrie ___\n");
        //Processing.Build_CuckooTrie(book);// todo 1;
        System.out.printf("\n___ Build_PerfectTrie ___\n");
        Processing.Build_PerfectTrie(books);

        System.out.printf("\n___ Build_DefaultHashDAWG ___\n");
        Processing.Build_DefaultHashDAWG(books);
        System.out.printf("\n___ Build_CustomHashDAWG ___\n");
        Processing.Build_CustomHashDAWG(books);
        System.out.printf("\n___ Build_CuckooHashDAWG ___\n");
        //Processing.Build_CuckooHashDAWG(book);// todo 1;
        System.out.printf("\n___ Build_PerfectHashDAWG ___\n");
        Processing.Build_PerfectHashDAWG(books);

    }
}
