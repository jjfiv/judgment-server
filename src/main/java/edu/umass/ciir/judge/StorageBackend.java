package edu.umass.ciir.judge;

import java.util.List;
import java.util.Set;

/**
 * @author jfoley.
 */
public interface StorageBackend {
  public Judgment getJudgment(String user, String qid, String doc);
  public void submit(Judgment judgment);
}
