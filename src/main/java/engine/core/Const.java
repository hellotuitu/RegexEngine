package engine.core;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: regexengine
 * @description:
 * @author: TUITU
 * @create: 2018-05-17 12:16
 **/
public class Const {
    // NFA中的空输入
    public static final String EPSILON = "ε";
    public static final char EPSILON_VALUE = 0;

    // 正规文法的元符号
    public static final Set<Character> RE_META_SYMBOL = new HashSet<>();

    static {
        RE_META_SYMBOL.add('(');
        RE_META_SYMBOL.add(')');
        RE_META_SYMBOL.add('|');
        RE_META_SYMBOL.add('*');
    }
}
