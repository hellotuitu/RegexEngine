package engine.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-18 16:42
 **/
public class TestLexer {

    @Test
    void testMatch() throws Exception {
        DFA dfa = new TestDFAMinimizer().constructDFA("a(b|c)*");
        dfa = DFAMinimizer.minimize(dfa);

        Lexer lexer = new Lexer();
        lexer.setDDFA(dfa);
        lexer.compile();

        assertEquals(-1, lexer.match("a"));
        assertEquals(-1, lexer.match("ab"));
        assertEquals(-1, lexer.match("abc"));
        assertEquals(-1, lexer.match("ac"));
        assertEquals(-1, lexer.match("abbbbbbc"));

        assertEquals(0, lexer.match("bcccc"));
        assertEquals(1, lexer.match("adddd"));
        assertEquals(2, lexer.match("abfff"));
        assertEquals(3, lexer.match("acbq"));
    }

    @Test
    void testMatch2() throws Exception {
        DFA dfa = new TestDFAMinimizer().constructDFA("f(ee|ie)");
        dfa = DFAMinimizer.minimize(dfa);

        Lexer lexer = new Lexer();
        lexer.setDDFA(dfa);
        lexer.compile();

        assertEquals(-1, lexer.match("fee"));
        assertEquals(-1, lexer.match("fie"));
        assertEquals(0, lexer.match("oie"));
        assertEquals(1, lexer.match("fde"));
        assertEquals(2, lexer.match("fef"));
        assertEquals(2, lexer.match("fe"));
    }
}
