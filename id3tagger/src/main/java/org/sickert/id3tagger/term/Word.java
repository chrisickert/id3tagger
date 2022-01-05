package org.sickert.id3tagger.term;

import javax.annotation.Nonnull;

/** @author Christian Sickert */
public class Word extends Term {

  @Nonnull private String word;

  public Word(@Nonnull String word) {
    this.word = word;
  }

  @Nonnull
  @Override
  public Term entryAt(int index) {
    char characterAtIndex = word.charAt(index);
    return new Word(Character.toString(characterAtIndex));
  }

  @Override
  public int length() {
    return word.length();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Word && word.equals(((Word) obj).word);
  }

  @Override
  public int hashCode() {
    return word.hashCode();
  }

  @Override
  public String toString() {
    return word;
  }
}
