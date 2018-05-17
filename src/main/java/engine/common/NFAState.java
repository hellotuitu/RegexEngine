package engine.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: regexengine
 * @description:
 * @author: TUITU
 * @create: 2018-05-17 12:15
 **/
public class NFAState extends State {
    private Map<Character, ArrayList<NFAState>> nextStates;

    public NFAState(int stateID) {
        super(stateID);
        nextStates = new HashMap<>();
    }

    public ArrayList<NFAState> getNextState(char key){
        return nextStates.get(key);
    }

    public Map<Character, ArrayList<NFAState>> getNextStates(){
        return nextStates;
    }

    public void addNextState(char key, NFAState nextState){
        nextStates.computeIfAbsent(key, k -> new ArrayList<NFAState>());
        nextStates.get(key).add(nextState);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        String desc = String.format("ID %d, Acceptable: %s: \n", this.getStateID(), this
                .isAcceptable());
        sb.append(desc);

        for(Map.Entry<Character, ArrayList<NFAState>> entry : nextStates.entrySet()){
            ArrayList<Integer> gotoStates = new ArrayList<>();
            for(NFAState s : entry.getValue()){
                gotoStates.add(s.getStateID());
            }
            sb.append("when \"" + entry.getKey() + "\" goto state " + gotoStates + "\n");
        }

        return sb.toString();
    }
}
