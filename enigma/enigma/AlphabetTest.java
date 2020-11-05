package enigma;

import org.junit.Test;

import static org.junit.Assert.*;

public class AlphabetTest {
    @Test
    public void testConstructor() {
        try {
            Alphabet hello = new Alphabet("HELLO");
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }
        Alphabet t1 = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Alphabet t2 = new Alphabet("123456789");

        try {
            Alphabet t3 = new Alphabet("!@#$%^&*()_-+=~`?<>");
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }

        Alphabet t4 = new Alphabet("ABC123<>?");

    }

    @Test
    public void testContains() {
        assertTrue(test1.contains('Z'));
        assertFalse(test1.contains('1'));
        assertTrue(test2.contains('8'));
        assertFalse(test2.contains('?'));
        assertTrue(test3.contains('@'));
        assertFalse(test3.contains('6'));
        assertTrue(test4.contains('<'));
        assertFalse(test4.contains('D'));
    }

    @Test
    public void testTo() {
        testLoop(test1); testLoop(test2);
        testLoop(test3); testLoop(test4);

        try {
            test1.toInt('1');
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }

        try {
            test2.toChar(12);
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }
    }

    public void testLoop(Alphabet alpha) {
        for (int i = 0; i < alpha.size(); i += 1) {
            assertEquals(alpha.toInt(alpha.toChar(i)), i);
        }
    }

    public static void main(String[] args) {

    }

    final Alphabet test1 = new Alphabet();
    final Alphabet test2 = new Alphabet("123456789");
    final Alphabet test3 = new Alphabet("!@#$%^&_-+=~`?<>");
    final Alphabet test4 = new Alphabet("ABC123<>?");
}
