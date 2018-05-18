package engine.common;

import org.dom4j.DocumentException;

import java.util.*;

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
    public static DFA toDFA(NFA nfa) throws Exception {
        ArrayList<NFAState> nfaStates = nfa.getStates();
        DFA dfa = new DFA();

        DFAState q0 = dfa.newState();
        Set<Integer> q0Eps = new HashSet<>();
        getEpsClosure(nfaStates.get(0), q0Eps);

        LinkedList<DFAState> workList = new LinkedList<>();
        LinkedList<Set<Integer>> epsList = new LinkedList<>();
        LinkedList<Set<Integer>> epsStoreList = new LinkedList<>();

        workList.add(q0);
        epsList.add(q0Eps);
        epsStoreList.add(q0Eps);

        while(!workList.isEmpty()){
            DFAState cur = workList.remove();
            Set<Integer> eps = epsList.remove();

            // 获取当前cur可以接受的字符
            ArrayList<Character> acceptChar = new ArrayList<>();
            for(int i : eps){
                for(Character c : nfaStates.get(i).getNextStates().keySet()){
                    if(Const.EPSILON_VALUE != c && !acceptChar.contains(c)){
                        acceptChar.add(c);
                    }
                }
            }

            // 对每个可以接受的字符求 epsilon
            for(char c : acceptChar){
                Set<Integer> tempClosure = new HashSet<>();
                for(int i : eps){
                    Map<Character, ArrayList<NFAState>> map = nfaStates.get(i).getNextStates();
                    if(map.containsKey(c)){
                        for(NFAState tempS : map.get(c)){
                            getEpsClosure(tempS, tempClosure);
                        }
                    }
                }

                // 如果 tempClosure 不为空 且 未在epsList中出现过
                // 则生成新的DFA节点
                if(!tempClosure.isEmpty()){

                    boolean found = false;
                    int foundIndex = -1;
                    for(int i = 0; i < epsStoreList.size(); i++){
                        if(epsStoreList.get(i).equals(tempClosure)){
                            found = true;
                            foundIndex = i;
                            break;
                        }
                    }

                    if(!found){
                        DFAState temp = dfa.newState();
                        cur.addNextState(c, temp);

                        // 判断新增的节点是否是 可接受状态
                        for(int stateIndex : tempClosure){
                            if(nfa.getStates().get(stateIndex).isAcceptable()){
                                temp.setAcceptable();
                                break;
                            }
                        }

                        workList.add(temp);
                        epsList.add(tempClosure);
                        epsStoreList.add(tempClosure);
                    } else {
                        // 如果当前状态已出现过了
                        cur.addNextState(c, dfa.getStates().get(foundIndex));
                    }

                }
            }
        }
        return dfa;
    }

    public static void getEpsClosure(NFAState state, Set<Integer> closure){
        closure.add(state.getStateID());

        ArrayList<NFAState> states = state.getNextStates().get(Const.EPSILON_VALUE);

        if(states != null){
            for(NFAState next : states){
                getEpsClosure(next, closure);
            }
        }

    }

    public static void main(String[] args) throws Exception {
        String fileName = ".\\src\\main\\resources\\sample_dfa.xml";
        NFA nfa = NFA.constructNFAFromXML(fileName);

        DFA dfa = NFA2DFA.toDFA(nfa);
        System.out.println(dfa);
    }

}
