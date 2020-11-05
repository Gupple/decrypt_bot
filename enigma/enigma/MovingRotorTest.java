package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Sahil Gupta
 */
public class MovingRotorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)",
                    ci, c),
                         ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                         ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                                notches);
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
    }

    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        checkRotor("Rotor I advanced", UPPER_STRING, NAVALB_MAP.get("I"));
    }

    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA, "");
        rotor.set(25);
        checkRotor("Rotor I set", UPPER_STRING, NAVALZ_MAP.get("I"));
    }


    @Test
    public void testComprehensive() {
        Alphabet a1 = new Alphabet(UPPER_STRING);
        Permutation p1 = new Permutation("(ABC) (DEF) (GHI)",
                new Alphabet(UPPER_STRING));
        MovingRotor m1  = new MovingRotor("I", p1, "DEZ");
        try {
            MovingRotor m2  = new MovingRotor("I", p1, "1");
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }

        Permutation p2 = new Permutation("(ABCZXYT) (SNMDEF) (LKJGHI)",
                new Alphabet(UPPER_STRING));

        Permutation p3 = new Permutation("(1256) (\"97[|)",
                new Alphabet("123456789~`!@#$%^&_-+={}[]|'\""));

        Permutation p4 = new Permutation("(ABC) (DEF) (1256) (\"97[|) ()",
                new Alphabet(UPPER_STRING
                        + "123456789~`!@#$%^&_-+={}[]|'\""));

        MovingRotor m3 = new MovingRotor("II", p2, "X");
        MovingRotor m5 = new MovingRotor("IV", p4, "X-_");

        for (int i = 0; i < m1.size() * 2; i += 1) {
            assertEquals(i % m1.size(), m1.setting());

            if (i % m1.size() == 25 || i % m1.size() == 3
                    || i % m1.size() == 4) {
                assertTrue(m1.atNotch());
            } else {
                assertFalse(m1.atNotch());
            }
            m1.advance();
        }
        for (int j = 0; j < m3.size() * 2; j += 1) {
            assertEquals(j % m3.size(), m3.setting());
            if (j % m3.size() == 23) {
                assertTrue(m3.atNotch());
            } else {
                assertFalse(m3.atNotch());
            }
            m3.advance();
        }

        for (int j = 0; j < m5.size() * 2; j += 1) {
            assertEquals(j % m5.size(), m5.setting());
            if (j % m5.size() == 23 || j % m5.size() == 45
                    || j % m5.size() == 44) {
                assertTrue(m5.atNotch());
            } else {
                assertFalse(m5.atNotch());
            }
            m5.advance();
        }
    }

}
