package org.sickert.id3tagger.term;

import java.util.Iterator;
import javax.annotation.Nonnull;

/** @author Christian Sickert */
public abstract class Term implements Iterable<Term> {

  private class TermIterator implements Iterator<Term> {

    private int nextIndex = 0;

    @Override
    public boolean hasNext() {
      return length() > nextIndex;
    }

    @Override
    public Term next() {
      return entryAt(nextIndex++);
    }
  }

  @Nonnull
  public abstract Term entryAt(int index);

  public abstract int length();

  @Override
  @Nonnull
  public Iterator<Term> iterator() {
    return new TermIterator();
  }
}
