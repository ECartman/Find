/*
 * Copyright © 2025 Eduardo Vindas Cordoba. All rights reserved.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.aeongames.utils;

/**
 * this Enumeration class is designed to show,use values from data sizes.
 *
 * @author Eduardo
 */
public enum sizes {
    GigaByte(1000_000_000L,1_073_741_824L, "Gigabytes", "GB"),
    MegaByte(1_000_000L,1_048_576L, "Megabytes", "MB"),
    KiloByte(1000L,1024L, "Kilobytes", "KB"),
    bytee(1L,1L, "bytes", "B");
    
    private final String Name;
    private final String Suffix;
    /**
     * the decimal amount that represent the &quot;size&quot;
     * this number is loss. but represent the closest &quot;decimal&quot;
     */
    private final long DecSize;
    /**
     * the amount of bytes of this size.
     */
    private final long Bytes;
    
    /**
     * creates the Enumeration with its own properties. 
     * @param value the decimal representation of this size
     * @param bibytes the amount of bytes that represent this size
     * @param nombre the Size name
     * @param dim the suffix of this size
     */
    private sizes(long value,long bibytes, String nombre, String dim) {
        DecSize = value;
        Name = nombre;
        Suffix = dim;
        Bytes=bibytes;
    }

    /**
     * returns the amount of bytes that represent this size. 
     * @return the amount of bytes
     */
    public long size() {
        return Bytes;
    }
    
    /**
     * returns the decimal amount that represent this size. 
     * @return  the decimal amount that represent this size. 
     */
    public long getNumericSize(){
        return DecSize;
    }

    /**
     * get the Name of this size
     * @return a String that represent the Name of this value
     */
    public String getName() {
        return Name;
    }

    /**
     * returns the suffix of this value
     * @return a String that represent the suffix for this value
     */
    public String getSuffix() {
        return Suffix;
    }
}
