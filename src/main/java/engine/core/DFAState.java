package engine.core;

import java.util.*;

/**
 * @program: regexengine
 * @description:
 * @author: TUITU
 * @create: 2018-05-17 12:16
 **/
public class DFAState extends State {
    private Map<Character, DFAState> nextStates;

    public DFAState(int stateID) {
        super(stateID);
        nextStates = new HashMap<>();
    }

    public DFAState getNextState(char key){
        return nextStates.get(key);
    }

    public Map<Character, DFAState> getNextStates(){
        return nextStates;
    }

    public void addNextState(char key, DFAState nextState) throws Exception {
        if(nextStates.get(key) != null){
            throw new Exception("add DFAState error. key " + key + " already exists.");
        }

        nextStates.put(key, nextState);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        String desc = String.format("DFA State ID %d, Acceptable: %s \n", this.getStateID(), this
                .isAcceptable());
        sb.append(desc);

        for(Map.Entry<Character, DFAState> entry : nextStates.entrySet()){
            sb.append("when \"" + entry.getKey() + "\" goto state " + entry.getValue().getStateID());
            sb.append("\n");
        }

        return sb.toString();
    }
}
