package engine.common;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @program: regexengine
 * @description:
 * @author: TUITU
 * @create: 2018-05-17 12:12
 **/
public class DFA {
    private int stateCounter = 0;
    private ArrayList<DFAState> states;
    private DFAState startState;

    public DFA(){
        states = new ArrayList<>();
    }

    public DFAState newState(){
        DFAState state = new DFAState(getStateCounterAndIncrement());
        states.add(state);

        return state;
    }

    public ArrayList<DFAState> getStates(){
        return states;
    }

    public int getStateCounterAndIncrement(){
        return stateCounter++;
    }

    /**
     * 根据当前已存在的状态设置开始状态
     */
    public void setStartStateByExistStates(){
        // 搜索当前所有的节点 找到第一个未被其他状态指向过的状态 设置为起始状态
        Set<DFAState> set = new HashSet<>(states);

        for(DFAState state : states){
            set.removeAll(state.getNextStates().values());
        }

        // 剩下的节点应该只有一个
        assert set.size() == 1;
        setStartState((DFAState) set.toArray()[0]);
    }

    public void setStartState(DFAState state){
        this.startState = state;
    }

    public DFAState getStartState(){
        return this.startState;
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
