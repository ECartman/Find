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

import com.aeongames.find.Rules.FindDirectory;
import com.aeongames.find.Rules.FindExtension;
import com.aeongames.find.Rules.FindName;
import com.aeongames.find.Rules.Rule;
import com.aeongames.find.Rules.FindSize;
import com.aeongames.utils.sizes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eduardo
 */
public class Find {
    /*
    example params: 
    "C:\Users\cartman\OneDrive\book" -type f -size 1mb -ext jpg
    output: 
    --- exec:3.1.0:exec (default-cli) @ Find ---
    File Size 1.25 MB C:\Users\cartman\OneDrive\book\0.00.jpg
    File Size 1.58 MB C:\Users\cartman\OneDrive\book\0.01.jpg
    File Size 1.00 MB C:\Users\cartman\OneDrive\book\0.02.jpg
    */
    public static void main(String[] args) throws IOException {
        LinkedList<Rule> rules = new LinkedList<>();
        //parse the paramenters and create the rules out of the Command line paramenters. 
        var basefolder = parseParams(args, rules);
        List<Path> results = find(Path.of(basefolder), rules);
        parseResults(results);
    }

    /**
     * this is the base entry point of this API. this function will seek the
     * Provided path and recourse all sub-folders and seek all files that
     * matches All the rules.
     *
     * @param basePath the base path (a folder) to start the look from.
     * @param rules
     * @return
     */
    public static List<Path> find(Path basePath, List<Rule> rules) {
        if (Objects.isNull(basePath) || !Files.exists(basePath) || !Files.isReadable(basePath)) {
            return List.of();
        }
        //use an anonimous Instance of Rule Executor to process the files and Check the file rules
        RuleExecutor executor = CreateRuleExecutor(rules);
        List<Path> results = null;
        try {
            results = executor.ExecuteRule(basePath, true);
        } catch (IOException ex) {
            if (results == null) {
                results = List.of();
            }
            Logger.getLogger(Find.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
        
    private static void parseResults(List<Path> results) throws IOException {
        for (Path result : results) {
            System.out.print("File Size ");
            sizes touse;
            var MinimalSizeBytes = Files.size(result);
            if (MinimalSizeBytes >= sizes.GigaByte.size()) {
                touse = sizes.GigaByte;
            } else if (MinimalSizeBytes >= sizes.MegaByte.size()) {
                touse = sizes.MegaByte;
            } else if (MinimalSizeBytes >= sizes.KiloByte.size()) {
                touse = sizes.KiloByte;
            } else {
                touse = sizes.bytee;
            }
            System.out.print(String.format("%.2f", (double) MinimalSizeBytes / touse.size()));
            System.out.print(" ");
            System.out.print(touse.getSuffix());
            System.out.print(" ");
            System.out.println(result.toString());
        }
    }

    /**
     * TODO and LIMITATIONS. it only supports 1 of each Filters. as each class
     * Parse only seeks and process the first instanced of the Flag instead of
     * seeking for all instances of it. for the sake of this example this is
     * Acceptable.
     */
    private static String parseParams(String[] args, final LinkedList<Rule> rules) {
        String path = null;
        if (args != null && args.length > 1) {
            path = args[0];
            includeNonNull(FindDirectory.Parse(args), rules);
            includeNonNull(FindExtension.Parse(args), rules);
            includeNonNull(FindName.Parse(args), rules);
            includeNonNull(FindSize.Parse(args), rules);
        }
        return path;
    }

    private static void includeNonNull(Rule rule, final LinkedList<Rule> rules) {
        if (rule != null) {
            rules.add(rule);
        }
    }

    private static RuleExecutor CreateRuleExecutor(final List<Rule> rules) {
        return (pathToFile) -> {
            //this is the definition on a lambda of the function MatchRule. 
            //on this case MatchRule looks and check that the pathToFile matches all the rules.
            for (Rule rule : rules) {
                if (!rule.MatchRule(pathToFile)) {
                    return false;
                }
            }
            return true;
        };
    }
}
