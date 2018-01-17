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
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Computes JavaDoc coverage statistics for a list of classes.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class ClassesDocStats extends MembersDocStats {
    private final List<ClassDocStats> classesDocStats;

    /**
     * Instantiates an object to compute JavaDoc coverage statistics for a list of classes.
     *
     * @param docs an array of elements which enables reading the classes' JavaDoc documentation
     */
    public ClassesDocStats(final ClassDoc[] docs){
        classesDocStats = new ArrayList<>(docs.length);
        for (final ClassDoc doc : docs) {
            classesDocStats.add(new ClassDocStats(doc));
        }
    }

    @Override
    public long getMembersNumber() {
        return classesDocStats.size();
    }

    @Override
    public String getType() {
        return "Classes/Interfaces/Enums";
    }

    @Override
    public long getDocumentedMembers() {
        return classesDocStats.stream().map(ClassDocStats::getDoc).map(Doc::getRawCommentText).filter(Utils::isNotStringEmpty).count();
    }

    /**
     * Gets a List where each element represents the individual JavaDoc coverage statistics for a specific class.
     *
     * @return a List of class's JavaDoc coverage statistics
     */
    public List<ClassDocStats> getClassesList() {
        return Collections.unmodifiableList(classesDocStats);
    }

    /**
     * A set of classes doesn't have documentation,
     * only each individual class may have.
     * @return
     */
    @Override
    public boolean isDocumented() {
        return false;
    }
}
