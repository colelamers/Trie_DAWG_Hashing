import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
     public static void main(String[] args) throws Exception {
         String epubFilePath = "/home/soren/eBooks/pg3069-images-3.epub";
         StringBuilder kBook = ReadBook.Read(epubFilePath);
         List<String> book = Arrays.asList(kBook.toString().split("\\."));
         //Processing.Test_DefaultHash();         // works
         //Processing.Test_CustomHash();          // works
         //Processing.Test_PerfectHash();         // works
         //Processing.Test_CuckooHash();
         // Processing.Build_DefaultTrie(book);     // works
         Processing.Build_CustomTrie(book);

         Processing.Build_PerfectTrie(book);
         // Processing.Build_CuckooTrie(book);
         // Processing.Build_PerfectDAWG(book);
         // Processing.Build_CuckooDAWG(book);
         // Processing.Build_DefaultDAWG(book);
         // Processing.Build_CustomDAWG(book);
         // todo 1; default dawg
         // todo 1; serialize default dawg

         // todo 1; custom trie
         // todo 1; custom dawg
         // todo 1; serialize custom hash
         // todo 1; serialize custom trie
         // todo 1; serialize custom dawg

         // todo 1; perfect trie
         // todo 1; perfect dawg
         // todo 1; serialize perfect hash
         // todo 1; serialize perfect trie
         // todo 1; serialize perfect dawg

         // todo 1; cuckoo hashmap
         // todo 1; cuckoo trie
         // todo 1; cuckoo dawg
         // todo 1; serialize cuckoo hash
         // todo 1; serialize cuckoo trie
         // todo 1; serialize cuckoo dawg

         // todo 1; setup test data to read in
         // todo 1; create tests
    }
}