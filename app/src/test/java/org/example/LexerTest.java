package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.example.Lexer.Lexer;
import org.example.Lexer.Token;
import org.example.Lexer.Token.TokenType;
import org.junit.jupiter.api.Test;

class LexerTest {

  @Test
  public void tokenizesSimpleInput() {
    List<Token> tokens = Lexer.tokenize("Hello World.");
    assertFalse(tokens.isEmpty());

    List<Token> expectedTokens = new ArrayList<>();
    expectedTokens.add(new Token(TokenType.WORD, "Hello"));
    expectedTokens.add(new Token(TokenType.WORD, "World."));
    assertEquals(expectedTokens, tokens);
  }

  @Test
  public void correctlyTokenizesRedirects() {
    List<Token> tokens = Lexer.tokenize("This is a redirect 1> target.filetype");
    assertEquals(6, tokens.size());
    assertEquals(TokenType.OP, tokens.get(4).type());
  }
}
