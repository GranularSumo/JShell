package org.example.parser;

import java.util.List;

public record Command(List<String> args, List<Redirect> redirects) {
  public Command {
    args = List.copyOf(args != null ? args : List.of());
    redirects = List.copyOf(redirects != null ? redirects : List.of());

    if (args.isEmpty()) {
      throw new IllegalArgumentException("Command must have at least one argument (the name of the command to run)");
    }
  }

  public String commandName() {
    return args.get(0);
  }

  public List<String> arguments() {
    return args.subList(1, args.size());
  }

}
