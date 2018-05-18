package engine.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-18 09:45
 **/
public class DFAMinimizer {
    /**
     * 注意，最小化是在原DFA之上进行的
     *
     * @param dfa 需进行最小化的DFA
     * @return 最小化的DFA
     */
    public static DFA minimize(DFA dfa){
        return null;
    }

    /**
     * 使用 hopcroft 算法进行DFA最小化，其思想如下：
     *   初始时将所有状态划分为两个集合，一个是可接受状态集合，一个是不可接受状态集合
     *   然后算法进入循环，对每一个集合进行是否可分判断
     *   判断是否可分的依据是 对于这个集合每一个可接受的字符，如果接受这个字符时转向了不同的状态，则可分
     * @param dfa
     */
    public static void hopcroft(DFA dfa){

    }

    public static Set<Set<DFAState>> splitAll(DFA dfa){
        // 1: 划分为两个集合 可接受集合 不可接受集合
        Set<Set<DFAState>> allSet = new HashSet<>();
        Set<DFAState> acc = new HashSet<>();
        Set<DFAState> unacc = new HashSet<>();

        for(DFAState state : dfa.getStates()){
            if(state.isAcceptable()){
                acc.add(state);
            } else {
                unacc.add(state);
            }
        }

        allSet.add(acc);
        allSet.add(unacc);

        // 2: 循环判断每个集合是否可分
        int setSize = 0;
        while(setSize != allSet.size()){
            setSize = allSet.size();

            for(Set<DFAState> set : allSet){
                split(set, allSet);
                if(allSet.size() != setSize){
                    break;
                }
            }
        }

        return allSet;
    }

    public static void split(Set<DFAState> set, Set<Set<DFAState>> allSet) {
        // 0： 如果当前set只有一个状态，直接返回
        if(set.size() == 1){
            return ;
        }

        // 1: 获取集合所有接受字符
        Set<Character> charSet = new HashSet<>();
        for(DFAState state : set){
            for(char c : state.getNextStates().keySet()){
                charSet.add(c);
            }
        }

        // 2: 循环判断每个字符，如果可分，分割完之后返回，不再继续循环
        for(char ch : charSet){
            // 暂存分割结果
            HashMap<Set<DFAState>, Set<DFAState>> setTOSet = new HashMap<>();

            for(DFAState state : set){
                Set<DFAState> toSet = new HashSet<>();

                if(state.getNextStates().containsKey(ch)){
                    // 当前状态对该字符可以转移
                    DFAState toState = state.getNextStates().get(ch);
                    // 判断该状态在哪个集合中
                    for(Set<DFAState> temp : allSet){
                        if(temp.contains(toState)){
                            // 接受字符转移到了temp集合里的状态
                            toSet = temp;
                        }
                    }
                } else {
                    // 转移到了当前集合
                    toSet = set;
                }

                if(setTOSet.containsKey(toSet)){
                    setTOSet.get(toSet).add(state);
                } else {
                    Set<DFAState> no_name = new HashSet<>();
                    no_name.add(state);
                    setTOSet.put(toSet, no_name);
                }
            }

            if(setTOSet.size() > 1){
                // 转移到了不同状态
                allSet.remove(set);
                allSet.addAll(setTOSet.values());
                return ;
            }
        }
    }
}
