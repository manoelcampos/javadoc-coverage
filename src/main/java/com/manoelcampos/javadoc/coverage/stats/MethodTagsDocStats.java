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
import com.sun.javadoc.Tag;

import java.util.Arrays;

/**
 * Computes statistics about the javadocs of a set of tags
 * associated to a method, such as the param, return and throws tags.
 *
 * @author Manoel Campos da Silva Filho
 */
public abstract class MethodTagsDocStats extends MembersDocStats {
    private final ExecutableMemberDoc methodDoc;

    MethodTagsDocStats(final ExecutableMemberDoc methodDoc) {
        super();
        this.methodDoc = methodDoc;
    }

    public abstract String getTagName();

    @Override
    public long getDocumentedMembers() {
        return Arrays.stream(getMethodDoc().tags())
                .filter(tag -> getTagName().equals(tag.name()))
                .map(Tag::text)
                .filter(Utils::isNotStringEmpty)
                .count();
    }

    ExecutableMemberDoc getMethodDoc() {
        return methodDoc;
    }
}