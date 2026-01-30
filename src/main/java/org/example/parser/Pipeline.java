package org.example.parser;

import java.util.List;

public record Pipeline(List<Command> commands) {
  // TODO: need to implement this at some point.
  //
  // pipe Nodes need to redirect the content of stdout from the left command into
  // the stdin of the right command.
  // that means that if the left command has a redirected output then the pipeline
  // basically gets bypassed:
  //
  // printf "%s\n" "hello this is a test" "and another" "one final test" >
  // test.txt | grep 'this'
  // result: blank
  //
  // printf "%s\n" "hello this is a test" "and another" "one final test" | grep
  // 'this'
  // result: hello this is a test
}
