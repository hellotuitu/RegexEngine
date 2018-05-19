package engine.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * @program: regexengine
 * @description:
 * @author: TUITU
 * @create: 2018-05-17 13:08
 **/
public class TestDFA {
    
    @Test
    void testNormal() throws Exception {
        DFA dfa = new DFA();
        DFAState[] states = new DFAState[10];

        for(int i = 0; i < states.length; i++){
            states[i] = dfa.newState();
        }

        states[0].addNextState('a', states[1]);
        states[0].addNextState('b', states[2]);
        states[0].addNextState('c', states[3]);
        states[1].addNextState('a', states[2]);
        states[3].addNextState('a', states[6]);
        states[8].addNextState('a', states[9]);
        states[7].addNextState('a', states[5]);

        states[5].setAcceptable();

        System.out.println(dfa);
    }

    @Test
    void testWrongCase() throws Exception {
        DFA dfa = new DFA();
        DFAState[] states = new DFAState[10];

        for(int i = 0; i < states.length; i++){
            states[i] = dfa.newState();
        }
        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                states[0].addNextState('a', states[1]);
                states[0].addNextState('a', states[2]);
                states[0].addNextState('a', states[3]);
            }
        });
    }
}
