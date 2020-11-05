package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Sahil Gupta
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {

        if (allRotors.size() < numRotors) {
            throw new EnigmaException(
                    "The number of Rotor objects is less than "
                           + "the rotor count parameter.");
        }

        if (numRotors <= 1) {
            throw new EnigmaException("The number of rotors is "
                   + "less than or equal to 1.");
        }

        int countPawls = 0, countReflectors = 0;
        for (Rotor rotor: allRotors) {
            if (!rotor.alphabet().equals(alpha)) {
                throw new EnigmaException("One of the rotors does not"
                       + " share the same alphabet.");
            }
            if (rotor.rotates()) {
                countPawls += 1;
            } else if (rotor.reflecting()) {
                countReflectors += 1;
            }
        }

        if (countPawls < pawls) {
            throw new EnigmaException(
                    "The number of movable Rotor objects "
                           + "is less than the pawl count parameter");
        }

        if (countReflectors < 1) {
            throw new EnigmaException("There must be at least one reflector.");
        }

        if (pawls == numRotors || allRotors.size() == countPawls) {
            throw new EnigmaException("The pawl count is equal "
                   + "to the number of rotors.");
        }

        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _plugboard = new Permutation("", alpha);
        _allRotorsOrdered = new ArrayList<>();
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _allRotorsOrdered.clear();
        if (rotors.length != numRotors()) {
            throw new EnigmaException("Input does not have the same"
                   + " number of rotors as the initialized amount.");
        }

        int countPawls = 0;
        for (int i = 0; i < rotors.length; i += 1) {
            Rotor currentRotor = null;
            for (Rotor rotor: _allRotors) {
                if (rotor.name().equals(rotors[i])) {
                    currentRotor = rotor;
                    break;
                }
            }
            if (currentRotor == null) {
                throw new EnigmaException("Rotor " + rotors[i]
                        + " does not exist.");
            } else if (i == 0 && !currentRotor.reflecting()) {
                throw new EnigmaException("The first rotor "
                       + "is not a reflector.");
            } else if (i != 0 && currentRotor.reflecting()) {
                throw new EnigmaException("There can only be "
                       + "one reflector inserted.");
            }
            if (currentRotor.rotates()) {
                countPawls += 1;
            }
            _allRotorsOrdered.add(currentRotor);
            currentRotor.set(0);
        }
        if (countPawls != numPawls()) {
            throw new EnigmaException("The number of pawls in the settings"
                   + "is not equivalent to the pawl count parameter.");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("Setting must be "
                   + "of length numRotors()-1.");
        }
        for (int i = 1; i < _allRotorsOrdered.size(); i += 1) {
            _allRotorsOrdered.get(i).set(setting.charAt(i - 1));
        }
    }

    /** Set my rotors ring settings according to SETTING, which must be a
     *  string of numRotors()-1 characters in my alphabet. The first letter
     *  refers to the leftmost rotor setting (not counting the reflector). */
    void setRingSetting(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("Setting must be "
                    + "of length numRotors()-1.");
        }
        for (int i = 1; i < _allRotorsOrdered.size(); i += 1) {
            _allRotorsOrdered.get(i).setRingSetting(setting.charAt(i - 1));
        }

        for (int i = 1; i < _allRotorsOrdered.size(); i += 1) {
            Rotor rotor = _allRotorsOrdered.get(i);
            rotor.set(rotor.setting() - _alphabet.toInt(setting.charAt(i - 1)));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int permutation = _plugboard.permute(c);
        rotate();
        for (int i = _allRotorsOrdered.size() - 1; i >= 0; i -= 1) {
            permutation = _allRotorsOrdered.get(i)
                    .convertForward(permutation);
        }
        for (int j = 1; j < _allRotorsOrdered.size(); j += 1) {
            permutation = _allRotorsOrdered.get(j)
                    .convertBackward(permutation);
        }

        return _plugboard.invert(permutation);
    }

    /** Rotates the rotors based on their mobility and whether the rotor
     * to the right of the rotor is at the notch.
     */
    void rotate() {
        Rotor rightMost = _allRotorsOrdered
                .get(_allRotorsOrdered.size() - 1);
        int[] rotorSettings = new int[numRotors()];

        for (int i = 0; i < _allRotorsOrdered.size() - 1; i += 1) {
            Rotor rotorLeft = _allRotorsOrdered.get(i),
                    rotorRight = _allRotorsOrdered.get(i + 1);
            if (rotorLeft.rotates() && rotorRight.atNotch()
                    && rotorSettings[i] == 0) {
                rotorLeft.advance();
                rotorSettings[i] = 1;
                if (rotorRight.rotates()) {
                    rotorRight.advance();
                    rotorSettings[i + 1] = 1;
                }
            }
        }

        if (rotorSettings[numRotors() - 1] == 0 && rightMost.rotates()) {
            rightMost.advance();
        }
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        if (msg.equals("")) {
            return msg;
        } else {
            char c = msg.charAt(0);
            if (!_alphabet.contains(c)) {
                throw new EnigmaException(
                        String.format("Character \"%c\" "
                               + "is not in the alphabet.", c));
            }
            int converted = convert(_alphabet.toInt(msg.charAt(0)));
            return _alphabet.toChar(converted) + convert(msg.substring(1));
        }
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors I have. */
    private int _numRotors;

    /** Number of pawls/movable rotors I have. */
    private int _pawls;

    /** All of my rotors. */
    private Collection<Rotor> _allRotors;

    /** My plugboard. */
    private Permutation _plugboard;

    /** This is the ordered list of my rotors. */
    private ArrayList<Rotor> _allRotorsOrdered;
}
