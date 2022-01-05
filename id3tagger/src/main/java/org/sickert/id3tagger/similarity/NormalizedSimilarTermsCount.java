package org.sickert.id3tagger.similarity;

import javax.annotation.Nonnull;
import org.sickert.id3tagger.term.Term;

/** @author Christian Sickert */
public class NormalizedSimilarTermsCount extends SimilarTermsCount {

  public NormalizedSimilarTermsCount(@Nonnull Similarity entrySimilarity, double threshold) {
    super(entrySimilarity, threshold);
  }

  @Override
  public double similarity(@Nonnull Term term1, @Nonnull Term term2) {
    double absoluteCount = super.similarity(term1, term2);
    return normalize(absoluteCount, term1.length(), term2.length());
  }

  private double normalize(double absoluteCount, int term1Length, int term2Length) {
    if (absoluteCount == 0) {
      return 0d;
    }

    int maximumTermLength = Math.max(term1Length, term2Length);
    if (maximumTermLength <= absoluteCount) {
      return 1d;
    }

    return absoluteCount / (double) maximumTermLength;
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
