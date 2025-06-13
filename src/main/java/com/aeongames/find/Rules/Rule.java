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

import java.io.IOException;
import java.nio.file.Path;

/**
 * an abstract class that defines the basic blueprint for find rules on files
 * and the contracts to match. 
 * @author Eduardo
 */
public abstract class Rule {
    private final String RuleName;

    /**
     * base Rule Constructor
     * @param MyRuleName the base Name of this rule the name can be expanded on
     * {@link BaseRule#getRuleName()}
     * 
     */
    protected Rule(String MyRuleName) {
        RuleName = MyRuleName;
    }

    /**
     * checks and returns whenever this rule is satisfied on this path
     * @param pathToFile the path to a file to check for match for this rule
     * @return true if this rule is satisfied false otherwise. 
     * @throws IOException if it fails to read File metadata (when and if required)
     */
    public abstract boolean MatchRule(Path pathToFile)throws IOException;
    
    /**
     * the full name of the Rule (might contain implementation details or other 
     * details
     * @return a String that Describe this Rule
     */
    public abstract String getRuleName();

    /**
     * provide the rule name for this particular Find Rule. it is intended to be
     * User Readable
     *
     * @return the RuleName
     */
    protected String getBaseRuleName() {
        return RuleName;
    }
}
