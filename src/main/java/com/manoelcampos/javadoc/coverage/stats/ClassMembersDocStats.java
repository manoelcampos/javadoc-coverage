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

import com.manoelcampos.javadoc.coverage.Utils;
import com.manoelcampos.javadoc.coverage.configuration.Configuration;
import com.sun.javadoc.*;

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
    private final Configuration config;

    /**
     * Instantiates an object to compute JavaDoc coverage statistics for the members of a class, interface or enum.
     *
     * @param membersDocs the JavaDoc documentation for the members of the owner.
     * @param membersType the type of the members of the owner to compute JavaDoc coverage statistics.
     * @param config the configuration of the coverage doclet
     */
    ClassMembersDocStats(final Doc[] membersDocs, final String membersType, Configuration config) {
        this.membersDocs = membersDocs;
        this.membersType = membersType;
        this.config = config;
    }

    /**
     * Gets the number of members which are explicitly declared into the source code,
     * from a list of given members.
     *
     * The length of the given array cannot be used to this purpose
     * because some elements such as default no-args constructors are not directly declared
     * into the source class but are counted as a member.
     * This way, it may count as a non-documented element
     * while it doesn't even exist into the source code.
     */
    @Override
    public long getMembersNumber() {
        /*
         * @todo the method is not working as expected. It always returns the length of the array.
         * The side-effect is that default no-args constructors (which aren't directly declared into
         * a class source code) will be computed as undocumented.
         */
        return Arrays.stream(membersDocs)
                .filter(this::filterPublicIfNecessary)
                .map(Doc::position)
                .filter(Objects::nonNull)
                .count();
    }

    @Override
    public long getDocumentedMembers() {
        return Arrays.stream(membersDocs)
                .filter(this::filterPublicIfNecessary)
                .map(Doc::getRawCommentText)
                .filter(Utils::isNotStringEmpty)
                .count();
    }

    private boolean filterPublicIfNecessary(Doc m) {
        boolean computeOnlyForPublic = config.computePublicCoverageOnly();

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
}
