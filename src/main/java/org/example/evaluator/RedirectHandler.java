package org.example.evaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.List;

import org.example.evaluator.IoContext.Resource;
import org.example.parser.Redirect;

public class RedirectHandler {

  public static IoContext applyAllToIoContext(List<Redirect> redirects, IoContext io) throws IOException {
    if (redirects.isEmpty()) {
      return io;
    }

    BufferedReader in = io.in();
    PrintWriter out = io.out();
    PrintWriter err = io.err();
    EnumSet<IoContext.Resource> ownedResources = EnumSet.noneOf(IoContext.Resource.class);

    for (Redirect r : redirects) {
      switch (r.type()) {
        case INPUT -> {
          if (ownedResources.contains(Resource.IN)) {
            in.close();
          }
          in = new BufferedReader(new FileReader(r.target()));
          ownedResources.add(Resource.IN);
        }
        case OUTPUT, OUTPUT_APPEND -> {
          if (ownedResources.contains(Resource.OUT)) {
            out.close();
          }
          out = createWriter(r);
          ownedResources.add(Resource.OUT);
        }
        case ERROR, ERROR_APPEND -> {
          if (ownedResources.contains(Resource.ERR)) {
            err.close();
          }
          err = createWriter(r);
          ownedResources.add(Resource.ERR);
        }
        case ALL_OUTPUT, ALL_APPEND -> {
          if (ownedResources.contains(Resource.OUT)) {
            out.close();
          }
          if (ownedResources.contains(Resource.ERR)) {
            err.close();
          }
          PrintWriter pw = createWriter(r);
          out = err = pw;
          ownedResources.add(Resource.OUT);
          ownedResources.add(Resource.ERR);
        }
        default -> throw new IOException("Invalid RedirectType: " + r.type());
      }
    }
    return new IoContext(in, out, err, ownedResources);
  }

  private static PrintWriter createWriter(Redirect r) throws IOException {
    boolean append = r.isAppendMode();
    return new PrintWriter(new FileWriter(r.target(), append), append);
  }

  public static void applyAllToProcess(List<Redirect> redirects, ProcessBuilder builder, IoContext io)
      throws IOException {

    builder.inheritIO();

    for (Redirect r : redirects) {
      File f = checkAndPrepareFile(r);
      updateBuilderForRedirect(r, builder, f);
    }

  }

  private static void updateBuilderForRedirect(Redirect r, ProcessBuilder builder, File f) throws IOException {

    switch (r.type()) {
      case INPUT -> builder.redirectInput(f);
      case OUTPUT -> builder.redirectOutput(f);
      case OUTPUT_APPEND -> builder.redirectOutput(ProcessBuilder.Redirect.appendTo(f));
      case ERROR -> builder.redirectError(f);
      case ERROR_APPEND -> builder.redirectError(ProcessBuilder.Redirect.appendTo(f));
      case ALL_OUTPUT -> {
        builder.redirectOutput(f);
        builder.redirectError(f);
      }
      case ALL_APPEND -> {
        builder.redirectOutput(ProcessBuilder.Redirect.appendTo(f));
        builder.redirectError(ProcessBuilder.Redirect.appendTo(f));
      }
      default -> throw new IOException("Invalid Redirect");
    }
  }

  private static File checkAndPrepareFile(Redirect r) throws IOException {
    Path p = Path.of(r.target());
    switch (r.type()) {
      case INPUT: {
        return checkFileExistsAndReturn(p);
      }
      case OUTPUT, ERROR, ALL_OUTPUT: {
        return checkForWriteAndTruncate(p);
      }
      case OUTPUT_APPEND, ERROR_APPEND, ALL_APPEND: {
        return checkForWriteAndAppend(p);
      }
      default: {
        throw new IOException("Invalid Redirect");
      }
    }
  }

  private static File checkFileExistsAndReturn(Path p) throws IOException {
    if (Files.exists(p)) {
      return p.toFile();
    }
    throw new IOException("file: " + p.toString() + " does not have read access");
  }

  private static File checkForWriteAndTruncate(Path p) throws IOException {
    try {
      Files.write(p, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
      return p.toFile();
    } catch (IOException e) {
      throw new IOException("file: " + p.toString() + " does not have write access");
    }
  }

  private static File checkForWriteAndAppend(Path p) throws IOException {
    try {
      Files.write(p, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      return p.toFile();
    } catch (IOException e) {
      throw new IOException("file: " + p.toString() + " does not have write access");
    }
  }

}
