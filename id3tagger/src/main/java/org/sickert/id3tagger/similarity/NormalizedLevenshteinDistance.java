package org.sickert.id3tagger.similarity;

import javax.annotation.Nonnull;
import org.sickert.id3tagger.term.Term;

/** @author Christian Sickert */
public class NormalizedLevenshteinDistance implements Similarity {

  @Override
  public double similarity(@Nonnull Term term1, @Nonnull Term term2) {
    int length1 = term1.length();
    int length2 = term2.length();

    // handle trivial cases
    if (length1 == 0 && length2 == 0) {
      return 1d;
    } else if (length1 == 0 || length2 == 0) {
      return 0d;
    }

    int previousCosts[] = new int[length1 + 1];
    int costs[] = new int[length1 + 1];
    int _costs[]; // placeholder to assist in swapping previousCosts and costs

    // indexes into input terms
    int i; // iterates through term1
    int j; // iterates through term2

    Term term1_i; // ith entry of term1
    Term term2_j; // jth entry of term2

    int cost;

    for (i = 0; i <= length1; i++) {
      previousCosts[i] = i;
    }

    for (j = 1; j <= length2; j++) {
      term2_j = term2.entryAt(j - 1);
      costs[0] = j;

      for (i = 1; i <= length1; i++) {
        term1_i = term1.entryAt(i - 1);
        cost = term1_i.equals(term2_j) ? 0 : 1;
        costs[i] =
            Math.min(Math.min(costs[i - 1] + 1, previousCosts[i] + 1), previousCosts[i - 1] + cost);
      }

      // copy current distance counts to 'previous row' distance counts
      _costs = previousCosts;
      previousCosts = costs;
      costs = _costs;
    }

    // our last action in the above loop was to switch costs and previousCosts, so previousCosts
    // now
    // actually has the most recent cost counts, i.e. the levenshtein distance
    int levenshteinDistance = previousCosts[length1];

    // return the normalized levenshtein distance
    return normalize(levenshteinDistance, length1, length2);
  }

  private double normalize(int levenshteinDistance, int term1Length, int term2Length) {
    if (levenshteinDistance == 0) {
      return 1d;
    }

    int maximumTermLength = Math.max(term1Length, term2Length);
    if (maximumTermLength <= levenshteinDistance) {
      return 0d;
    }

    return 1d - ((double) levenshteinDistance / (double) maximumTermLength);
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
