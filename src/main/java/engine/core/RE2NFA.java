package engine.core;


/**
 * @program: regex-engine
 * @description: 由正规文法生成NFA
 * @author: TUITU
 * @create: 2018-05-18 17:01
 **/
public class RE2NFA {
    /**
     * @param re 正规文法，注意：不是正则表达式
     * @return 生成的NFA
     */
    public static NFA toNFA(String re){
        NFA nfa = new NFA();
        try{
            parse(nfa, re, 0, re.length());
        } catch (Exception why){
            System.out.println(why.getMessage());
            nfa = null;
        }
        return nfa;
    }

    /**
     * @param nfa 目标的DFA
     * @param re 要解析的RE
     * @param from 起始位置，包含
     * @param to 终止位置，不包含
     * @Return 解析的部分生成的NFA的起始状态和终止状态构成的数组
     */
    public static NFAState[] parse(NFA nfa, String re, int from, int to) throws Exception {
        // 解析RE

        // 正规文法共五种情况：
        // 1. e -> ε
        // 2. e -> c
        // 3. e -> e1 e2 (连接)
        // 4. e -> e1 | e2 (选择)
        // 5. e -> e1* (闭包)

        // 对1，2可直接解析
        // 对3， 4， 5递归解析

        if(1 == to - from){
            // 只有一个字符 这个字符不可能是元符号
            // 为这个普通字符生成状态并返回
            if(Const.RE_META_SYMBOL.contains(re.charAt(from))){
                throw new Exception("parse re error");
            }
            NFAState[] states = {nfa.newState(), nfa.newState()};
            states[0].addNextState(re.charAt(from), states[1]);
            return states;
        }

        NFAState[] lastStates = null;
        for(int i = from; i < to && i < re.length(); ){
            char cur = re.charAt(i);

            if(Const.RE_META_SYMBOL.contains(cur)){
                // 元符号
                if(cur == '('){
                    // 继续找 找到匹配的 ）
                    int layer = 0;
                    for(int index = i + 1; index < to && index < re.length(); index++){
                        if(re.charAt(index) == '('){
                            layer++;
                        } else if(re.charAt(index) == ')'){
                            if(layer == 0){
                                NFAState[] ret = parse(nfa, re, i + 1, index);
                                if(index + 1 < to && re.charAt(index + 1) == '*'){
                                    // 闭包
                                    ret = wrapEpsilon(nfa, ret);
                                    i = index + 2;
                                } else {
                                    i = index + 1;
                                }

                                if(lastStates == null){
                                    lastStates = ret;
                                } else {
                                    lastStates = wrapConcat(nfa, lastStates, ret);
                                    break;
                                }
                            } else {
                                layer--;
                            }
                        }
                    }
                } else if(cur == '|'){
                    // 左边的不能为null
                    assert lastStates != null;
                    NFAState[] ret = parse(nfa, re, i + 1, to);
                    lastStates = wrapSelect(nfa, lastStates, ret);
                    i = to;
                } else {
                    System.out.println("error");
                }
            } else {
                // 普通字符
                NFAState[] ret = parse(nfa, re, i, i + 1);
                if(i + 1 < to && re.charAt(i + 1) == '*'){
                    ret = wrapEpsilon(nfa, ret);
                    i = i + 2;
                } else {
                    i++;
                }
                if(lastStates == null){
                    lastStates = ret;
                } else {
                    lastStates = wrapConcat(nfa, lastStates, ret);
                }
            }
        }

        return lastStates;
    }

    public static NFAState[] wrapConcat(NFA nfa, NFAState[] left, NFAState[] right){
        left[1].addNextState(Const.EPSILON_VALUE, right[0]);
        return new NFAState[]{left[0], right[1]};
    }

    public static NFAState[] wrapSelect(NFA nfa, NFAState[] up, NFAState[] down){
        NFAState left = nfa.newState();
        NFAState right = nfa.newState();

        left.addNextState(Const.EPSILON_VALUE, up[0]);
        left.addNextState(Const.EPSILON_VALUE, down[0]);
        up[1].addNextState(Const.EPSILON_VALUE, right);
        down[1].addNextState(Const.EPSILON_VALUE, right);

        return new NFAState[]{left, right};
    }

    public static NFAState[] wrapEpsilon(NFA nfa, NFAState[] states){
        NFAState left = nfa.newState();
        NFAState right = nfa.newState();

        left.addNextState(Const.EPSILON_VALUE, states[0]);
        left.addNextState(Const.EPSILON_VALUE, right);
        states[1].addNextState(Const.EPSILON_VALUE, states[0]);
        states[1].addNextState(Const.EPSILON_VALUE, right);

        return new NFAState[]{left, right};
    }
}
