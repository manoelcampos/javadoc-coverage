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

import java.util.stream.Stream;

/**
 * Computes statistics about the JavaDocs of a list of members
 * belonging to a {@link CompoundedDocStats} object.
 * In the case the {@link CompoundedDocStats} object is a class,
 * this object computes statistics for its fields, methods and so on.
 * In case the the {@link CompoundedDocStats} object is a method,
 * this object computes statistics for its parameters and thrown exceptions.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public abstract class MembersDocStats implements DocStats {
    private boolean printIfNoMembers;

    public abstract long getMembersNumber();

    public long getDocumentedMembers(){
        return getDocumentedMembersCount(getMembersComments());
    }

    public abstract Stream<String> getMembersComments();

    public long getUndocumentedMembers() {
        return getMembersNumber() - getDocumentedMembers();
    }

    /**
     * Gets the percentage of {@link #getMembersNumber() members}
     * that are {@link #getDocumentedMembers() documented}.
     *
     * @return the percentage of documented members, in scale from 0 to 100.
     */
    public double getDocumentedMembersPercent(){
        return Utils.computePercentage(getDocumentedMembers(), getMembersNumber());
    }

    public final boolean isPrintIfNoMembers() {
        return this.printIfNoMembers;
    }

    final void setPrintIfNoMembers(final boolean printIfNoMembers) {
        this.printIfNoMembers = printIfNoMembers;
    }
}
