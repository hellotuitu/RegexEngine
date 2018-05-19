package engine;

import engine.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static  org.junit.jupiter.api.Assertions.*;

/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-19 13:20
 **/
public class TestEngine {

    @ParameterizedTest
    @MethodSource("matchReAndTargetProvider")
    void testMatch(String re, String target) throws Exception {
        assertEquals(true, Engine.match(re, target));
    }

    static Stream<Arguments> matchReAndTargetProvider() {
        return Stream.of(
                Arguments.of("a(b|c)*", "a"),
                Arguments.of("a(b|c)*", "ab"),
                Arguments.of("a(b|c)*", "ac"),
                Arguments.of("a(b|c)*", "abccccc"),
                Arguments.of("a(b|c)*", "accccbb"),

                Arguments.of("f(ef|if)*", "fef"),
                Arguments.of("f(ef|if)*", "fif"),
                Arguments.of("f(ef|if)*", "fefif"),

                Arguments.of("a(b(cd|ef)|g)", "ag"),
                Arguments.of("a(b(cd|ef)|g)", "abcd"),
                Arguments.of("a(b(cd|ef)|g)", "abef")
                );
    }
}
