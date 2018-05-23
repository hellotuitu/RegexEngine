package engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-22 17:05
 **/
public class TestUtil {

    @ParameterizedTest
    @ValueSource(strings  = {"a", "b", "z"})
    void testAlpha(String alpha) throws Exception {
        assertEquals(true, Engine.match(Util.alpha,alpha));
    }

    @ParameterizedTest
    @ValueSource(strings  = {"0", "1", "2"})
    void testDigit(String digit) throws Exception {
        assertEquals(true, Engine.match(Util.digit, digit));
    }

    @ParameterizedTest
    @ValueSource(strings  = {"123", "456", "789"})
    void testNumber(String number) throws Exception {
        assertEquals(true, Engine.match(Util.number, number));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Abc0", "cAs11dd", "dfd222BDDFGf"})
    void testString(String string) throws Exception {
        assertEquals(true, Engine.match(Util.string, string));
    }


}
