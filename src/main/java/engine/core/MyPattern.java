package engine.core;

import java.util.Arrays;
import java.util.Map;

/**
 * @program: regex-engine
 * @description: 由DFA生成词法识别工具
 * @author: TUITU
 * @create: 2018-05-18 16:20
 **/
public class MyPattern {
    private DFA dfa;
    private int[][] transitionTable;

    public void setDFA(DFA dfa){
        this.dfa = dfa;
        // dfa发生了改变 使当前的转移表无效
        transitionTable = null;
    }

    public void compile(){
        if(transitionTable != null){
            return ;
        }

        int stateCount = dfa.getStates().size();
        transitionTable = new int[stateCount][256];

        for(int[] row : transitionTable){
            Arrays.fill(row, -1);
        }

        // 根据dfa构造转移表
        for(DFAState state : dfa.getStates()){
            int curID = state.getStateID();
            for(Map.Entry<Character, DFAState> entry : state.getNextStates().entrySet()){
                transitionTable[curID][entry.getKey()] = entry.getValue().getStateID();
            }
        }
    }

    /**
     * @param target 要匹配的目标字符串
     * @return 如果匹配返回-1，否则返回不匹配字符的位置
     */
    public int match(String target){
        if(dfa.getStartState() == null){
            dfa.setStartStateByExistStates();
        }

        int curState = dfa.getStartState().getStateID();
        int index = 0;
        for(; index < target.length(); index++){
            int gotoState = transitionTable[curState][target.charAt(index)];
            if(gotoState < 0 || gotoState >= dfa.getStates().size()){
                return index;
            } else {
                curState = gotoState;
            }
        }

        // 匹配完了所有字符 查看当前状态是否可接受
        if(dfa.getStates().get(curState).isAcceptable()){
            return -1;
        } else {
            return index;
        }
    }
}
