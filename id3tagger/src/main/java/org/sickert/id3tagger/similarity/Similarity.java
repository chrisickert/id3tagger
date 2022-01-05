package org.sickert.id3tagger.similarity;

import javax.annotation.Nonnull;
import org.sickert.id3tagger.term.Term;

/** @author Christian Sickert */
public interface Similarity {

  public static final Similarity NORMALIZED_LEVENSHTEIN_DISTANCE =
      new NormalizedLevenshteinDistance();

  public static final Similarity JARO_WINKLER_DISTANCE = new JaroWinklerDistance();

  public static final Similarity NORMALIZED_FUZZY_LEVENSHTEIN_DISTANCE =
      new NormalizedFuzzyLevenshteinDistance(NORMALIZED_LEVENSHTEIN_DISTANCE, 0.75d);

  public static final Similarity SIMILAR_TERMS_COUNT =
      new SimilarTermsCount(JARO_WINKLER_DISTANCE, 0.92d);

  public static final Similarity NORMALIZED_SIMILAR_TERMS_COUNT =
      new NormalizedSimilarTermsCount(JARO_WINKLER_DISTANCE, 0.9d);

  double similarity(@Nonnull Term term1, @Nonnull Term term2);

  double getEqualReleaseYearsReward();

  double getEqualTrackNumbersReward();
}
