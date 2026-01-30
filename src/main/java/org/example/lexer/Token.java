package org.example.lexer;

public record Token(TokenType type, String value) {
  public enum TokenType {
    WORD,
    REDIRECT,
  }
}
