package edu.umass.ciir.judge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * @author jfoley.
 */
@RunWith(Parameterized.class)
public class StorageBackendTest {
  private final StorageBackend backend;

  public StorageBackendTest(StorageBackend sb) {
    this.backend = sb;
    backend.clear();
  }

  // run these tests with all backends :)
  @Parameterized.Parameters
  public static Collection<Object[]> testParameters() throws SQLException, ClassNotFoundException {

    return Arrays.asList(new Object[][]{
        {new MemStorageBackend()},
        {new LocalDBBackend("test-scratch/StorageBackendTest")}
    });
  }
  
  @Test
  public void testStuff() throws Exception {
    // make sure that we can't get a judgment yet
    assertNull(backend.getJudgment("user1", "q0", "doc0"));

    Judgment insertMe = new Judgment("user1", "q0", "doc0", "0", "");
    backend.submit(insertMe);
    Judgment user1_q0_doc0 = backend.getJudgment("user1", "q0", "doc0");

    assertNotNull(user1_q0_doc0);
    assertEquals(insertMe.value.judgment, user1_q0_doc0.value.judgment);
    assertEquals(insertMe.value.note, user1_q0_doc0.value.note);
    assertEquals(insertMe, user1_q0_doc0);

    // test changing your mind

    Judgment changedMyMind = new Judgment("user1", "q0", "doc0", "1", "");
    backend.submit(changedMyMind);
    user1_q0_doc0 = backend.getJudgment("user1", "q0", "doc0");

    assertNotNull(user1_q0_doc0);
    assertNotEquals(insertMe.value.judgment, user1_q0_doc0.value.judgment);
    assertEquals(changedMyMind.value.judgment, user1_q0_doc0.value.judgment);
    assertEquals(user1_q0_doc0.value.note, user1_q0_doc0.value.note);

    // "That's why you should always leave a note."
    //     - George Bluth
    Judgment leaveANote = new Judgment("user1", "q0", "doc0", "1", "This one is confusing!");
    backend.submit(leaveANote);
    user1_q0_doc0 = backend.getJudgment("user1", "q0", "doc0");

    assertNotNull(user1_q0_doc0);
    assertEquals(leaveANote.value.judgment, user1_q0_doc0.value.judgment);
    assertEquals(leaveANote.value.note, user1_q0_doc0.value.note);
  }


}
