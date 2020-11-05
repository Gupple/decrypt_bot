package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Sahil Gupta
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initially in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);

        for (int i = 0; i < notches.length(); i += 1) {
            char c = notches.charAt(i);
            if (!alphabet().contains(c)) {
                throw new EnigmaException(
                        String.format("Notch setting \"%c\" "
                               + "doesn't exist.", c));
            }
        }
        _notches = notches;
    }

    /** Returns the notches of this moving rotor. */
    String getNotches() {
        return _notches;
    }


    @Override
    void advance() {
        set(setting() + 1);
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i += 1) {
            int modifiedNotch = mod(alphabet()
                    .toInt(_notches.charAt(i)) - ringSetting(), size());
            if (modifiedNotch == setting()) {
                return true;
            }
        }
        return false;
    }

    /** Denotes the notches on the alphabet ring of the rotor. */
    private String _notches;
}
