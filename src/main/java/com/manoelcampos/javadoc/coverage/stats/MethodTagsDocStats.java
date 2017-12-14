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
import java.util.stream.Stream;

/**
 * An abstract class to compute JavaDoc statistics for a set of tags
 * associated to a method/constructor, such as the {@code @param} and {@code @throws} tags.
 *
 * Each {@link MethodTagsDocStats} sub-class is accountable to compute
 * statistics to a specific kind of tag such as method's parameters or thrown exceptions.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public abstract class MethodTagsDocStats extends MembersDocStats {
    private final ExecutableMemberDoc doc;

    /**
     * Instantiates an object to compute JavaDoc coverage statistics for the tags
     * of a method/constructor.
     *
     * @param doc the element which enables reading the method's JavaDoc documentation
     */
    MethodTagsDocStats(final ExecutableMemberDoc doc) {
        super();
        this.doc = doc;
    }

    /**
     * The name of the tags associated to this object for which
     * JavaDoc coverage statistics will be computed, for instance,
     * "param" or "throws" tags.
     *
     * @return
     */
    public abstract String getTagName();

    @Override
    public long getDocumentedMembers() {
        return getDocumentedTagStream().count();
    }

    /**
     * Gets a Stream of all documented method Tags for the method.
     * @return the documented Tag Stream
     */
    protected Stream<Tag> getDocumentedTagStream() {
        return Arrays.stream(getDoc().tags())
                .filter(tag -> getTagName().equals(tag.name()))
                .filter(tag -> Utils.isNotStringEmpty(tag.text()));
    }

    /**
     * Gets the element which enables reading the method's JavaDoc documentation.
     *
     * @return the method's JavaDoc documentation element
     */
    ExecutableMemberDoc getDoc() {
        return doc;
    }

    @Override
    public boolean hasDocumentation() {
        return Utils.isNotStringEmpty(doc.getRawCommentText());
    }
}
