/*
 * Copyright (c) 2010-2012 Célio Cidral Junior.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.dialywork.io;

import static org.dialywork.util.FileUtil.createFile;
import static org.dialywork.util.FileUtil.createSubdirectory;
import static org.dialywork.util.FileUtil.newTemporaryPath;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DirectoryTest {

    private Directory directory;

    private File directoryPath;
    private File subdirPath1;
    private File subdirPath2;
    private File subdirPath3;
    private File textFile1;
    private File textFile2;

    @Before
    public void setUp() throws IOException {
        directoryPath = newTemporaryPath();
        subdirPath1 = createSubdirectory("subdir1", directoryPath);
        subdirPath2 = createSubdirectory("subdir2", directoryPath);
        subdirPath3 = createSubdirectory("subdir3.txt", directoryPath);
        textFile1 = createFile("textFile1.txt", directoryPath);
        textFile2 = createFile("textFile2.txt", directoryPath);
        createFile("zipfile.zip", directoryPath);

        directoryPath.mkdirs();

        directory = new FileSystemDirectory(directoryPath);
    }

    @Test
    public void path() {
        assertEquals(directoryPath, directory.path());
    }

    @Test
    public void subdirs() {
        List<Directory> subdirs = directory.subdirs();

        Collections.sort(subdirs, new Comparator<Directory>() {
            public int compare(Directory d1, Directory d2) {
                return d1.path().compareTo(d2.path());
            }
        });
        assertEquals(3, subdirs.size());

        Directory subdir1 = subdirs.get(0);
        Directory subdir2 = subdirs.get(1);
        Directory subdir3 = subdirs.get(2);

        assertEquals(subdirPath1, subdir1.path());
        assertEquals(subdirPath2, subdir2.path());
        assertEquals(subdirPath3, subdir3.path());
    }

    @Test
    public void filesByExtension() {
        File[] files = directory.filesByExtension("txt");

        assertEquals(2, files.length);
        Arrays.sort(files);
        assertEquals(textFile1, files[0]);
        assertEquals(textFile2, files[1]);
    }

}
