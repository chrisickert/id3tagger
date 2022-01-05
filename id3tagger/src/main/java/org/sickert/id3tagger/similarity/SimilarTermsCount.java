package org.sickert.id3tagger.similarity;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.sickert.id3tagger.term.Term;

/** @author Christian Sickert */
public class SimilarTermsCount implements Similarity {

  @Nonnull private Similarity entrySimilarity;

  private double threshold;

  public SimilarTermsCount(@Nonnull Similarity entrySimilarity, double threshold) {
    this.entrySimilarity = entrySimilarity;
    this.threshold = threshold;
  }

  @Override
  public double similarity(@Nonnull Term term1, @Nonnull Term term2) {
    List<Term> termList2 = new ArrayList<>(term2.length());
    for (Term term : term2) {
      termList2.add(term);
    }

    int count = 0;
    for (Term termOne : term1) {
      for (Term termTwo : termList2) {
        if (entrySimilarity.similarity(termOne, termTwo) >= threshold) {
          count++;
          termList2.remove(termTwo);
          break;
        }
      }
    }

    return (double) count;
  }

  @Override
  public double getEqualReleaseYearsReward() {
    return 1d;
  }

  @Override
  public double getEqualTrackNumbersReward() {
    return 1d;
  }
}
