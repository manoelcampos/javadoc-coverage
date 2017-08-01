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

/**
 * An abstract class to compute JavaDocs coverage statistics for a list of members
 * belonging to another object, which is called the owner.
 *
 * <p>In the case the owner of the members is:
 * <ul>
 *     <li><b>a class</b>, this object computes statistics for its fields, methods and so on.</li>
 *     <li><b>a method</b>, this object computes statistics for its parameters and thrown exceptions.</li>
 * </ul>
 * </p>
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public abstract class MembersDocStats implements DocStats {
    private boolean printIfNoMembers;

    /**
     * Checks if the the owner of the members has a documentation itself.
     *
     * @return true if the owner has documentation, false otherwise
     */
    public abstract boolean hasDocumentation();

    /**
     * Checks if JavaDoc coverage statistics should be printed if the owner has no member.
     *
     * @return true if statistics should be printed even if the owner has no member, false if
     * statistics shouldn't be printed if the owner has no member
     */
    public final boolean isPrintIfNoMembers() {
        return this.printIfNoMembers;
    }

    /**
     * Enables printing JavaDoc coverage statistics even if the owner has no member.
     */
    public final void enablePrintIfNoMembers() {
        this.printIfNoMembers = true;
    }

    /**
     * Disables printing JavaDoc coverage statistics if the owner has no member.
     */
    public final void disablePrintIfNoMembers() {
        this.printIfNoMembers = false;
    }
}
