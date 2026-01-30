package org.example;

import java.io.IOException;

import org.example.evaluator.Evaluator;

public class Shell {

  private ShellContext ctx;
  private final Evaluator evaluator;

  public Shell(ShellContext ctx, Evaluator evaluator) {
    this.ctx = ctx;
    this.evaluator = evaluator;
  }

  public void run() {
    try {
      while (ctx.shouldContinue()) {
        prompt();
        String input = readInput();

        if (input == null) {
          break;
        }

        if (input.isBlank()) {
          continue;
        }

        evaluator.evaluate(input);
      }
    } catch (IOException e) {
      ctx.err().println("I/O error: " + e.getMessage());
    }
  }

  private void prompt() {
    ctx.out().print("\u001B[1;34m");
    ctx.out().print(ctx.getCwd().toString() + " $ ");
    ctx.out().print("\u001B[0m");
    ctx.out().flush();
  }

  private String readInput() throws IOException {
    StringBuilder input = new StringBuilder();
    while (true) {
      String line = ctx.in().readLine();

      if (line == null) {
        return null;
      }

      if (line.endsWith("\\")) {
        input.append(line, 0, line.length() - 1);
      } else {
        input.append(line);
        break;
      }
    }
    return input.toString();
  }
}
