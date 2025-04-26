import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    static String[] PLUTARCH = new String[] {
            // Plutarch's Lives, Procopius's History of Wars
            "plv1.epub", "plv2.epub", "plv3.epub", "plv4.epub"
    };

    static String[] PROCOPIUS = new String[] {
            // Procopius's History of Wars
            "how12.epub", "how34.epub", "how56.epub",
    };

    static String[] PLINY = new String[] {
            // Pliny's Natural History
            "nhop1.epub", "nhop2.epub", "nhop3.epub",
            "nhop4.epub", "nhop5.epub", "nhop6.epub",
    };

    static String[] KING_JAMES = new String[] {
            // King James Bible
            "kjb.epub"
    };

    public static void ProcessTests(List<String> books, String whichBookSet) throws Exception {

        Processing p = new Processing(whichBookSet);
        // Hash Function Tests
        System.out.printf("\n___ Default Hash - %s ___\n", whichBookSet);
        p.Build_DefaultHash(books);
        System.out.printf("\n___ Custom Hash - %s ___\n", whichBookSet);
        p.Build_CustomHash(books);
        System.out.printf("\n___ Cuckoo Hash - %s ___\n", whichBookSet);
        p.Build_CuckooHash(books);
        System.out.printf("\n___ Perfect Hash - %s ___\n", whichBookSet);
        p.Build_PerfectHash(books);

        // Trie Tests
        System.out.printf("\n___ Default Trie - %s ___\n", whichBookSet);
        p.Build_DefaultTrie(books);
        System.out.printf("\n___ Custom Trie - %s ___\n", whichBookSet);
        p.Build_CustomTrie(books);
        System.out.printf("\n___ Cuckoo Trie - %s ___\n", whichBookSet);
        p.Build_CuckooTrie(books);
        System.out.printf("\n___ Perfect Trie - %s ___\n", whichBookSet);
        p.Build_PerfectTrie(books);

        // DAWG Tests
        System.out.printf("\n___ Default Hash DAWG - %s ___\n", whichBookSet);
        p.Build_DefaultHashDAWG(books);
        System.out.printf("\n___ Custom Hash DAWG - %s ___\n", whichBookSet);
        p.Build_CustomHashDAWG(books);
        System.out.printf("\n___ Cuckoo Hash DAWG - %s ___\n", whichBookSet);
        p.Build_CuckooHashDAWG(books);
        System.out.printf("\n___ Perfect Hash DAWG - %s ___\n", whichBookSet);
        p.Build_PerfectHashDAWG(books);
    }

    public static void main(String[] args) throws Exception {
        List<String> globalBookSet = new ArrayList<>();

        processSet(PLUTARCH, "Plutarch", globalBookSet);
        processSet(PROCOPIUS, "Procopius", globalBookSet);
        processSet(PLINY, "Pliny", globalBookSet);
        processSet(KING_JAMES, "King-James", globalBookSet);

        // Now process the combined global set
        ProcessTests(globalBookSet, "AllBooks");
    }

    private static void processSet(String[] bookFiles, String setName, List<String> globalBookSet) throws Exception {
        List<String> bookSet = new ArrayList<>();

        for (String aBook : bookFiles) {
            StringBuilder tBook = ReadBook.Read("Books/" + aBook);
            List<String> bookLines = Arrays.asList(tBook.toString().split("\\."));
            bookSet.addAll(bookLines);        // Add to current set
            globalBookSet.addAll(bookLines);  // Also add to global set
        }

        ProcessTests(bookSet, setName);
    }


}
