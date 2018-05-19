package engine.core;

import engine.exception.BadREException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static  org.junit.jupiter.api.Assertions.*;
import static  engine.core.RE2NFA.*;
/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-18 18:04
 **/
public class TestRE2NFA {

    @Test
    void testWrapConcat() throws Exception {
        NFA nfa = new NFA();
        RE2NFA.wrapConcat(nfa, RE2NFA.parse(nfa, "a", 0, 1),
                                    RE2NFA.parse(nfa, "b", 0, 1));
        assertEquals(4, nfa.getStates().size());
    }

    @Test
    void testWrapSelect() throws Exception {
        NFA nfa = new NFA();
        RE2NFA.wrapSelect(nfa, RE2NFA.parse(nfa, "a", 0, 1),
                                    RE2NFA.parse(nfa, "b", 0, 1));
        assertEquals(6, nfa.getStates().size());
    }

    @Test
    void testWrapEpsilon() throws Exception {
        NFA nfa = new NFA();
        RE2NFA.wrapEpsilon(nfa, RE2NFA.parse(nfa, "a", 0, 1));
        System.out.println(nfa);

        assertEquals(4, nfa.getStates().size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "a|c", "a*b*",
                            "a(b|c)*, a(b(c|d)*)*", "acd(cd|fa)*(fff(ef|af))"})
    void testParser(String re) {
        NFA nfa = new NFA();

        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                parse(nfa, re, 0, re.length());
            }
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"|aa", "ab**", "a(bb(c))**", "a(bb", "abc|"} )
    void testBadRE(String re) {
        NFA nfa = new NFA();
        assertThrows(BadREException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                parse(nfa, re, 0, re.length());
            }
        });
    }
}
