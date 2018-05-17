package engine.common;

import static org.junit.jupiter.api.Assertions.*;

import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-17 22:55
 **/
public class TestNFA2DFA {

    @Test
    void testGetEpsClosure() throws DocumentException {
        String fileName = ".\\src\\main\\resources\\sample_dfa.xml";
        NFA nfa = NFA.constructNFAFromXML(fileName);

        ArrayList<NFAState> states = nfa.getStates();
        Set<Integer> closure = new HashSet<>();

        NFA2DFA.getEpsClosure(states.get(1), closure);

        int[] expect = new int[]{1, 2, 3, 4, 6, 9};
        ArrayList<Integer> expectClosure = new ArrayList<>();
        for(int i = 0; i < expect.length; i++){
            expectClosure.add(expect[i]);
        }
        assertEquals(closure, expectClosure);
    }

    @Test
    void testNFA2DFA() throws Exception {
        String fileName = ".\\src\\main\\resources\\sample_dfa.xml";
        NFA nfa = NFA.constructNFAFromXML(fileName);

        DFA dfa = NFA2DFA.toDFA(nfa);
        System.out.println(dfa);
    }


}
