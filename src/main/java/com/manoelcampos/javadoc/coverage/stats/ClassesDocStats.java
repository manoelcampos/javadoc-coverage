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

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.PackageDoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class ClassesDocStats extends MembersDocStats {
    private final List<ClassDocStats> classesDocStats;

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
    public Stream<String> getMembersComments() {
        return classesDocStats.stream().map(ClassDocStats::getDoc).map(Doc::getRawCommentText);
    }

    public List<ClassDocStats> getClassesList() {
        return Collections.unmodifiableList(classesDocStats);
    }
}
