package engine.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static  org.junit.jupiter.api.Assertions.*;
import static  engine.common.RE2NFA.*;
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
    void testWrapSelct() throws Exception {
        NFA nfa = new NFA();
        RE2NFA.wrapSelect(nfa, RE2NFA.parse(nfa, "a", 0, 1),
                                    RE2NFA.parse(nfa, "b", 0, 1));
        assertEquals(5, nfa.getStates().size());
    }

    @Test
    void testWrapEpsilon() throws Exception {
        NFA nfa = new NFA();
        RE2NFA.wrapEpsilon(nfa, RE2NFA.parse(nfa, "a", 0, 1));
        System.out.println(nfa);

        assertEquals(4, nfa.getStates().size());
    }

    @Test
    void testParser() throws Exception {
        NFA nfa = null;
        String re = null;

        re = "abc";
        parse(nfa = new NFA(), re, 0, re.length());

        re = "a|c";
        parse(nfa = new NFA(), re, 0, re.length());

        re = "a*b";
        parse(nfa = new NFA(), re, 0, re.length());

        re = "a(b|c)*";
        parse(nfa = new NFA(), re, 0, re.length());

        System.out.println(nfa);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a(b|c)*", "f(ee|ie)"})
    void testAll(String re) throws Exception {
        NFA nfa = new NFA();

        parse(nfa, re, 0, re.length());
        nfa.detectAcceptableStates();

        DFA dfa = NFA2DFA.toDFA(nfa);
        dfa.setStartStateByExistStates();
        dfa = DFAMinimizer.minimize(dfa);

        System.out.println(dfa);
    }
}
