package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Sahil Gupta
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }


    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation permutation, Alphabet alphabet) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, permutation.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, permutation.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, permutation.invert(e));
            int ci = alphabet.toInt(c), ei = alphabet.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, permutation.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, permutation.invert(ei));
        }
    }


    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testConstructor() {
        Permutation p1 = new Permutation("(ABC) (DEF) (GHI)",
                new Alphabet(UPPER_STRING));
        try {
            Permutation p2 = new Permutation("(AB)C) (DEF) (GHI)",
                    new Alphabet(UPPER_STRING));
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }
        try {
            Permutation p3 = new Permutation("(()) (#-) (34)",
                    new Alphabet("()#)"));
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }
        try {
            Permutation p4 = new Permutation("(12345) (DEF) (GHI)",
                    new Alphabet(UPPER_STRING));
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }

        Permutation p5 = new Permutation("(ABCZXYT) (SNMDEF) (LKJGHI)",
                new Alphabet(UPPER_STRING));

        Permutation p6 = new Permutation("(1256) (\"97[|)",
                new Alphabet("123456789~`!@#$%^&_-+={}[]|'\""));

        Permutation p7 = new Permutation("(ABC) (DEF) (1256) (\"97[|) ()",
                new Alphabet(UPPER_STRING + "123456789~`!@#$%^&_-+={}[]|'\""));
    }

    @Test
    public void testPermuteInvert() {
        Permutation p1 = new Permutation("(ABC) (DEF) (GHI)",
                new Alphabet(UPPER_STRING));
        String from1 = UPPER_STRING;
        String to1 = "BCAEFDHIGJKLMNOPQRSTUVWXYZ";

        Permutation p2 = new Permutation("(ABCZXYT) (SNMDEF) (LKJGHI)",
                new Alphabet(UPPER_STRING));
        String from2 = UPPER_STRING;
        String to2 = "BCZEFSHILGJKDMOPQRNAUVWYTX";

        Permutation p3 = new Permutation("(1256) (\"97[|)",
                new Alphabet("123456789~`!@#$%^&_-+={}[]|'\""));
        String from3 = "123456789~`!@#$%^&_-+={}[]|'\"";
        String to3 = "253461[87~`!@#$%^&_-+={}|]\"'9";

        Permutation p4 = new Permutation("(ABC) (DEF) (1256) (\"97[|) ()",
                new Alphabet(UPPER_STRING + "123456789~`!@#$%^&_-+={}[]|'\""));
        String from4 = UPPER_STRING + "123456789~`!@#$%^&_-+={}[]|'\"";
        String to4 = "BCAEFDGHIJKLMNOPQRSTUVWXYZ" + to3;

        checkPerm("I", from1, to1, p1, p1.alphabet());
        checkPerm("II", from2, to2, p2, p2.alphabet());
        checkPerm("III", from3, to3, p3, p3.alphabet());
        checkPerm("IV", from4, to4, p4, p4.alphabet());
    }

    @Test
    public void testDerangement() {
        Permutation p1 = new Permutation("(ABC) (DEF) (GHI)",
                new Alphabet(UPPER_STRING));

        Permutation p2 = new Permutation("(ABCZXYT) (SNMDEF) (LKJGHI)",
                new Alphabet(UPPER_STRING));

        Permutation p3 = new Permutation("(1256) (\"97[|)",
                new Alphabet("123456789~`!@#$%^&_-+={}[]|'\""));

        Permutation p4 = new Permutation("(ABC) (DEF) (1256) (\"97[|) ()",
                new Alphabet(UPPER_STRING + "123456789~`!@#$%^&_-+={}[]|'\""));

        Permutation p5 = new Permutation("(AELTPHQXRU) "
               + "(BKNW) (CMOY) (DFG) (IV) (JZS)",
                new Alphabet(UPPER_STRING));

        Permutation p6 = new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)",
                new Alphabet(UPPER_STRING));

        Permutation p7 = new Permutation("(23456789~`!@#$%^&_-+={}[]|'\"1)",
                new Alphabet("123456789~`!@#$%^&_-+={}[]|'\""));

        Permutation p8 = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
               + "(DFG) (IV) (JZS) ()",
                new Alphabet(UPPER_STRING));

        assertFalse(p1.derangement());
        assertFalse(p2.derangement());
        assertFalse(p3.derangement());
        assertFalse(p4.derangement());

        assertTrue(p5.derangement());
        assertTrue(p6.derangement());
        assertTrue(p7.derangement());
        assertTrue(p8.derangement());
    }



}
