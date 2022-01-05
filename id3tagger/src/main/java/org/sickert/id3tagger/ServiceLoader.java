package org.sickert.id3tagger;

public class ServiceLoader {

  public static <T> T load(Class<T> serviceClass) {
    java.util.ServiceLoader<T> services = java.util.ServiceLoader.load(serviceClass);
    for (T service : services) {
      return service;
    }
    throw new IllegalStateException("No service provider found for " + serviceClass.getName());
  }
}
