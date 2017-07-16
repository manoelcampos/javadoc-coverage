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

import com.manoelcampos.javadoc.coverage.Utils;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Tag;

import java.util.Arrays;

/**
 * Computes statistics about the JavaDocs of a method
 * and its members, such as: parameters and thrown exceptions.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class MethodDocStats extends MembersDocStats {
    private final ExecutableMemberDoc doc;
    private final MethodParamsDocStats paramsStats;
    private final MethodExceptionsDocStats thrownExceptions;

    MethodDocStats(final ExecutableMemberDoc doc) {
        this.doc = doc;
        this.paramsStats = new MethodParamsDocStats(doc);
        this.thrownExceptions = new MethodExceptionsDocStats(doc);
        this.enablePrintIfNoMembers();
    }

    /**
     * Indicates if the return value of the method is documented.
     *
     * @return true if the return value is documented, false otherwise.
     */
    public boolean isReturnValueDocumented() {
         /* @todo If the return is void or is a constructor, it don't have to be documented.
         This way, the method should return true.*/
        return Arrays.stream(doc.tags())
                .filter(tag -> "@return".equals(tag.name()))
                .map(Tag::text)
                .anyMatch(Utils::isNotStringEmpty);
    }

    public String getMethodName() {
        return doc.name();
    }

    @Override
    public String getType() {
        return doc.isConstructor() ? "Constructor" : "Method";
    }

    public MethodParamsDocStats getParamsStats() {
        return paramsStats;
    }

    public MethodExceptionsDocStats getThrownExceptions() {
        return thrownExceptions;
    }

    @Override
    public long getDocumentedMembers() {
        return 0;
    }

    @Override
    public double getDocumentedMembersPercent() {
        final double documentedMembers =
                Utils.boolToInt(hasDocumentation()) +
                paramsStats.getDocumentedMembers() +
                thrownExceptions.getDocumentedMembers();

        //this 1 is used to count the method as a element which can be documented or not
        return Utils.computePercentage(documentedMembers, 1+ getMembersNumber());
    }

    @Override
    public long getMembersNumber() {
        return paramsStats.getMembersNumber() + thrownExceptions.getMembersNumber();
    }

    @Override
    public boolean hasDocumentation() {
        return Utils.isNotStringEmpty(doc.getRawCommentText());
    }
}
