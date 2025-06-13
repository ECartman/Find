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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 *
 * @author Eduardo
 */
public class FindDirectory extends Rule {
    /**
     * the singleton instance for FindDirectory, directory rule
     */
    public static final FindDirectory Directory = new FindDirectory(true);
    /**
     * the singleton instance for FindDirectory, Regular file rule
     */
    public static final FindDirectory RegularFile = new FindDirectory(false);    
    public static final String COMMAND = "-type";

    /**
     * gets the Singleton instance of the Find Directory depending on the Command
     * Line parameters 
     * @param CMDLine an array of command line parameters 
     * @return null if the parameters are invalid. otherwise return either 
 a Rule to seek for directories or for Regular Files.
     */
    public static final FindDirectory Parse(String[] CMDLine) {
        for (int i = 0; i < CMDLine.length; i++) {
            if (Objects.equals(CMDLine[i].strip(), COMMAND)
                    && i + 1 < CMDLine.length) {
                var dir = CMDLine[i + 1].strip().equalsIgnoreCase("d");
                var file = CMDLine[i + 1].strip().equalsIgnoreCase("f");
                if (dir || file) {
                    return dir?Directory:RegularFile;
                }
            }
        }
        return null;
    }

    /**
     * whenever we are seeking for directory or regular file.
     */
    private final boolean SeekDirectory;

    /**
     * creates a FindDirectory.
     *
     * @param Directory whenever or not to seek for directories.
     */
    protected FindDirectory(boolean Directory) {
        super("Type of File ");
        SeekDirectory = Directory;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean MatchRule(Path pathToFile) {
        //if we seek for a directory and is a directory will return true. 
        //if we seek a non directory and is a directory will return false.
        return SeekDirectory == Files.isDirectory(pathToFile);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getRuleName() {
        var type = SeekDirectory ? "Directory" : "File";
        return String.format("%s matching %s", getBaseRuleName(), type);
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
        if (otherobj instanceof FindDirectory other) {
            return SeekDirectory == other.SeekDirectory;
        }
        return false;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        return Objects.hash(SeekDirectory,getBaseRuleName());
    }
}
