package amp190005;

import java.util.*;
import java.lang.Math;
/** 
 * Big Integer calculator
 * This program implements a big integer calculator with the help
 * of array of longs and has the capability to handle very
 * large numbers
 * 
 * @author Anish Patel amp190005
 * @author Henil Doshi hxd180025
 * @author Ishan Shah ixs180019
 * @author Neel Gotecha nxg180023
 * @version 1.0
 * @since 2020-02-03
 */

/**
 * ParseError class throws exception when string to be evaluated is not valid for "evaluateExp".
 */
class ParseError extends Exception {
    public ParseError() {
        super();
    }

    public ParseError(String message) {
        super(message);
    }
}


public class Num implements Comparable<Num> {

    static long defaultBase = 1000000000;
    long base = 10;
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    /**
     * long constructor for Num that initialized Num object of given string
     * @param s number in default base; 10
     */
    public Num(String s) {
        // Default base is 10. Assuming string is coming with base 10.
        this.base = 10;
        len = s.length();
        int i=0,j;

        //check if number is negetive
        if(s.charAt(0) == '-'){
            isNegative = true;
            i++;
            len--;
        }
        else{
            isNegative = false;
        }
        
        // This arr will eventaully contain updated arr with new base
        arr = new long[len];
        for(j=len-1;j>=0;j--,i++){
            arr[j] = Character.digit(s.charAt(i),10);
        }

        // converting the number to current base
        arr = convertBase(arr,10,Num.defaultBase);
        this.base = Num.defaultBase;
        this.len = arr.length;
    }

    /**
     * long constructor for Num
     * @param x number in base 10
     * @param newBase new base
     */
    public Num(long x, long base) {
        this.len = 0;
        this.arr = new long[1000];
        this.base = base;

        int ind = 0;

        // Check if x is zero
        if(x==0){
            arr[ind] = 0;
            ind++;
        }

        // Check if x is negative
        if(x<0){
            isNegative = true;
            x *=-1;
        }

        // Convert x to array of long using modulo and division
        while(x>0)
        {
            isNegative = false;
            arr[ind] = x % base;
            x = x / base;
            ind++;
        }
        len = ind;
    }

    /**
     * Constructor which takes long array as input and created object
     * @param arr:  Array to be used to make an object
     */
    public Num(long arr[]){
        this(arr,Num.defaultBase);
    }

    /**
     * Constructor which takes long array and desired base to create object
     * @param arr:  Array to be used to make an object
     * @param base: Base to be used when making an object
     */
    public Num(long arr[], long base){
        this.arr = arr;
        this.len = arr.length;
        this.base = base;
    }

    /**
     * Constructor which takes long number to create object in current base
     * @param x: Number to be converted to Num
     */
    public Num(long x) {
        this(x,Num.defaultBase);
    }

    /**
     * Addition of two numbers a and b and returning the result as Num
     * @param a: First input number
     * @param b: Second input number
     * @return Num Addition(sum) of two input numbers
     */
    public static Num add(Num a, Num b) {
        Num copyB = new Num (Arrays.copyOf(b.arr,b.len),b.base);
        Num copyA = new Num (Arrays.copyOf(a.arr,a.len),a.base);

        if(a.base != b.base)
            throw new NumberFormatException();
        if ((a.isNegative && !b.isNegative) || (!a.isNegative && b.isNegative)) {
            if (copyA.compareTo(copyB) > 0) {
                System.out.println("I am in A");
                Num res = subtract(copyA, copyB);
                res.isNegative = a.isNegative;
                return res;
            } else {
                System.out.println("I am here");
                Num res = subtract(copyB, copyA);
                res.isNegative = b.isNegative;
                return res;
            } 
        } else if (a.isNegative && b.isNegative) {
            Num res = add(copyA, copyB);
            res.isNegative = true;
            return res;
        }

        long[] outputArr = new long[Math.max(a.len,b.len)+1];
        int t = 0;
        long carry = 0 , sum = 0;
        int arrLength = outputArr.length-1;
        while(t < a.len && t < b.len) {
            sum = a.arr[t] + b.arr[t] + carry;
            outputArr[t] =sum % a.base;
            carry = sum / a.base;
            t += 1;
        }
        while(t < a.len) {
            sum = a.arr[t] + carry;
            outputArr[t] = sum % a.base;
            carry = sum / a.base;
            t += 1;
        }
        while(t < b.len) {
            sum = b.arr[t] + carry;
            outputArr[t] = sum % b.base;
            carry = sum / a.base;
            t += 1;
        }
        if(carry > 0) {
        	outputArr[t] = carry;
        }
        
        //Trailing zeros are being removed here
        return new Num(outputArr[outputArr.length-1]==0?Arrays.copyOfRange(outputArr,0,outputArr.length-1):outputArr,a.base);
    }

    /**
     * Subtraction of two numbers a and b and returning the result as Num
     * @param a First input number
     * @param b Second input number
     * @return Num difference between two input numbers
     */
    public static Num subtract(Num a, Num b) {
        Num copyB = new Num (Arrays.copyOf(b.arr,b.len),b.base);
        Num copyA = new Num (Arrays.copyOf(a.arr,a.len),a.base);

        if(a.base != b.base)
            throw new NumberFormatException();
        if (a.isNegative && !b.isNegative) {
            Num res = add(copyB, copyA);
            res.isNegative = true;
            return res;
        } else if (!a.isNegative && b.isNegative) {
            Num res = add(copyA, copyB);
            res.isNegative = false;
            return res;
        } else if (!a.isNegative && !b.isNegative) {
            if (copyA.compareTo(copyB) < 0) {
                Num res = subtract(copyB, copyA);
                res.isNegative = true;
                return res;
            }
        } else if (a.isNegative && b.isNegative) {
            if (copyA.compareTo(copyB) > 0) {
                Num res = add(a, copyB);
                res.isNegative = a.isNegative;
                return res;
            } else {
                Num res = add(b, copyA);
                res.isNegative = false;
                return res;
            }
        }

        long carry = 0;
        Num zero = new Num(0);
        long[] diff = new long[Math.max(a.len,b.len)]; // Max length will be max of length of both the number
        Num x =a ,y =b;
        if(a.compareTo(b)<0)
        {
            x = b;
            y = a;
        }
        if(x.compareTo(zero) ==0)
            return y;
        if(y.compareTo(zero)==0)
            return x;

        for(int i=0; i<y.len; i++)
        {
            long sub = x.arr[i] - y.arr[i] - carry;
            if(sub < 0){
                sub += x.base;
                carry = 1;
            }
            else{
                carry = 0;
            }
            diff[i] = sub;
        }
        for(int j=y.len; j<x.len; j++)
        {
            long sub = x.arr[j] - carry;
            if(sub < 0){
                sub += x.base;
                carry = 1;
            }
            else{
                carry = 0;
            }
            diff[j] = sub;
        }

        Num output = new Num(diff[diff.length-1]==0?Arrays.copyOfRange(diff,0,diff.length-1):diff,a.base);

        //Check if number is negetive
        if(a.compareTo(b)<0)
        {
            output.isNegative = true;
        }
        return output;
    }

    /**
     * Product of two numbers a*b.
     * @param a
     * @param b
     * @return Prodcut
     */
    public static Num product(Num a, Num b) {
        if(a.base != b.base) {
            String message = "Can not do multiplication of two numbers with different base";
            throw new NumberFormatException(message);
        }

        Num ZERO = new Num(0,a.base);

        // Return ZERO if a OR b is ZERO
        if(a.compareTo(ZERO)==0 || b.compareTo(ZERO)==0)
            return ZERO;

        // This will contain the result array with length of a.len + b.len (Max length)
        long[] productRes = new long[a.len+b.len];

        long carry;
        for(int i=0; i<b.len ; i++){
            carry=0;
            for(int j=0; j<a.len ; j++){
                long tempMultiply = a.arr[j] * b.arr[i];
                productRes[i+j] += carry + tempMultiply;
                carry = productRes[i+j] / a.base;
                productRes[i+j] = productRes[i+j] % a.base;
            }
            productRes[i + a.len] = carry;
        }

        //Removing trailing zeros
        Num result;
        if(productRes[productRes.length-1]==0)
            result = new Num(Arrays.copyOfRange(productRes,0,productRes.length-1),a.base);
        else
            result = new Num(productRes,a.base);

        //Checking if the product is negetive
        if(a.isNegative && b.isNegative)
            result.isNegative = false;
        else if ((!a.isNegative && b.isNegative) || (a.isNegative && !b.isNegative))
            result.isNegative = true;

        return result;
    }

    /**
     * This function computes power of a number in which exponent is provided as second parameter.
     * It will return Num corresponding to number^exp (number to the power exp). 
     * We are assuming that exp is a non negative number. 
     * Here, we are using divide and conquer method to implement power.
     * @param number Number whose power we are computing 
     * @param exp exponent
     * @return Num This will return number^exp
     */
    public static Num power(Num number, long exp) {
        if(exp == 0) {
            return new Num(1,number.base);
        }
        else if(exp % 2 == 0) {
        	return product(power(number,exp/2),power(number,exp/2));
        }
        else {
            return product(number,product(power(number,exp/2),power (number,exp/2)));
        }
    }

    /**
     * Integer division a/b. Using divide-and-conquer or division algorithm. Return null if b=0.
     * @param a
     * @param b
     * @return a/b, null if b=0
     */
    public static Num divide(Num a, Num b) {
        if(a.base != b.base)
            throw new NumberFormatException();
        Num copyB = new Num (Arrays.copyOf(b.arr,b.len),b.base);
        Num copyA = new Num (Arrays.copyOf(a.arr,a.len),a.base);

        if(copyB.compareTo(new Num(0,copyB.base)) ==0)
            return null;
        Num ONE = new Num(1,a.base);
        if (b.compareTo(ONE) == 0) {
            return a;
        }

        Num left = new Num(0,a.base);
        Num right = new Num (Arrays.copyOf(a.arr,a.len),a.base);
        Num prevMid = new Num (0,a.base);

        Num temp1,temp2, mid;

        while(true){
            temp1 = subtract(right,left); //using temporary variables to store intermediate results
            temp2 = temp1.divideBy2();
            mid = add(left,temp2);
            if(product(copyB,mid).compareTo(copyA) == 0 || prevMid.compareTo(mid)==0)
                break;
            if(product(copyB,mid).compareTo(copyA) < 0)
                left = mid;
            else
                right = mid;
            prevMid =mid;
        }

        //Checking if the product is negetive
        if(a.isNegative && b.isNegative)
            mid.isNegative = false;
        else if ((!a.isNegative && b.isNegative) || (a.isNegative && !b.isNegative))
            mid.isNegative = true;
        return mid;
    }



    /**
     * Modulo of two input numbers. This function returns remainder when you divide first
     * number (a) by second number (b) (a%b). We are assuming a is non-negative and b is greater than 0,
     * and we will return null if b = 0.
     * @param a First input number
     * @param b Second input number
     * @return Num Remainder that we get when a is divided by b
     */
    public static Num mod(Num a, Num b) {
        if(a.base != b.base) {
            throw new NumberFormatException();
        }
        Num copyB = new Num (Arrays.copyOf(b.arr,b.len),b.base);
        Num copyA = new Num (Arrays.copyOf(a.arr,a.len),a.base);

        Num one = new Num(1,a.base);
        Num zero = new Num(0,a.base);
        if(b.compareTo(zero) == 0) {
            return null;
        }
        if(a.compareTo(b) == 0) {
            return zero;
        }
        if(b.compareTo(one) == 0) {
            return zero;
        }
        Num result = subtract(copyA,product(copyB,divide(copyA,copyB)));

        if (result.arr.length == 0) {
            long[] temp = new long[1];
            temp[0] = 0;
            result.arr = temp;
            result.len = 1;
        }

        if (a.isNegative && !b.isNegative) {
            Num latestResult = subtract(copyB, result);
            latestResult.isNegative = false;
            return latestResult;
        } else if (a.isNegative && b.isNegative) {
            result.isNegative = true;
            return result;
        } else if (!a.isNegative && b.isNegative) {
            Num latestResult = subtract(result, copyB);
            latestResult.isNegative = true;
            return latestResult;
        }
        return result;
    }
    /**
     * Using binary search to return the square root of a given object.
     * @param a
     * @return square root of a
     */
    public static Num squareRoot(Num a) {
        if (a.isNegative) {
            String message = "Can not find square root of negative number";
            throw new NumberFormatException(message);
        }

        // Creating Zero and One number
        Num ZERO = new Num(0,a.base);
        Num ONE = new Num(1,a.base);

        Num output = ZERO;
        Num previousMid = ZERO;
        Num start = ONE;
        Num end = a;

        if(a.compareTo(ZERO)==0 || a.compareTo(ONE)==0) // Check if number is 1 or 0
            return a;

        while(start.compareTo(end) <=0){ // Running until start is less than or equal to end
            Num midVal = (add(start,end)).divideBy2();

            Num prod = product(midVal,midVal);
            if(prod.compareTo(a)== 0 || previousMid.compareTo(midVal) == 0 )
                return midVal;

            if(prod.compareTo(a)<0){
                start = add(midVal,ONE);
                output = midVal;
            }
            else{
                end = subtract(midVal,ONE);
            }
            previousMid = midVal;
        }
        return output;
    }

    /**
     * Unsigned compareTo
     * @param other
     * @return
     */
    public int unsignedCompareTo(Num other) {
        if (this.len<other.len) {
            return -1;
        } else if (this.len>other.len) {
            return +1;
        } else {
            // return compareMagnitude(other);
            for (int j = this.len - 1; j >= 0; j--) {
                if (this.arr[j] < other.arr[j]) {
                    return -1;
                } else if (this.arr[j] > other.arr[j]) {
                    return 1;
                }
            }
            return 0;
        }
    }

    /**
     * compare "this" to "other": return +1 if this is greater, 0 if equal, -1 otherwise
     * @param other
     * @return +1 if this is greater, 0 if equal, -1 otherwise
     */
    public int compareTo(Num other) {
        if (this.isNegative && other.isNegative)
            return -1 * unsignedCompareTo(other);
        else if (!this.isNegative && !other.isNegative)
            return unsignedCompareTo(other);
        else if (this.isNegative && !other.isNegative)
            return -1;
        else
            return 1;

    }
    
    /**
     *  Prints the base and elements of the list, separated by spaces
     * @param null
     * @return nothing
     */
    public void printList() {
        System.out.print(this.base + ": ");
        for (long i: arr) {
            System.out.print(" " + i);
        }
        System.out.println();
    }
    
    /**
     * return string in base 10
     * @return decimal represention of the Num instance invoking it
     */
    public String toString() {
        Num number;
        if(this.base() == defaultBase){
            number = this;
        }
        else{
            number = this.convertBase(defaultBase);
        }

        int i;
        StringBuilder res = new StringBuilder();
        int lenofbase = String.valueOf(defaultBase).length() - 1;
        int padding;
        if(this.isNegative){
            res.append("-");
        }
        i = number.len - 1;
        res.append(number.arr[i]);
        for(i = i - 1; i >=0; i--){
            padding = lenofbase - String.valueOf(number.arr[i]).length();
            while(padding>0){
                res.append("0");
                padding--;
            }
            res.append(number.arr[i]);
        }
        return res.toString();
    }

    /**
     * Return number as string without base change.
     * @return
     */
    public String toStringWithoutBaseChange() {
        StringBuilder output = new StringBuilder();
        for(int i=len-1; i>=0; i--)
            output.append(arr[i]);
        return String.valueOf(output);
    }
    
    /**
     * returns base
     * @return base
     */
    public long base() { return base; }

    /**
     * Converts given Num object to new base
     * @param newBase number equal to "this" number, in base=newBase  
     * @return result number in new base
     */
    public Num convertBase(long newBase) {
        Num thisNum = new Num(this.arr);
        long[] newNum = convertBase(thisNum.arr,thisNum.base,newBase);
        Num result = new Num(newNum,newBase);
        return result;
    }

    /**
     * Converts given array object to array having new base
     * @param thisNumArr Array to be converted
     * @param currentBase current base of given array
     * @param newBase base to which array will be converted  
     * @return array with new base
     */
    public long[] convertBase(long[] thisNumArr, long currentBase,long newBase){
        Num ZERO = new Num(0,currentBase);
        int arrSize = 0;
        Num thisNum = new Num(thisNumArr,currentBase);
        Num b = new Num(newBase,currentBase);
        arrSize = getLengthNewBase(len, newBase);
        long[] newNum = new long[1000];
        int i =0;

        //horner's method
        while(thisNum.compareTo(ZERO) > 0){
            newNum[i] = Long.parseLong(mod(thisNum,b).toStringWithoutBaseChange());
            thisNum = divide(thisNum,b);
            i++;
        }

        //removing trailing zeros
        int k = newNum.length -1;
        while(k>=0 && newNum[k] == 0)
            k--;
        if(k == -1)
            return new long[]{0};
        if(k == 0)
            return new long[]{newNum[0]};

        return Arrays.copyOfRange(newNum,0,k+1);
    }
    /**
     * Calculates length of the array in new base
     * @param len length in old base
     * @param base new base to be converted into
     * @return diffLength Modified length of the array in new base
     */
    public int getLengthNewBase(int len, long base) {
        double Log_10 = Math.log10(base);
        int diffLength = (int) Math.ceil(((len + 1) / Log_10) + 1);
        return diffLength;
    }

    /**
     * Divide by 2, for using in binary search
     * @return by2 divides the object by 2
     */
    public Num divideBy2() {
        Num ZERO = new Num(0,this.base);
        Num one = new Num(1,this.base);
        if(this.compareTo(one)==0 || this.compareTo(ZERO) == 0)
            return ZERO;
        long[] result = Arrays.copyOf(this.arr,this.len);
        long carry = 0;
        for(int i=len-1; i>=0 ; i-- ){
            result[i] = result[i] + carry;
            if(result[i] % 2 == 1)
                carry = this.base;
            else
                carry = 0;
            result[i] = result[i] / 2;
        }

        //Removing trailing zeros
        Num by2;
        if(result[result.length-1]==0) {
            by2 = new Num(Arrays.copyOfRange(result, 0, result.length - 1),this.base);
        }
        else
            by2 = new Num(result,this.base);
        return  by2;
    }

   /**
 * Function evaluates the expression in postfix and return the resulting number
 * Parameter contains a string made of either: "+", "-", "*", "^", "/", "%", "0", or
 * a number between 1 to 9
 * @param exp of String type
 * @return Num returns evaluated result
 */
public static Num evaluatePostfix(String[] exp) {
    ArrayDeque<Num> ExpStack = new ArrayDeque<>();

    for(int i=0 ; i<exp.length ; i++){
        if(Character.isDigit(exp[i].charAt(0))){
            ExpStack.push(new Num(exp[i]));
        }
        else{
            Num a = ExpStack.pop();
            Num b = ExpStack.pop();

            switch(exp[i]){
                case "+":
                    ExpStack.push(add(b,a));
                    break;

                case "-":
                    ExpStack.push(subtract(b,a));
                    break;

                case "*":
                    ExpStack.push(product(b,a));
                    break;

                case "^":
                    ExpStack.push(power(b,Long.parseLong(a.toString())));
                    break;

                case "/":
                    ExpStack.push(divide(b,a));
                    break;

                case "%":
                    ExpStack.push(mod(b,a));
                    break;

            }

        }
    }

    return ExpStack.pop();
}

    /**
     * Evaluates given expression returns calculated result. 
     * Parameter will contain a string made of operands("+", "-", "*", "^", "/", "%", "0",) and operands(1-9)
     * @param exp of String type
     * @return Evaluated expression
     */
    public static Num evaluateExp(String expr) throws ParseError {
        StringBuilder sb = new StringBuilder();
        sb.append(expr);
        Queue<String> res = getTokens(sb);
        Num output = evalE(res);
        return output;
    }

    public Boolean printSign() {
        return this.isNegative;
    }


/**
 *  Tokenizes the string and generate token queue.
 * @param sb Expression passed as string builder 
 * @return Queue containing tokens from the expression
 * @throws ParseError User-defined exception to indicate invalid expression string
 */
    public static Queue<String> getTokens(StringBuilder sb) throws ParseError {
        StringBuilder result = new StringBuilder(); // This temp variable will hold each string token
        boolean isPrevDigit = false; // Handling multi-digit numbers
        Queue<String> tokenQueue = new LinkedList<>(); // This queue will hold resulting queue

        if (String.valueOf(sb.charAt(0)).equals("(") || String.valueOf(sb.charAt(0)).equals(")")) {
            result.append(String.valueOf(sb.charAt(0)));
        } else if (sb.length() > 0 && Character.isDigit(sb.charAt(0))) {
            isPrevDigit = true;
            result.append(String.valueOf(sb.charAt(0)));
        } else {
            throw new ParseError("Expression is invalid");
        }

        for (int i = 1; i < sb.length(); i++) { 
            if (String.valueOf(sb.charAt(i)).equals(" ")) { // Ignoring space characters
                continue;
            }

            if (Character.isDigit(sb.charAt(i))) {
                if (isPrevDigit) {
                    result.append(String.valueOf(sb.charAt(i)));
                } else {
                    tokenQueue.add(result.toString());
                    result = new StringBuilder();
                    result.append(String.valueOf(sb.charAt(i)));
                    isPrevDigit = true;
                }
            } else {
                if (String.valueOf(sb.charAt(i)).equals("(") || String.valueOf(sb.charAt(i)).equals(")")) {
                    tokenQueue.add(result.toString());
                    result = new StringBuilder();
                    result.append(String.valueOf(sb.charAt(i)));
                } else {
                    if (isPrevDigit) {
                        tokenQueue.add(result.toString());
                        result = new StringBuilder();
                        result.append(String.valueOf(sb.charAt(i)));
                        isPrevDigit = false;
                    } else {
                        throw new ParseError("Expression is invalid");
                    }
                }                
            }
        }
        tokenQueue.add(result.toString());
        return tokenQueue;
    }

    /**
     * Parses the Queue using first rule of grammar
     * @param qt Queue of type String
     * @return Parsed expression i.e. "2+3"
     * @throws ParseError User-defined exception to indicate invalid expression string
     */
    public static Num evalE(Queue<String> qt) throws ParseError {
        Num val1 = evalT(qt);
        while((qt.size() > 0) && ((qt.peek().equals("+")) ||  (qt.peek().equals("-")))){
            String oper = qt.remove();
            Num val2 = evalT(qt);
            if(oper.equals("+"))
                val1 = add(val1, val2);
            else
                val1 = subtract(val1, val2);
        }
        return val1;
    }

    /**
     * Parses the Queue using second rule of grammar
     * @param qt Queue of type String
     * @return Parsed expression i.e. "15/3"
     * @throws ParseError User-defined exception to indicate invalid expression string
     */
    public static Num evalT(Queue<String> qt) throws ParseError {
        Num val1 = evalF(qt);
        while((qt.size() > 0) && ((qt.peek().equals("*")) ||  (qt.peek().equals("/")))){
            String oper = qt.remove();
            Num val2 = evalF(qt);
            if(oper.equals("*"))
                val1 = product(val1, val2);
            else
                val1 = divide(val1, val2);
        }
        return val1;
    }

    
    /**
     * Parses the Queue using third rule of grammar
     * @param qt Queue of type String
     * @return Parsed expression
     * @throws ParseError User-defined exception to indicate invalid expression string
     */
    public static Num evalF(Queue<String> qt) throws ParseError{
        Num val;
        if(qt.peek().equals("(")){
            String oper = qt.remove();
            val = evalE(qt);
            if (qt.size() <= 0) {
                throw new ParseError("Expression is invalid");
            }
            oper = qt.remove();
        }
        else{
            String num = qt.remove();
            if (!isValidNum(num))
                throw new ParseError("Expression is invalid");
            val = new Num(num);
        }
        return val;
    }

    /**
     * Checks if the given string is a number
     * @param num String to be checked
     * @return Boolean value, True or False
     */
    public static boolean isValidNum(String num) {
        if (num.equals("+") || num.equals("-") || num.equals("*") || num.equals("/") || num.equals(")")) {
            return false;
        }
        return true;
    }

    /**
     * VALID MAIN METHOD IS INSIDE TestLP1.java
     */
    // public static void main(String[] args) {
    //     Num x = new Num("7817827382783728738273872837287837928398");
    //     x = x.convertBase(1212);
    //     x.printList();
    // }

}
