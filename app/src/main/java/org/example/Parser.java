package org.example;

import java.util.List;

import org.example.Lexer.Lexer;
import org.example.Lexer.Token;

public class Parser {
  private List<Token> tokenList;

  public Parser(String input) {
    tokenList = Lexer.tokenize(input);
  }

  public static void parse(String input) {
    Parser parser = new Parser(input);
    parser.parse();
  }

  private void parse() {
    System.out.println("do a thing");
  }

}

/*
 * TODO: build an AST from the tokenList.
 * 
 *
 * Spec:
 *
 * Nodes:
 *
 * Word: String content
 * Redirect: RedirectType type, Word target
 * Command: List<Word> args, List<Redirect> redirects
 * Pipeline: List<Command> commands (will implement this at a later date.)
 *
 * can use this to properly truncate files on multiple redirects I think:
 * https://stackoverflow.com/a/36316178
 *
 * need to look further into how pipelines connect the outputs of commands.
 * I believe that the AST will need to be set up so that redirected outputs beat
 * pipelines.
 */
