package org.ow2.osgi.scanner.test.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Collection;

/**
* Created by IntelliJ IDEA.
* User: guillaume
* Date: 26/01/11
* Time: 22:12
* To change this template use File | Settings | File Templates.
*/
public class IsIn<T> extends BaseMatcher<T> {

    private final Collection<T> collection;

       public IsIn(Collection<T> collection) {
           this.collection = collection;
       }

       public boolean matches(Object o) {
           return collection.contains(o);
       }

       public void describeTo(Description buffer) {
           buffer.appendText("one of ");
           buffer.appendValueList("{", ", ", "}", collection);
       }
}
