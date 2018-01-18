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

import com.sun.javadoc.ExecutableMemberDoc;

/**
 * Computes JavaDoc coverage statistics for parameters from a specific method.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class MethodParamsDocStats extends MethodTagsDocStats {

    /**
     * Instantiates an object to compute JavaDoc coverage statistics for the params
     * of a method/constructor.
     *
     * @param doc an object which enables reading the JavaDoc documentation for the method the params belong to
     */
    MethodParamsDocStats(final ExecutableMemberDoc doc) {
        super(doc);
    }

    @Override
    public String getTagName() {
        return "@param";
    }

    @Override
    public String getType() {
        return "Params";
    }

    @Override
    public long getNumberOfDocumentableMembers() {
        return getDoc().parameters().length;
    }
}
