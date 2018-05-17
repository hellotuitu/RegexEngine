package engine.common;

import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;

/**
 * @program: regexengine
 * @description:
 * @author: TUITU
 * @create: 2018-05-17 20:56
 **/
public class TestNFA {

    @Test
    void testNormal(){
        System.out.println(new NFAState(0).toString());

        NFA nfa = new NFA();
        NFAState[] states = new NFAState[5];
        for(int i = 0; i < 5; i++){
            states[i] = nfa.newState();
        }

        states[0].addNextState('a', states[1]);
        states[0].addNextState('a', states[2]);
        states[0].addNextState('b', states[3]);
        states[0].addNextState('b', states[4]);

        states[1].addNextState('f', states[0]);
        states[1].addNextState('f', states[0]);
        states[1].addNextState('g', states[0]);
        states[1].addNextState('d', states[0]);
        states[1].addNextState('a', states[0]);

        states[2].setAcceptable();
        System.out.println(nfa);
    }

    @Test
    void testConstructNFAFromXML() throws DocumentException {
        String fileName = ".\\src\\main\\resources\\sample_dfa.xml";

        NFA nfa = NFA.constructNFAFromXML(fileName);
        System.out.println(nfa);
    }
}
