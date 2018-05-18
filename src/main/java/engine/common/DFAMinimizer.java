package engine.common;

import java.util.*;

/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-18 09:45
 **/
public class DFAMinimizer {
    /**
     * @param dfa 需进行最小化的DFA
     * @return 返回一个新的最小化的DFA
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
    public static DFA hopcroft(DFA dfa) throws Exception {
        Set<Set<DFAState>> allSet = splitAll(dfa);

        // 利用划分好的集合重新构建一个最小化的dfa
        DFA miniDFA = new DFA();
        Map<Set<DFAState>, DFAState> table = new HashMap<>();

        // 为每一个集合生成一个DFAState
        for(Set<DFAState> set : allSet){
            table.put(set, miniDFA.newState());
        }

        // 生成转移关系
        // 每一个集合应该只转移到另一个集合或自身
        for(Set<DFAState> set : allSet){
            // 获取集合所能接受的全部字符
            TreeSet<Character> charSet = new TreeSet<>();
            // 判断当前集合是否时可接受的集合
            boolean acceptable = false;
            for(DFAState state : set){
                for(char c : state.getNextStates().keySet()){
                    charSet.add(c);
                }
                if(state.isAcceptable()){
                    acceptable = true;
                }
            }

            // 从当前集合中选一个状态判断当前集合转移到哪个集合
            DFAState toState = null;
            // 如果可接受字符为空 则转移到自身
            DFAState state = (DFAState) set.toArray()[0];
            if(!charSet.isEmpty()){
                char randomC = (char) charSet.toArray()[0];
                if(state.getNextStates().containsKey(randomC)){
                    for(Set<DFAState> t : allSet){
                        if(t.contains(state.getNextState(randomC))){
                            toState = table.get(t);
                        }
                    }
                } else {
                    // 转移到自身
                    toState = table.get(set);
                }
            } else {
                toState = table.get(set);
            }


            // 为所有字符生成转移关系
            DFAState cur = table.get(set);
            for(char c : charSet){
                cur.addNextState(c, toState);
            }

            // 设置是否可接受
            if(acceptable){
                cur.setAcceptable();
            }
        }

        return miniDFA;
    }

    /**
     * @param dfa
     * @return DFA划分至不可分状态后的所有状态集合的集合
     */
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

    /**
     * 划分一个集合，并将划分的结果加入到allSet中，然后删除原集合
     * @param set
     * @param allSet
     */
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
