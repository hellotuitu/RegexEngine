package engine;

/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-22 16:57
 **/
public class Util {
    public static final String alpha = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)";
    public static final String ALPHA = "(A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z)";
    public static final String CHAR = String.format("(%s|%s)", alpha, ALPHA);
    public static final String digit_without_zero = "(1|2|3|4|5|6|7|8|9)";
    public static final String digit = String.format("(0|%s)", digit_without_zero);
    public static final String number_not_leading_with_zero = digit_without_zero + digit + "*";
    public static final String number = "0|" + number_not_leading_with_zero;
    public static final String string = String.format("%s(%s|%s)*", CHAR, CHAR, digit);
}

