package engine;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-22 19:39
 **/
public class TestLexer {

    @Test
    void testCompile() throws Exception {
        Lexer lexer = new Lexer();
        lexer.compile(REContainer.map);
    }

    @Test
    void testLexer() throws Exception {
        Lexer lexer = new Lexer();
        lexer.compile(REContainer.map);

//        String source = "begin x:=9;if x>0 then x:=2/x+1/3;end#";
        String source = "begin x:=999;end#";
        LinkedList<Token> tokens = lexer.lexer(source);
        System.out.println(tokens.size());
        System.out.println(tokens);
    }
}
