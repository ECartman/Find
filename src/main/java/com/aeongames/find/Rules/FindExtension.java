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

import java.nio.file.Path;
import java.util.Objects;

/**
 *
 * @author Eduardo
 */
public class FindExtension extends Rule {
    public static final String COMMAND = "-ext";
    /**
     * gets a instance of the FindExtension rule depending on the Command
     * Line parameters 
     * @param CMDLine an array of command line parameters 
     * @return null if the parameters are invalid. otherwise return either 
 a Rule to seek the extension desired.
     */
    public static final FindExtension Parse(String[] CMDLine) {
        for (int i = 0; i < CMDLine.length; i++) {
            if (Objects.equals(CMDLine[i].strip(), COMMAND)
                    && i + 1 < CMDLine.length) {
                var invalid = CMDLine[i + 1].strip().startsWith("-");
                if (invalid) {
                    return null;
                }
                return new FindExtension(CMDLine[i + 1]);
            }
        }
        return null;
    }
    
    private final String Extension;
    /**
     * creates a FindName.
     *
     * @param namePattern the name pattern to seek.
     * @param Regex if the pattern is a Regular expression or not
     */
    public FindExtension(String extension) {
        super("Searches files by the Extension");
        Extension = extension.strip();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean MatchRule(Path pathToFile) {
        //we assume the path is valid 
        var PathName = pathToFile.getFileName().toString();
        if(PathName.indexOf('.')==-1){
            //no extension.
            return false;
        }
        var FileExt = PathName.substring(PathName.indexOf('.'));
        //we could try both ways. check if .xxx or xxx we can later on add a "strict" rule
        if (Objects.equals(Extension, FileExt)) {
            return true;
        }
        FileExt = PathName.substring(PathName.lastIndexOf('.') + 1);
        return Objects.equals(Extension, FileExt);
    }

    @Override
    public String getRuleName() {
        return String.format("%s matching %s", getBaseRuleName(), Extension);
    }
    
        /**
     * checks if there is another instance of this class with the same rules.
     * @param otherobj the other object to check. can be null (but will return false)
     * @return whenever or not this and the otherobj matches and represent the same rule
     * (NOT the same reference) 
     */
    @Override
    public boolean equals(Object otherobj) {
        if (Objects.isNull(otherobj)) {
            return false;
        }
        if (otherobj instanceof FindExtension other) {
            return Objects.equals(Extension, other.Extension);
        }
        return false;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
       return Objects.hash(Extension,getBaseRuleName());
    }
}
