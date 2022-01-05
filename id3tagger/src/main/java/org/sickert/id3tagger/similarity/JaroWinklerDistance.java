package org.sickert.id3tagger.similarity;

import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.sickert.id3tagger.term.Term;

/** @author Christian Sickert */
public class JaroWinklerDistance implements Similarity {

  @Override
  public double similarity(@Nonnull Term term1, @Nonnull Term term2) {
    return StringUtils.getJaroWinklerDistance(term1.toString(), term2.toString());
  }

  @Override
  public double getEqualTrackNumbersReward() {
    return 0.125d;
  }

  @Override
  public double getEqualReleaseYearsReward() {
    return 0.125d;
  }
}
