package engine.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-18 10:55
 **/
public class TestDFAMinimizer {

    private DFA constructDFA(String reg){
        DFA dfa = new DFA();

        try{
            if("a(b|c)*".equals(reg)){
                DFAState state0 = dfa.newState();
                DFAState state1 = dfa.newState();
                DFAState state2 = dfa.newState();
                DFAState state3 = dfa.newState();

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
            } else if("f(ee|ie)".equals(reg)){
                DFAState state0 = dfa.newState();
                DFAState state1 = dfa.newState();
                DFAState state2 = dfa.newState();
                DFAState state3 = dfa.newState();
                DFAState state4 = dfa.newState();
                DFAState state5 = dfa.newState();

                state0.addNextState('f', state1);

                state1.addNextState('e', state2);
                state1.addNextState('i', state4);

                state2.addNextState('e', state3);

                state4.addNextState('e', state5);

                state3.setAcceptable();

                state5.setAcceptable();
            } else {
                throw new Exception("can't create dfa for " + reg);
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
            dfa = null;
        }

        return dfa;
    }

    @Test
    void testSplit() throws Exception {
        // can't minimize case

        DFA expectDFA = constructDFA("a(b|c)*");

        // construct params
        Set<Set<DFAState>> allSet = new HashSet<>();
        Set<DFAState> acc = new HashSet<>();
        Set<DFAState> unacc = new HashSet<>();

        for(DFAState state : expectDFA.getStates()){
            if(state.isAcceptable()){
                acc.add(state);
            } else {
                unacc.add(state);
            }
        }

        allSet.add(acc);
        allSet.add(unacc);

        DFAMinimizer.split(acc, allSet);
        assertEquals(2, allSet.size());

        DFAMinimizer.split(unacc, allSet);
        assertEquals(2, allSet.size());
    }

    @Test
    void testSplit1() throws Exception {
        // can minimize case

        // construct a dfa
        DFA dfa = constructDFA("f(ee|ie)");

        Set<Set<DFAState>> allSet = DFAMinimizer.splitAll(dfa);

        // expect to split to [[state0], [state1], [state2, state4], [state3, state5]]
        assertEquals(4, allSet.size());
    }
}
