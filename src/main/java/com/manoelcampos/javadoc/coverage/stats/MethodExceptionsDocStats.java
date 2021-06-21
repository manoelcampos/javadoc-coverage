/*
 * Copyright 2017-2017 Manoel Campos da Silva Filho
 *
 * Licensed under the General Public License Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.gnu.org/licenses/gpl-3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.manoelcampos.javadoc.coverage.stats;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Tag;

/**
 * Computes JavaDoc coverage statistics for the exceptions thrown by a specific method.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class MethodExceptionsDocStats extends MethodTagsDocStats {

    /**
     * Instantiates an object to compute JavaDoc coverage statistics for the exceptions thrown
     * by a method/constructor.
     *
     * @param doc an object which enables reading the JavaDoc documentation for the method the exceptions belong to
     */
    MethodExceptionsDocStats(final ExecutableMemberDoc doc) {
        super(doc);
    }

    @Override
    public String getType() {
        return "Exceptions";
    }

    /**
     * Gets the total number of exceptions in the method for which the JavaDoc
     * coverage statistics is being computed.
     *
     * <p>
     * There may be documented exceptions which aren't declared.
     * Usually, undeclared exceptions are unchecked exceptions,
     * which aren't required to be declared.
     * Due to such conditions, the number of existing exceptions will be those: <br>
     * <b>declared but not documented</b> + <b>documented but not declared</b> + <b>documented and declared</b>.
     * </p>
     * @return the total number of exceptions
     */
    @Override
    public long getNumberOfDocumentableMembers() {
        return getDeclaredButNotDocumentedExceptionsNumber() +
                getDocumentedButNotDeclaredExceptionsNumber() +
                getDeclaredAndDocumentedExceptionsNumber();
    }

    /**
     * Gets the number of exceptions which are documented in the JavaDoc but not actually declared in the
     * method signature.
     *
     * <p>Unchecked exceptions can be in this situation, since they are not required to be declared.
     * If a checked exception is documented but not declared, usually the IDEs report an issue
     * that the developer should fix.
     * </p>
     *
     * @return
     */
    private long getDocumentedButNotDeclaredExceptionsNumber() {
        return
                getDocumentedTagStream()
                .filter(tag -> getDeclaredExceptionsStream().noneMatch(ex -> isExceptionEqualsToJavaDocTag(ex, tag)))
                .count();
    }

    /**
     * Gets the number of exceptions which are declared but not documented in the JavaDoc.
     * @return
     */
    private long getDeclaredButNotDocumentedExceptionsNumber() {
        return getDeclaredExceptionsStream()
                .filter(ex -> getDocumentedTagStream().noneMatch(tag -> isExceptionEqualsToJavaDocTag(ex, tag)))
                .count();
    }

    /**
     * Gets the number of exceptions which are both declared and documented in the JavaDoc.
     * @return
     */
    private long getDeclaredAndDocumentedExceptionsNumber() {
        return getDeclaredExceptionsStream()
                .filter(ex -> getDocumentedTagStream().anyMatch(tag -> isExceptionEqualsToJavaDocTag(ex, tag)))
                .count();
    }

    /**
     * Gets a Stream of thrown exceptions declared in the method's signature.
     *
     * @return
     */
    private Stream<ClassDoc> getDeclaredExceptionsStream() {
        return Arrays.stream(getDoc().thrownExceptions());
    }

    /**
     * Checks if a given exception is equals to a given @throws tag get from a method JavaDoc,
     * meaning the tag represents the documentation for that exception.
     *
     * @param exception the exception to check
     * @param tag the JavaDoc @throws tag to check
     * @return true if the exception matches with the JavaDoc @throws tag
     */
    private boolean isExceptionEqualsToJavaDocTag(final ClassDoc exception, final Tag tag) {
        /*
        The exception class parsed by the Doclet always starts with the package name,
        even if the package is not included in the throws clause.
        On the other hand, the JavaDoc tag used by a developer to document the
        exception usually isn't prefixed with its package.
         */
        return exception.toString().endsWith(getExceptionClassFromTag(tag));
    }

    /**
     * Gets the class of an exception from its JavaDoc tag.
     * @param tag the JavaDoc tag representing the documentation of a method's exception
     * @return the class of the exception
     */
    private String getExceptionClassFromTag(final Tag tag) {
        final Matcher matcher = Pattern.compile("\\w*").matcher(tag.text());
        return matcher.find() ? matcher.group() : tag.text();
    }

    @Override
    public String getTagName() {
        return "@throws";
    }
}
