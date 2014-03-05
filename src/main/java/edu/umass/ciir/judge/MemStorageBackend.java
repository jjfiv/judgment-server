package edu.umass.ciir.judge;

import java.util.*;

/**
 * @author jfoley.
 */
public class MemStorageBackend implements StorageBackend {
  public Map<Judgment.Key, Judgment.Value> judgments;

  public MemStorageBackend() {
    this.judgments = new HashMap<Judgment.Key, Judgment.Value>();
  }

  @Override
  public Judgment getJudgment(String user, String qid, String doc) {
    Judgment.Key key = new Judgment.Key(user, qid, doc);
    Judgment.Value val = judgments.get(key);
    if(val == null) return null;
    return new Judgment(key, val);
  }

  @Override
  public void submit(Judgment judgment) {
    System.out.println("submit: "+judgment.toTSV());
    judgments.put(judgment.key, judgment.value);
  }
}
