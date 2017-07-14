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
 * Computes statistics about the JavaDocs of a set of members
 * belonging to a {@link CompoundedDocStats} object.
 * In the case the {@link CompoundedDocStats} object is a class,
 * this object computes statistics for its fields, methods and so on.
 * In case the the {@link CompoundedDocStats} object is a method,
 * this object computes statistics for its parameters and thrown exceptions.
 *
 * @author Manoel Campos da Silva Filho
 */
public abstract class MembersDocStats {
    private boolean printIfNoMembers;

    public abstract String getMembersType();

    public abstract long getMembersNumber();

    public abstract long getDocumentedMembers();

    public long getUndocumentedMembers() {
        return getMembersNumber() - getDocumentedMembers();
    }

    public final boolean isPrintIfNoMembers() {
        return this.printIfNoMembers;
    }

    final void setPrintIfNoMembers(final boolean printIfNoMembers) {
        this.printIfNoMembers = printIfNoMembers;
    }
}
