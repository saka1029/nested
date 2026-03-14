package saka1029.nested;

/**
 * グローバル変数、ローカル変数、ルーチン参照
 * <pre>
 *   Reference <*--------------- Nested
 *      △
 *      |
 *      +------+-------------------+
 *      |      |      variables    |
 *   Global  Local <*----------- Routine
 * </pre>
 */
public class Reference {

    public final int address;

    public Reference(int address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "%s(addr=%d)".formatted(getClass().getSimpleName(), address);
    }

}
