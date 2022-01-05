package org.sickert.id3tagger.term;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

/** @author Christian Sickert */
public class Terms {

  @Nonnull
  public static String[] splitTextIntoWords(@Nonnull String text) {
    String[] termArrayWithPotentiallyEmptyTerms =
        text.split(" |_|\\-|\\(|\\)|\\[|\\]\\{|\\}|\\.|/|\\||:");
    List<String> termListWithoutAnyEmptyTerms =
        Arrays.asList(termArrayWithPotentiallyEmptyTerms).stream()
            .filter(term -> term.length() > 0)
            .collect(Collectors.toList());
    return termListWithoutAnyEmptyTerms.toArray(new String[termListWithoutAnyEmptyTerms.size()]);
  }

  @Nonnull
  public static String normalizeUmlautsAndAccents(@Nonnull String term) {
    return term.replaceAll("ä", "ae")
        .replaceAll("ö", "oe")
        .replaceAll("ü", "ue")
        .replaceAll("Ä", "Ae")
        .replaceAll("Ö", "Oe")
        .replaceAll("Ü", "Ue")
        .replaceAll("ß", "ss")
        .replaceAll("é", "e")
        .replaceAll("è", "e")
        .replaceAll("ê", "e")
        .replaceAll("á", "a")
        .replaceAll("à", "a")
        .replaceAll("â", "a")
        .replaceAll("ó", "o")
        .replaceAll("ò", "o")
        .replaceAll("ô", "o")
        .replaceAll("ú", "u")
        .replaceAll("ù", "u")
        .replaceAll("û", "u")
        .replaceAll("É", "E")
        .replaceAll("È", "E")
        .replaceAll("Ê", "E");
  }

  @Nonnull
  public static String[] normalizeUmlautsAndAccents(@Nonnull String[] words) {
    String[] wordsWithNormalizedUmlauts = new String[words.length];
    for (int i = 0; i < words.length; i++) {
      if (words[i] != null) {
        wordsWithNormalizedUmlauts[i] = Terms.normalizeUmlautsAndAccents(words[i]);
      } else {
        wordsWithNormalizedUmlauts[i] = null;
      }
    }
    return wordsWithNormalizedUmlauts;
  }

  @Nonnull
  public static String[] toLowerCase(@Nonnull String[] words) {
    String[] lowerCaseWords = new String[words.length];
    for (int i = 0; i < words.length; i++) {
      if (words[i] != null) {
        lowerCaseWords[i] = words[i].toLowerCase();
      } else {
        lowerCaseWords[i] = null;
      }
    }
    return lowerCaseWords;
  }
}
