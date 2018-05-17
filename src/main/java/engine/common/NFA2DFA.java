package engine.common;

import java.util.ArrayList;
import java.util.Map;

/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-17 22:40
 **/
public class NFA2DFA {
    /**
     * 使用 子集构造算法 将 NFA 转换为 DFA
     * @param nfa 非确定有限状态机
     * @return 确定有限状态机(DFA)
     */
    public static DFA toDFA(NFA nfa){

        return null;
    }

    public static void getEpsClosure(NFAState state, ArrayList<Integer> closure){
        closure.add(state.getStateID());

        ArrayList<NFAState> states = state.getNextStates().get(Const.EPSILON_VALUE);

        if(states != null){
            for(NFAState next : states){
                getEpsClosure(next, closure);
            }
        }

    }
}
