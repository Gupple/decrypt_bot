package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Sahil Gupta
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        if (!perm.derangement()) {
            throw new EnigmaException("Permutation is not a derangement.");
        }
        super.setRingSetting(0);

    }

    /** Only sets setting to POSN if POSN is 0. */
    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

    /** Set setting() to character CPOSN. */
    @Override
    void set(char cposn) {
        set(alphabet().toInt(cposn));
    }

    /** Sets the ring setting of this rotor to RINGPOSN. */
    @Override
    void setRingSetting(int ringposn) {
        if (ringposn != 0) {
            throw new EnigmaException("Reflector can only "
                   + "have a 0 ring setting.");
        }
    }

    /** Sets the ring setting of this rotor to RINGCPOSN. */
    @Override
    void setRingSetting(char ringcposn) {
        setRingSetting(alphabet().toInt(ringcposn));
    }

    @Override
    boolean reflecting() {
        return true;
    }
}
