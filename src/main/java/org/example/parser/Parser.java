package org.example.parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.example.lexer.Token;
import org.example.lexer.Lexer;

public class Parser {
  private List<Token> tokenList;
  private int currentIndex = 0;

  public Parser(String input) {
    tokenList = Lexer.tokenize(input.trim());
  }

  public static Command parse(String input) throws ParseException {
    Parser parser = new Parser(input);
    return parser.parseCommand();
  }

  private Command parseCommand() throws ParseException {
    List<String> args = new ArrayList<>();
    List<Redirect> redirects = new ArrayList<>();

    while (currentIndex < tokenList.size()) {
      Token t = tokenList.get(currentIndex);

      switch (t.type()) {
        case WORD: {
          args.add(t.value());
          currentIndex++;
          break;
        }
        case REDIRECT: {
          Redirect redirect = parseRedirect();
          redirects.add(redirect);
          break;
        }
        default:
          throw new ParseException("Unexpected token: " + t, currentIndex);
      }
    }
    return new Command(args, redirects);
  }

  private Redirect parseRedirect() throws ParseException {
    Token t = tokenList.get(currentIndex);
    currentIndex++;

    if (currentIndex >= tokenList.size()) {
      throw new ParseException("Redirect operator requires a target file", currentIndex);
    }

    try {
      RedirectType type = RedirectType.fromString(t.value());
      Token targetToken = tokenList.get(currentIndex);

      if (targetToken.type() != Token.TokenType.WORD) {
        throw new ParseException("Redirect target must be a word, got: TokenType." + targetToken.type(), currentIndex);
      }

      currentIndex++;
      return new Redirect(type, targetToken.value());
    } catch (IllegalArgumentException e) {
      throw new ParseException("Invalid redirect: " + t.value(), currentIndex);
    }
  }
}
