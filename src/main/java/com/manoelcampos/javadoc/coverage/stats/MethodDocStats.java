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

import com.manoelcampos.javadoc.coverage.Utils;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Tag;

/**
 * Computes JavaDoc coverage statistics for a method/constructor
 * and its members, namely parameters and thrown exceptions.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class MethodDocStats extends MembersDocStats {
    /**
     * This value is added to the number of elements in order to count the method itself as an element
     * which can be documented.
     *
     * @see #getMembersOfElement()
     * @see #getDocumentedMembersPercent()
     */
    private static final int METHOD_DOC = 1;

    private final ExecutableMemberDoc doc;
    private final MethodParamsDocStats paramsStats;
    private final MethodExceptionsDocStats thrownExceptionsStats;

    /**
     * Instantiates an object to compute JavaDoc coverage statistics for a method/constructor.
     *
     * @param doc the element which enables reading the method's JavaDoc documentation
     */
    MethodDocStats(final ExecutableMemberDoc doc) {
        this.doc = doc;
        this.paramsStats = new MethodParamsDocStats(doc);
        this.thrownExceptionsStats = new MethodExceptionsDocStats(doc);
        this.enablePrintIfNoMembers();
    }

    public String getMethodName() {
        return doc.name();
    }

    @Override
    public String getType() {
        return doc.isConstructor() ? "Constructor" : "Method";
    }

    /**
     * Gets the JavaDoc coverage statistics for the method/constructor's parameters.
     *
     * @return
     */
    public MethodParamsDocStats getParamsStats() {
        return paramsStats;
    }

    /**
     * Gets the JavaDoc coverage statistics for the exceptions thrown by the method/constructor.
     *
     * @return
     */
    public MethodExceptionsDocStats getThrownExceptionsStats() {
        return thrownExceptionsStats;
    }

    public boolean isReturnDocumented() {
        return Arrays.stream(doc.tags()).filter(t -> t.name().equals("@return")).map(Tag::text).anyMatch(Utils::isNotStringEmpty);
    }

    private boolean isOverridden() {
        if (doc.isMethod()) {
            MethodDoc curMethod = (MethodDoc) doc;
            ClassDoc[] interfaces = curMethod.containingClass().interfaces();
            ClassDoc superclass = curMethod.containingClass().superclass();

            for (ClassDoc inter : interfaces) {
                for (MethodDoc method : inter.methods()) {
                    if (curMethod.overrides(method)) {
                        return true;
                    }
                }
            }

            if (superclass != null) {
                for (MethodDoc method : superclass.methods()) {
                    if (curMethod.overrides(method)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks if the method is void or is a constructor.
     * Either ways, it doesn't have a return value to be documented.
     *
     * @return true if the method is void or is a constructor, false otherwise
     */
    public boolean isVoidMethodOrConstructor() {
        return isVoidMethod() || doc.isConstructor();
    }

    private boolean isVoidMethod() {
        return doc.isMethod() && ((MethodDoc) doc).returnType() == null;
    }

    @Override
    public boolean isDocumented() {
        return Utils.isElementDocumented(doc.getRawCommentText());
    }

    @Override
    public long getNumberOfDocumentedMembers() {
        final int returnCount = (!isVoidMethodOrConstructor() && isReturnDocumented()) ? 1 : 0;

        // @formatter:off
        final long documentedMembers = Utils.boolToInt(isDocumented())
                + paramsStats.getNumberOfDocumentedMembers()
                + thrownExceptionsStats.getNumberOfDocumentedMembers()
                + returnCount;
        // @formatter:on

        /*
         * If an overridden method isn't documented at all, it doesn't matter because its documentation is optional. The superclass is
         * accountable to document the method. This way, the method is counted as completely documented.
         */
        if (isOverridden() && documentedMembers == 0) {
            return getNumberOfDocumentableMembers();
        }

        return documentedMembers;
    }

    @Override
    public long getNumberOfDocumentableMembers() {
        final int returnCount = isVoidMethodOrConstructor() ? 0 : 1;

        // @formatter:off
        return METHOD_DOC +
                paramsStats.getNumberOfDocumentableMembers() +
                thrownExceptionsStats.getNumberOfDocumentableMembers() +
                returnCount;
        // @formatter:on
    }
}
