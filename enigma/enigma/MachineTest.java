package enigma;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class MachineTest {

    @Test
    public void testConstructor() {
        Alphabet alpha = new Alphabet("ABC");
        Permutation p1 = new Permutation("(ABC)", alpha);
        ArrayList<Rotor> rotorSet = new ArrayList<>();
        rotorSet.add(new Reflector("Beta", p1));
        rotorSet.add(new MovingRotor("III", p1, "C"));
        rotorSet.add(new MovingRotor("II", p1, "C"));
        rotorSet.add(new FixedRotor("I", p1));

        Machine machine1 = new Machine(alpha, 4, 2, rotorSet);

        try {
            Machine machine2 = new Machine(alpha, 0, 0, new ArrayList<>());
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }
        try {
            ArrayList<Rotor> rotorSet2 = new ArrayList<>();
            rotorSet2.add(new MovingRotor("III", p1, "C"));
            rotorSet2.add(new MovingRotor("II", p1, "C"));
            Machine machine3 = new Machine(alpha, 2, 2, rotorSet2);
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }

        try {
            ArrayList<Rotor> rotorSet3 = new ArrayList<>();
            rotorSet3.add(new FixedRotor("Hi", p1));
            Machine machine3 = new Machine(alpha, 1, 0, rotorSet3);
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }

        ArrayList<Rotor> rotorSet4 = new ArrayList<>();
        rotorSet4.add(new Reflector("Beta", p1));
        rotorSet4.add(new FixedRotor("Gamma", p1));
        Machine machine4 = new Machine(alpha, 2, 0, rotorSet4);
    }

    @Test
    public void testConvert() {
        Permutation p1 = new Permutation(TestUtils.NAVALA
                .get("B"), TestUtils.UPPER);
        Permutation p2 = new Permutation(TestUtils.NAVALA
                .get("Beta"), TestUtils.UPPER);
        Permutation p3 = new Permutation(TestUtils.NAVALA
                .get("III"), TestUtils.UPPER);
        Permutation p4 = new Permutation(TestUtils.NAVALA
                .get("IV"), TestUtils.UPPER);
        Permutation p5 = new Permutation(TestUtils.NAVALA
                .get("I"), TestUtils.UPPER);

        ArrayList<Rotor> rotorSet = new ArrayList<>();
        rotorSet.add(new Reflector("B", p1));
        rotorSet.add(new FixedRotor("Beta", p2));
        rotorSet.add(new MovingRotor("III", p3, "V"));
        rotorSet.add(new MovingRotor("IV", p4, "J"));
        rotorSet.add(new MovingRotor("I", p5, "Q"));

        Machine machine1 = new Machine(TestUtils.UPPER,
                5, 3, rotorSet);
        machine1.insertRotors(new String[]{"B", "Beta", "III", "IV", "I"});
        machine1.setPlugboard(new Permutation("(HQ) (EX) (IP) "
               + "(TR) (BY)", TestUtils.UPPER));
        machine1.setRotors("AXLE");


        String message = "FROMHISSHOULDERHIAWATHATOOKTHECAMERAOFROSEWOOD"
               + "MADEOFSLIDINGFOLDINGROSEWOODNEATLYPUTITALLTOGETHERINITSCASE"
               + "ITLAYCOMPACTLYFOLDEDINTONEARLYNOTHINGBUTHEOPENEDOUTTHEHINGES"
               + "PUSHEDANDPULLEDTHEJOINTSANDHINGESTILLITLOOKEDALLSQUARESAND"
               + "OBLONGSLIKEACOMPLICATEDFIGUREINTHESECONDBOOKOFEUCLID";
        String encryption = machine1.convert(message);
        machine1.setRotors("AXLE");
        assertEquals(message, machine1.convert(encryption));
    }

    @Test
    public void testInsertRotors() {
        Alphabet alpha = new Alphabet("ABC");
        Permutation p1 = new Permutation("(ABC)", alpha);
        ArrayList<Rotor> rotorSet = new ArrayList<>();
        rotorSet.add(new Reflector("Beta", p1));
        rotorSet.add(new MovingRotor("III", p1, "C"));
        rotorSet.add(new MovingRotor("II", p1, "C"));
        rotorSet.add(new FixedRotor("I", p1));

        Machine machine1 = new Machine(alpha, 4, 2, rotorSet);
        try {
            machine1.insertRotors(new String[]{"III", "II", "Beta", "I"});
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }
        try {
            machine1.insertRotors(new String[]{"Beta", "II", "Beta", "I"});
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }
        machine1.insertRotors(new String[] {"Beta", "II", "III", "I"});
        try {
            machine1.insertRotors(new String[]{"Beta", "II"});
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSetRotors() {
        Permutation p1 = new Permutation("(AE) (BN) (CK) (DQ) (FU) "
               + "(GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", TestUtils.UPPER);
        ArrayList<Rotor> rotorSet = new ArrayList<>();
        rotorSet.add(new Reflector("Beta", p1));
        rotorSet.add(new FixedRotor("III", p1));
        rotorSet.add(new MovingRotor("II", p1, "C"));
        rotorSet.add(new MovingRotor("I", p1, "C"));

        Machine machine1 = new Machine(TestUtils.UPPER, 4, 2, rotorSet);
        machine1.insertRotors(new String[] {"Beta", "III", "II", "I"});
        try {
            machine1.setRotors("BBBB");
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }
        try {
            machine1.setRotors("BBB");
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }
        for (int i = 0; i < Math.pow(26, 2); i += 1) {
            char firstChar = TestUtils.UPPER.toChar(i / 26),
                    secondChar = TestUtils.UPPER.toChar(i % 26);
            String expectedSetting = "A" + firstChar + secondChar;
            machine1.setRotors(expectedSetting);
            String actualSetting = "";
            for (Rotor rotor: rotorSet) {
                actualSetting += TestUtils.UPPER.toChar(rotor.setting());
            }
            assertEquals("A" + expectedSetting, actualSetting);

        }

    }

    @Test
    public void testRotate() {
        Alphabet alpha = new Alphabet("ABC");
        Permutation p1 = new Permutation("(ABC)", alpha);

        ArrayList<Rotor> rotorSet = new ArrayList<>();
        rotorSet.add(new Reflector("Beta", p1));
        rotorSet.add(new MovingRotor("III", p1, "C"));
        rotorSet.add(new MovingRotor("II", p1, "C"));
        rotorSet.add(new MovingRotor("I", p1, "C"));

        Machine machine1 = new Machine(alpha, 4, 3, rotorSet);
        machine1.insertRotors(new String[] {"Beta", "III", "II", "I"});
        for (int i = 0; i < machine1.numRotors() * 10; i += 1) {
            String settings = "";
            for (Rotor rotor: rotorSet) {
                settings += alpha.toChar(rotor.setting());

            }
            System.out.println(settings);
            machine1.rotate();
        }

    }
}
