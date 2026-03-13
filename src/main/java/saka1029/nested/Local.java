package saka1029.nested;

/**
 * bp相対でアドレスするローカル変数。
 * ルーチンにおける引数を含む。
 * 帯域変数は含めない。
 */
public class Local extends Reference {

    public Local(int address) {
        super(address);
    }

}
