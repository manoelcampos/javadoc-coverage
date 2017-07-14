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
import com.sun.javadoc.ProgramElementDoc;

/**
 * Computes statistics about the javadocs of elements such as classes
 * (compounded of fields, constructors, methods, etc) and
 * methods (compounded of parameters and thrown exceptions).
 *
 * @author Manoel Campos da Silva Filho
 */
public abstract class CompoundedDocStats {
    public abstract String getType();

    public boolean hasDocumentation() {
        return Utils.isNotStringEmpty(getDoc().getRawCommentText());
    }

    protected abstract ProgramElementDoc getDoc();
}
