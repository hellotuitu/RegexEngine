package engine.common;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @program: regexengine
 * @description:
 * @author: TUITU
 * @create: 2018-05-17 12:12
 **/
public class DFA {
    private int stateCounter = 0;
    private ArrayList<DFAState> states;

    public DFA(){
        states = new ArrayList<>();
    }

    public DFAState newState(){
        DFAState state = new DFAState(getStateCounterAndIncrement());
        states.add(state);

        return state;
    }
    public int getStateCounterAndIncrement(){
        return stateCounter++;
    }


    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("DFA with " + this.stateCounter + " states.\n\n");
        for(DFAState state : states){
            sb.append(state.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
