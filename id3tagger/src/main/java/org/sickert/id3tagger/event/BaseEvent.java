package org.sickert.id3tagger.event;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;

/** @author Christian Sickert */
public abstract class BaseEvent {

  protected static final ResourceBundle EVENT_MESSAGES =
      ResourceBundle.getBundle(BaseEvent.class.getPackage().getName() + ".EventMessages");

  protected boolean success;

  public BaseEvent(boolean success) {
    this.success = success;
  }

  public boolean wasSuccessful() {
    return success;
  }

  @Nonnull
  public abstract String getSuccessMessage();

  @Nonnull
  public abstract String getErrorMessage();

  @Override
  public String toString() {
    if (wasSuccessful()) {
      return getSuccessMessage();
    } else {
      return getErrorMessage();
    }
  }
}
