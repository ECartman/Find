/* 
 *  Copyright © 2025 Eduardo Vindas Cordoba. All rights reserved.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 * 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aeongames.find.Rules;

import com.aeongames.utils.sizes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Eduardo
 */
public class FindSize extends Rule {
    public static final String COMMAND = "-size";
    /**
     * gets a instance of the FindSize rule depending on the Command Line
     * parameters
     *
     * @param CMDLine an array of command line parameters
     * @return null if the parameters are invalid. otherwise return either a
 Rule to seek the file with at least a specific size with just a name
 or a pattern
     */
    public static final FindSize Parse(String[] CMDLine) {
        for (int i = 0; i < CMDLine.length; i++) {
            if (Objects.equals(CMDLine[i].strip(), COMMAND)
                    && i + 1 < CMDLine.length) {
                var invalid = CMDLine[i + 1].strip().startsWith("-");
                if (invalid) {
                    return null;
                }
                var pattern = Pattern.compile("(?i)([0-9]+)(GB|MB|KB|)", Pattern.CASE_INSENSITIVE);
                Matcher m = pattern.matcher(CMDLine[i + 1].strip());
                if (m.find()) {
                    int value = Integer.parseInt(m.group(1));
                    sizes touse;
                    if (m.group(2).equalsIgnoreCase(sizes.GigaByte.getSuffix())) {
                        touse = sizes.GigaByte;
                    } else if (m.group(2).equalsIgnoreCase(sizes.MegaByte.getSuffix())) {
                        touse = sizes.MegaByte;
                    } else if (m.group(2).equalsIgnoreCase(sizes.KiloByte.getSuffix())) {
                        touse = sizes.KiloByte;
                    } else {
                        touse = sizes.bytee;
                    }
                    return new FindSize(value, touse);
                }
            }
        }
        return null;
    }

    private final long MinimalSizeBytes;

    /**
     * creates a FindDirectory.
     *
     * @param Directory whenever or not to seek for directories.
     */
    public FindSize(long minimalSize) {
        super("Find Files that match or exceeds size");
        MinimalSizeBytes = minimalSize;
    }

    /**
     * creates a FindDirectory.
     *
     * @param Directory whenever or not to seek for directories.
     */
    public FindSize(int count, sizes size) {
        this(count * size.size());
    }

    @Override
    public boolean MatchRule(Path pathToFile) throws IOException {
        if (Files.isDirectory(pathToFile)) {
            return false;
        }
        return Files.size(pathToFile) >= MinimalSizeBytes;
    }

    /**
     * checks if there is another instance of this class with the same rules.
     *
     * @param otherobj the other object to check. can be null (but will return
     * false)
     * @return whenever or not this and the otherobj matches and represent the
     * same rule (NOT the same reference)
     */
    @Override
    public boolean equals(Object otherobj) {
        if (Objects.isNull(otherobj)) {
            return false;
        }
        if (otherobj instanceof FindSize other) {
            return MinimalSizeBytes == other.MinimalSizeBytes;
        }
        return false;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        return Objects.hash(MinimalSizeBytes, getBaseRuleName());
    }

    @Override
    public String getRuleName() {
        StringBuilder build = new StringBuilder(getBaseRuleName());
        build.append(" ");
        sizes touse;
        if (MinimalSizeBytes > sizes.GigaByte.size()) {
            touse = sizes.GigaByte;
        } else if (MinimalSizeBytes > sizes.MegaByte.size()) {
            touse = sizes.MegaByte;
        } else if (MinimalSizeBytes > sizes.KiloByte.size()) {
            touse = sizes.KiloByte;
        } else {
            touse = sizes.bytee;
        }
        build.append(String.format("%.2f", (double) MinimalSizeBytes / touse.size()));
        build.append(" ");
        build.append(touse.getSuffix());
        return build.toString();
    }
}
