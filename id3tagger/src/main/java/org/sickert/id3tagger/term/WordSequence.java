package org.sickert.id3tagger.term;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

/** @author Christian Sickert */
public class WordSequence extends Term {

  @Nonnull private Word[] words;

  private static final Term NULL_WORD = new Word(StringUtils.EMPTY);

  public WordSequence(@Nonnull String[] terms) {
    this.words = new Word[terms.length];
    for (int i = 0; i < terms.length; i++) {
      this.words[i] = new Word(terms[i]);
    }
  }

  @Nonnull
  @Override
  public Term entryAt(int index) {
    if (index < 0 || index >= words.length) {
      throw new IndexOutOfBoundsException(
          "Illegal index: "
              + index
              + ". Must be greater than 0 and less than "
              + words.length
              + ".");
    }

    Word wordAtIndex = words[index];
    if (wordAtIndex == null) {
      return NULL_WORD;
    }
    return wordAtIndex;
  }

  @Override
  public int length() {
    return words.length;
  }

  public boolean isEmpty() {
    return length() == 0;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof WordSequence && this.toString().equals(obj.toString());
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public String toString() {
    return StringUtils.join(words, " ");
  }

  @Nonnull
  public WordSequence removeFirstOccurrenceOf(@Nullable WordSequence anotherWordSequence) {
    if (anotherWordSequence == null) {
      return this;
    }

    String thisAsString = this.toString();
    String theOtherWordSequenceAsString = anotherWordSequence.toString();
    return new WordSequence(
        Terms.splitTextIntoWords(
            StringUtils.replaceOnce(
                thisAsString, theOtherWordSequenceAsString, StringUtils.EMPTY)));
  }
}
