package org.example.evaluator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumSet;

public record IoContext(BufferedReader in, PrintWriter out, PrintWriter err, EnumSet<Resource> ownedResources) {
  public enum Resource {
    IN,
    OUT,
    ERR,
  }

  public void closeResources() throws IOException {
    if (ownedResources.contains(Resource.IN))
      in.close();
    if (ownedResources.contains(Resource.OUT))
      out.close();
    if (ownedResources.contains(Resource.ERR))
      err.close();
  }
}
