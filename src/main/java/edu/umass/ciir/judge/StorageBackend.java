package edu.umass.ciir.judge;

/**
 * @author jfoley.
 */
public interface StorageBackend {
  public Judgment getJudgment(String user, String qid, String doc);
  public void submit(Judgment judgment);
  public void clear();
}
