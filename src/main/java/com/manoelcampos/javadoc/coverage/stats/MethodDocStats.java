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
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;

import java.util.Arrays;

/**
 * Computes statistics about the JavaDocs of a method
 * and its members, such as: parameters and thrown exceptions.
 *
 * @author Manoel Campos da Silva Filho
 */
public class MethodDocStats extends CompoundedDocStats {
    private final ExecutableMemberDoc doc;
    private final MethodParamsDocStats paramsStats;
    private final MethodExceptionsDocStats thrownExceptions;

    MethodDocStats(final ExecutableMemberDoc doc) {
        this.doc = doc;
        this.paramsStats = new MethodParamsDocStats(doc);
        this.paramsStats.setPrintIfNoMembers(true);
        this.thrownExceptions = new MethodExceptionsDocStats(doc);
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

    @Override
    protected ProgramElementDoc getDoc() {
        return doc;
    }

    public MethodParamsDocStats getParamsStats() {
        return paramsStats;
    }

    public MethodExceptionsDocStats getThrownExceptions() {
        return thrownExceptions;
    }
}
