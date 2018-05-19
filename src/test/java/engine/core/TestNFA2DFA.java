package engine.core;

import static org.junit.jupiter.api.Assertions.*;

import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
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
        Set<Integer> expectClosure = new HashSet<>();
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

        DFA expectDFA = new DFA();
        DFAState state0 = expectDFA.newState();
        DFAState state1 = expectDFA.newState();
        DFAState state2 = expectDFA.newState();
        DFAState state3 = expectDFA.newState();

        state0.addNextState('a', state1);

        state1.addNextState('b', state2);
        state1.addNextState('c', state3);
        state1.setAcceptable();

        state2.addNextState('b', state2);
        state2.addNextState('c', state3);
        state2.setAcceptable();

        state3.addNextState('b', state2);
        state3.addNextState('c', state3);
        state3.setAcceptable();

        assertEquals(dfa.toString(), expectDFA.toString());
    }


}
