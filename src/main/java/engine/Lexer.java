package engine;

import engine.core.*;

import java.util.*;

/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-22 18:57
 **/
class Token {
    private int id;
    private String text;

    public Token(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public String toString(){
        return String.format("(%s, \"%s\")", id, text);
    }
}

class REContainer {
    public static final Map<String, Integer> map = new LinkedHashMap<>();

    static {
        // 所有元符号先注释掉
        // 前面的优先级较后面的优先级高
//        map.put("identifier", start++);
//        map.put("number", start++);
//        map.put("*", start++);
//        map.put("(", start++);
//        map.put(")", start++);
        int start = 1;
        map.put("begin", start++);
        map.put("if", start++);
        map.put("then", start++);
        map.put("while", start++);
        map.put("do", start++);
        map.put("end", start++);
        map.put("+", start++);
        map.put("-", start++);
        map.put("/", start++);
        map.put(":", start++);
        map.put(":=", start++);
        map.put("<", start++);
        map.put("<>", start++);
        map.put("<=", start++);
        map.put(">", start++);
        map.put(">=", start++);
        map.put("=", start++);
        map.put(";", start++);
        map.put("#", start++);
        map.put(Util.string, start++);
        map.put(Util.number, start++);
    }
}
public class Lexer {
    public Map<String, Integer> reMap;
    public int[][][] transitionTables;
    public DFA[] dfas;

    public void compile(Map<String, Integer> reMap) throws Exception {
        this.reMap = reMap;
        this.transitionTables = new int[reMap.size()][][];
        this.dfas = new DFA[reMap.size()];

        int index = 0;
        for(String re : reMap.keySet()){
            NFA nfa = RE2NFA.toNFA(re);
            DFA dfa = NFA2DFA.toDFA(nfa);
            dfa = DFAMinimizer.minimize(dfa);

            this.dfas[index] = dfa;

            // 构造转移表
            int[][] transitionTable = new int[dfa.getStates().size()][256];
            for(int[] row : transitionTable){
                Arrays.fill(row, -1);
            }
            for(DFAState state : dfa.getStates()){
                int curID = state.getStateID();
                for(Map.Entry<Character, DFAState> entry : state.getNextStates().entrySet()){
                    transitionTable[curID][entry.getKey()] = entry.getValue().getStateID();
                }
            }

            this.transitionTables[index] = transitionTable;
            index++;
        }
    }

    /**
     * 匹配原则：
     *     1：最长匹配
     *     2：最先匹配
     * @param source 源字符串
     * @return 返回所有的token的列表
     */
    public LinkedList<Token> lexer(String source){
        LinkedList<Token> tokens = new LinkedList<>();

        // 初始化起始状态
        // 0 - 初始状态， 2 - 识别中， 3 - 错误
        int state = 0;
        final int[] stopStates = new int[dfas.length];
        Arrays.fill(stopStates, -1);
        int[] initStates = new int[dfas.length];
        for(int i = 0; i < initStates.length; i++){
            initStates[i] = dfas[i].getStartState().getStateID();
        }


        int sourceIndex = 0;
        int lastIndex = 0;
        int[] curStates = null;
        int[] nextStates = null;
        // 增加=情况是为了方便做新一轮处理
        while(sourceIndex <= source.length()){
            // 查看当前是什么状态
            if(state == 0){
                // 如果串已经结束了
                if(sourceIndex >= source.length()){
                    break;
                }

                // 重新初始化
                curStates = initStates.clone();
                nextStates = new int[dfas.length];

                state = 2;
            } else if(state == 3) {
                // 错误
                System.out.println("error");
                break;
            } else if(state == 2){
                // 识别中 继续

                // 生成nextStates
                if(sourceIndex < source.length()){
                    char curChar = source.charAt(sourceIndex);

                    // 处理空格和换行

                    for(int tableIndex = 0; tableIndex < transitionTables.length; tableIndex++){
                        if(curStates[tableIndex] != -1){
                            // 等于-1已终止 跳过
                            nextStates[tableIndex] =
                                    transitionTables[tableIndex][curStates[tableIndex]][curChar];
                        }
                    }
                } else {
                    // 直接强行终止
                    nextStates = stopStates;
                }

                // 检查nextStates是不是全部终止了
                if(Arrays.equals(nextStates, stopStates)){
                    // 终止了 说明当前states是终止态
                    // 查找accept状态
                    boolean found = false;
                    for(int i = 0; i < dfas.length; i++){
                        if(curStates[i] != - 1 && !found && dfas[i].getStates().get(curStates[i]).isAcceptable()){
                            // 当前状态可被接受 且 未之前找到过接受状态
                            Token token = new Token((Integer) REContainer.map.values().toArray
                                    ()[i],
                                    source.substring(lastIndex, sourceIndex));
                            tokens.add(token);

                            // 找到
                            found = true;
                        }
                    }

                    if(!found){
                        // 终止了但没有可接受的状态
                        // 报告错误 并 尝试前进一位

                        // 对于空格和换行应当认为是合法的分隔符 直接忽略即可
                        char curChar = source.charAt(sourceIndex);

                        if(curChar != ' ' && curChar != '\n'){
                            System.out.println(String.format("illegal character \"%s\"， at %d.", source
                                    .charAt(sourceIndex), sourceIndex));
                        }
                        sourceIndex++;
                    }

                    // 终止之后的处理
                    lastIndex = sourceIndex;
                    state = 0;
                } else {
                    // 未终止
                    curStates = nextStates.clone();
                    sourceIndex++;
                }
            }

        }

        return tokens;
    }
}
