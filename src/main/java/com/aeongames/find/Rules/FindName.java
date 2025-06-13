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
 */
package com.aeongames.find.Rules;

import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 *
 * @author Eduardo
 */
public class FindName extends Rule {

    public static final String COMMAND = "-name";

   /**
     * gets a instance of the FindName rule depending on the Command
     * Line parameters 
     * @param CMDLine an array of command line parameters 
     * @return null if the parameters are invalid. otherwise return either 
 a Rule to seek the file name with just a name or a pattern
     */
    public static final FindName Parse(String[] CMDLine) {
        for (int i = 0; i < CMDLine.length; i++) {
            if (Objects.equals(CMDLine[i].strip(), COMMAND)
                    && i + 1 < CMDLine.length) {
                var invalid = CMDLine[i + 1].strip().startsWith("-");
                var hasOther = i + 2 < CMDLine.length && !CMDLine[i + 2].strip().startsWith("-");
                if (invalid) {
                    return null;
                }
                boolean Regex = false;
                if (hasOther) {
                    Regex = CMDLine[i + 2].strip().equalsIgnoreCase("RE");
                }
                return new FindName(CMDLine[i + 1], Regex);
            }
        }
        return null;
    }

    private final String NamePattern;
    private final boolean isRegex;

    /**
     * creates a FindName.
     *
     * @param namePattern the name pattern to seek.
     * @param Regex if the pattern is a Regular expression or not
     */
    public FindName(String namePattern, boolean Regex) {
        super("Searches files by name.");
        NamePattern = namePattern.strip();
        isRegex = Regex;
    }

    /**
     * creates a FindName. rule and Assumes is NOT regular expression rather a
     * simple name matching
     *
     * @param namePattern the name pattern to seek.
     */
    public FindName(String namePattern) {
        this(namePattern, false);
    }

    @Override
    public boolean MatchRule(Path pathToFile) {
        //we assume the path is valid 
        var PathName = pathToFile.getFileName().toString();
        if (!isRegex) {
            return PathName.equals(NamePattern);
        } else {
            return Pattern.matches(NamePattern, PathName);
        }
    }

    @Override
    public String getRuleName() {
        StringBuilder build = new StringBuilder(getBaseRuleName());
        build.append(" matching");
        if (isRegex) {
            build.append("the Regular Expresion");
        }
        build.append(NamePattern);
        return build.toString();
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
        if (otherobj instanceof FindName other) {
            return isRegex == other.isRegex
                    && Objects.equals(NamePattern, other.NamePattern);
        }
        return false;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        return Objects.hash(isRegex, NamePattern, getBaseRuleName());
    }
}
