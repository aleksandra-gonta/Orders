  /*Generic interface providing validate method*/
package org.example.validator.generic;

import java.util.Map;

public interface Validator<T> {
    Map<String, String> validate(T t);
    boolean hasErrors();
}
