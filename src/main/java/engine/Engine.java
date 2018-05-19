package engine;

import engine.core.*;

/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-19 13:11
 **/
public class Engine {
    /**
     * @param re 正规文法表示的re
     * @param target 匹配的目标
     * @return 是否匹配
     */
    public static boolean match(String re, String target) throws Exception {
        // RE -> NFA
        NFA nfa = RE2NFA.toNFA(re);
        // detect acceptable states
        nfa.detectAcceptableStates();
        // NFA -> DFA
        DFA dfa = NFA2DFA.toDFA(nfa);
        // minimize DFA
        dfa = DFAMinimizer.hopcroft(dfa);

        Lexer lexer = new Lexer();
        lexer.setDFA(dfa);
        lexer.compile();
        return lexer.match(target) == -1;
    }
}
