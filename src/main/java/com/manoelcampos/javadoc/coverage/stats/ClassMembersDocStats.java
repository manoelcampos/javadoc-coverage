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
import java.util.Objects;
import java.util.function.Predicate;

import com.manoelcampos.javadoc.coverage.Utils;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.ProgramElementDoc;

/**
 * Computes JavaDoc coverage statistics for specific type of members belonging to an owner.
 * An owner can be a class, interface or enum.
 * Members may be either fields, methods or constructors.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class ClassMembersDocStats extends MembersDocStats {
    /**
     * The JavaDoc documentation for the members of the owner.
     */
    private final Doc[] membersDocs;
    private final String membersType;
    private final boolean computeOnlyForPublic;

    /**
     * Instantiates an object to compute JavaDoc coverage statistics for the members of a class, interface or enum.
     *
     * @param membersDocs
     *            the JavaDoc documentation for the members of the owner.
     * @param membersType
     *            the type of the members of the owner to compute JavaDoc coverage statistics.
     * @param computeOnlyForPublic
     *            indicates that coverage should only be compute for the public part of the javadoc
     */
    ClassMembersDocStats(final Doc[] membersDocs, final String membersType, boolean computeOnlyForPublic) {
        this.membersDocs = membersDocs;
        this.membersType = membersType;
        this.computeOnlyForPublic = computeOnlyForPublic;
    }

    @Override
    public String getType() {
        return membersType;
    }

    /**
     * A set of class members doesn't have documentation, only each individual member may have.
     *
     * @return always false
     */
    @Override
    public boolean isDocumented() {
        return false;
    }


    @Override
    public long getNumberOfDocumentedMembers() {
        // @formatter:off
        return Arrays.stream(membersDocs)
                .filter(filterPublicIfNecessary())
                .map(Doc::getRawCommentText)
                .filter(Utils::isNotStringEmpty)
                .count();
        // @formatter:on
    }

    @Override
    public long getNumberOfDocumentableMembers() {
        /*
         * default constructors are also counted here, this is correct, they will also appear in the generated javadoc, and have no
         * dedicated information
         */
        // @formatter:off
        return Arrays.stream(membersDocs)
                .filter(filterPublicIfNecessary())
                .map(Doc::position)
                .filter(Objects::nonNull)
                .count();
        // @formatter:on
    }

    private Predicate<? super Doc> filterPublicIfNecessary() {
        return m -> {
            if (computeOnlyForPublic) {
                if (m instanceof ClassDoc) {
                    return !computeOnlyForPublic || ((ClassDoc) m).isPublic();
                } else if (m instanceof ProgramElementDoc) {
                    return !computeOnlyForPublic || ((ProgramElementDoc) m).isPublic();
                } else {
                    throw new UnsupportedOperationException("unimplemented for type " + m.getClass());
                }
            }
            return true;
        };
    }
}
