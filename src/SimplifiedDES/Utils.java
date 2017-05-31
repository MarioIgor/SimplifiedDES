package SimplifiedDES;

import java.util.BitSet;

public class Utils {

	public static void printBits(BitSet input, int size) {
      	System.out.println(convertBitSetToString(input, size));
	}
	
	public static String convertBitSetToString(BitSet input, int size) {
		String result = "";
      	for (int i = 0; i < size; i++) {
      		result += input.get(i) ? "1" : "0";
      	}
      	return result;
	}
	
	public static String convertAsciiToBinaryString(String text){  
        byte[] bytes = text.getBytes();  
        StringBuilder binary = new StringBuilder();  
        for (byte b : bytes)  
        {  
           int val = b;  
           for (int i = 0; i < 8; i++)  
           {  
              binary.append((val & 128) == 0 ? 0 : 1);  
              val <<= 1;  
           }  
        }  
        return binary.toString();  
	} 
	
	public static BitSet createBitSetFromString(String input){
		BitSet result = new BitSet(input.length());
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '1') {
				result.set(i);
			}
		}
		return result;
	}
	
	public static String convertBinaryStringToText(String binary) {
		String s2 = "";   
		char nextChar;

		for(int i = 0; i <= binary.length()-8+1; i += 8) {
			nextChar = (char)Integer.parseInt(binary.substring(i, i+8), 2);
			s2 += nextChar;
		}
		
		return s2;
	}
	
	public static String[] splitBinaryString(String input, int lengthOfEachPart) {		
		Double lengthOfResults = Math.ceil((double)input.length()/(double)lengthOfEachPart);
		String[] results = new String[lengthOfResults.intValue()];   

		int j = 0;
		for(int i = 0; i <= input.length()-lengthOfEachPart+1; i += lengthOfEachPart) {
			results[j] = input.substring(i, i+lengthOfEachPart);
			j++;
		}
		
		return results;
	}
	
	public static BitSet leftShift(BitSet input, int offset, int size) {
		BitSet result = new BitSet(size);		
		
		for (int i = 0; i < size; i++) {
			int indexOfNewValue = ((i+offset) < size) ? i+offset : (i+offset) - size;
			result.set(i, input.get(indexOfNewValue));
		}
		
		return result;
	}	
	
	public static BitSet[] divideBitSet(BitSet input, int size) {
		int halfOfSize = (int)(size/2); 
				
		BitSet bitSet1 = new BitSet(halfOfSize);
		BitSet bitSet2 = new BitSet(halfOfSize);
		
		for (int i = 0; i < halfOfSize; i++) {
			bitSet1.set(i, input.get(i));
		}
		int i = 0;
		for (int j = halfOfSize; j < size; j++) {
			bitSet2.set(i, input.get(j));
			i++;
		}
					
		return new BitSet[]{bitSet1, bitSet2};
	}
	
	public static BitSet unionBitSet(BitSet input1, BitSet input2, int size) {
		BitSet result = new BitSet(size);				

		for (int i = 0; i < size; i++) {
			BitSet input;
			int indexOfNewValue;
			if (i < (size/2)) {
				input = input1;
				indexOfNewValue = i;
			} else {
				input = input2;
				indexOfNewValue = i - (size/2);
			}
			result.set(i, input.get(indexOfNewValue));
		}
						
		return result;
	}
}