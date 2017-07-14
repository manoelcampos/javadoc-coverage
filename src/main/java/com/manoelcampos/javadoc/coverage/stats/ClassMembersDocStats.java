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
import com.sun.javadoc.Doc;
import com.sun.javadoc.MemberDoc;

import java.util.Arrays;
import java.util.Objects;

/**
 * Computes statistics about the javadocs of specific type of members of a class, interface or enum.
 * Members may be either fields, methods or constructors.
 *
 * @author Manoel Campos da Silva Filho
 */
public class ClassMembersDocStats extends MembersDocStats {
    private final MemberDoc[] membersDocs;
    private final String membersType;

    ClassMembersDocStats(final MemberDoc[] membersDocs, final String membersType) {
        this.membersDocs = membersDocs;
        this.membersType = membersType;
    }

    /**
     * Gets the number of members which are explicitly declared into the source code,
     * from a list of given members.
     * The length of the given array cannot be used to this purpose
     * because some elements such as default no-args constructors are not directly declared
     * into the source class but are counted as a member.
     * This way, it may count as a non-documented element
     * while it doesn't even exist into the source code.
     *
     * @todo the method is not working as expected. It always returns the length of the array.
     */
    @Override
    public long getMembersNumber() {
        return Arrays.stream(membersDocs)
                .map(Doc::position)
                .filter(Objects::nonNull)
                .count();
    }

    @Override
    public long getDocumentedMembers() {
        return Arrays.stream(membersDocs)
                .map(Doc::getRawCommentText)
                .filter(Utils::isNotStringEmpty)
                .count();
    }

    @Override
    public String getMembersType() {
        return membersType;
    }
}
