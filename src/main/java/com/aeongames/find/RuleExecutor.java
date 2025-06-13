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
package com.aeongames.find;

import com.aeongames.find.Rules.Rule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * a functional Interface that defines a execution routine to process a specific
 * rule.
 *
 * @author Eduardo
 */
public class RuleExecutor {

    private boolean ignoreIOexeption = false;
    private final List<Rule> rules;

    public RuleExecutor(List<Rule> tharules) {
        rules = tharules;
    }

    public void setIgnoreIOExceptions(boolean ignore) {
        ignoreIOexeption = ignore;
    }

    /**
     * loops the folder (in a recursive if required) and seeks and matches the
     * Rules for each file/folder and return a list of path that matches the
     * rules
     *
     * @param ParentPath the folder from which start the search
     * @param recursive whenever to look recursively
     * @return a list (linked list) of found path's
     * @throws IOException if there was a I/O problem navigating or reading the
     * files or files metadata.
     */
    public List<Path> ExecuteRule(Path ParentPath, boolean recursive) throws IOException {
        //assume at this point caller alredy check existance, folder and readability.
        LinkedList<Path> results = new LinkedList<>();
        //given that we might or not require to play with folders we list instead of walk the path
        try (var FileList = Files.list(ParentPath)) {
            var iterator = FileList.iterator();
            while (iterator.hasNext()) {
                var nextfile = iterator.next();
                if (MatchRule(nextfile)) {
                    results.add(nextfile);
                }
                if (recursive && Files.isDirectory(nextfile, LinkOption.NOFOLLOW_LINKS)) {
                    results.addAll(ExecuteRule(nextfile, recursive));
                }
            }
        }//auto close the resource 
        return results;
    }

    /**
     * this function Runs a Rule for the provided path. and determines if it
     * matches a single or a set of rules and should consider to be matched or
     * not
     *
     * @param pathToFile the file to analyze.
     * @return true if matches the rule(s) false otherwise.
     * @throws IOException if fails to read the file or its metadata and
     * ignoreIOexeption is set to false
     */
    public boolean MatchRule(Path pathToFile) throws IOException {
        //this is the definition on a lambda of the function MatchRule. 
        //on this case MatchRule looks and check that the pathToFile matches all the rules.
        for (Rule rule : rules) {
            try {
                if (!rule.MatchRule(pathToFile)) {
                    return false;
                }
            } catch (IOException ex) {
                if (ignoreIOexeption) {
                    return false;
                } else {
                    throw ex;
                }
            }
        }
        return true;
    }

}
