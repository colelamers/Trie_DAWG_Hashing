import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static String[] MARK_TWAIN = new String[] {
            // Plutarch's Lives, Procopius's History of Wars
            "mt.epub"
    };
    static String[] PLUTARCH = new String[] {
            // Plutarch's Lives, Procopius's History of Wars
            "plv1.epub", "plv2.epub", "plv3.epub", "plv4.epub"
    };

    static String[] PROCOPIUS = new String[] {
            // Procopius's History of Wars
            "how12.epub", "how34.epub", "how56.epub",
    };

    static String[] KING_JAMES = new String[] {
            // King-James Bible
            "kjb.epub",
    };

    static String[] PLINY = new String[] {
            // Pliny's Natural History
            "nhop1.epub", "nhop2.epub", "nhop3.epub",
            "nhop4.epub", "nhop5.epub", "nhop6.epub",
    };

    public static void ProcessTests(List<String> books, String whichBookSet) throws Exception {

        Processing p = new Processing(whichBookSet);
        System.out.print("\n==================== " + whichBookSet + " - Default" + " ==========================");
        System.out.print("\n___ Hash ___\n");
        p.Build_DefaultHash(books);
        System.out.print("\n___ Trie ___\n");
        p.Build_DefaultTrie(books);
        System.out.print("\n___ DAWG ___\n");
        p.Build_DefaultHashDAWG(books);

        System.out.print("\n==================== " + whichBookSet + " - Custom" + " ==========================");
        System.out.print("\n___ Hash ___\n");
        p.Build_CustomHash(books);
        System.out.print("\n___ Trie ___\n");
        p.Build_CustomTrie(books);
        System.out.print("\n___ DAWG ___\n");
        p.Build_CustomHashDAWG(books);

        System.out.print("\n==================== " + whichBookSet + " - Cuckoo" + " ==========================");
        System.out.print("\n___ Hash ___\n");
        p.Build_CuckooHash(books);
        System.out.print("\n___ Trie ___\n");
        p.Build_CuckooTrie(books);
        System.out.print("\n___ DAWG ___\n");
        p.Build_CuckooHashDAWG(books);

        System.out.print("\n==================== " + whichBookSet + " - Perfect" + " ==========================");
        System.out.print("\n___ Hash ___\n");
        p.Build_PerfectHash(books);
        System.out.print("\n___ Trie ___\n");
        p.Build_PerfectTrie(books);
        System.out.print("\n___ DAWG ___\n");
        p.Build_PerfectHashDAWG(books);
    }

    public static void main(String[] args) throws Exception {
        List<String> globalBookSet = new ArrayList<>();
        processSet(MARK_TWAIN, "Mark-Twain", globalBookSet);
        processSet(PROCOPIUS, "Procopius", globalBookSet);
        processSet(PLUTARCH, "Plutarch", globalBookSet);
        processSet(PLINY, "Pliny", globalBookSet);
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