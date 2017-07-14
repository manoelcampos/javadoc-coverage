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
 * Computes statistics about the JavaDocs of parameters
 * from a specific method.
 *
 * @author Manoel Campos da Silva Filho
 */
public class MethodParamsDocStats extends MethodTagsDocStats {

    MethodParamsDocStats(final ExecutableMemberDoc methodDoc) {
        super(methodDoc);
    }

    @Override
    public long getMembersNumber() {
        return getMethodDoc().parameters().length;
    }

    @Override
    public String getTagName() {
        return "@param";
    }

    @Override
    public String getMembersType() {
        return "Params";
    }
}
