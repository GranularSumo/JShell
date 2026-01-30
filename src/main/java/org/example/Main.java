package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.EnumSet;

import org.example.commands.Builtins;
import org.example.evaluator.Evaluator;
import org.example.evaluator.IoContext;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter out = new PrintWriter(System.out);
    PrintWriter err = new PrintWriter(System.err);
    IoContext stdIO = new IoContext(in, out, err, EnumSet.noneOf(IoContext.Resource.class));
    ShellContext ctx = new ShellContext(stdIO);
    Evaluator evaluator = new Evaluator(ctx, new Builtins());
    Shell shell = new Shell(ctx, evaluator);
    shell.run();
  }
}
