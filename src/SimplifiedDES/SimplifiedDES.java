package SimplifiedDES;

import java.util.BitSet;

import javax.rmi.CORBA.Util;

public class SimplifiedDES {
	private BitSet k1;
	private BitSet k2;
	
	private final int[] P10Mask = new int[]{3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
	private final int[] P8Mask = new int[]{6, 3, 7, 4, 8, 5, 10, 9};
	private final int[] P4Mask = new int[]{2, 4, 3, 1};
	private final int[] IPMask = new int[]{2, 6, 3, 1, 4, 8, 5, 7};
	private final int[] IPInvMask = new int[]{4, 1, 3, 5, 7, 2, 8, 6};
	private final int[] EPMask = new int[]{4, 1, 2, 3, 2, 3, 4, 1};
	
	private final int[][] S0Matrix = new int[][]{
		  { 1, 0, 3, 2 },
		  { 3, 2, 1, 0 },
		  { 0, 2, 1, 3 },
		  { 3, 1, 3, 2 }
	};	
	private final int[][] S1Matrix = new int[][]{
		  { 0, 1, 2, 3 },
		  { 2, 0, 1, 3 },
		  { 3, 0, 1, 0 },
		  { 2, 1, 0, 3 }
	};	
	
	public SimplifiedDES(String key) throws Exception {			
		if (key.length() != 10) {
			throw new Exception("Key must have 10 bits.");			
		}
		
		if (!key.matches("^[01]+$")) {
			throw new Exception("Invalid key.");
		}
		
		BitSet keyBitSet = Utils.createBitSetFromString(key);
		generateKeys(keyBitSet);

		System.out.println("-- Chaves geradas ---");
		System.out.println("k1: "+Utils.convertBitSetToString(k1, 8));
		System.out.println("k2: "+Utils.convertBitSetToString(k2, 8));
		System.out.println();
	}
	
	public String encrypt(String input) throws Exception {		
		return Fk(input, "encrypt");		
	}
	
	public String decrypt(String input) throws Exception {
		return Fk(input, "decrypt");
	}
	
	public String encryptText(String input) {	
		String binary = "";
		String text = "";
		try {
			BitSet[] bitSets = new BitSet[input.length()]; 
			for (int i = 0; i < input.length(); i++) {
				String binaryStringOfLetter = Utils.convertAsciiToBinaryString(String.valueOf(input.charAt(i)));
				String binaryResult = "";
				binaryResult = encrypt(binaryStringOfLetter);
				bitSets[i] = Utils.createBitSetFromString(binaryResult);
				binary += binaryResult;
				text += Utils.convertBinaryStringToText(binary);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}					
		return binary;
	}
	
	public String decryptText(String input) {	
		String binary = "";
		String text = "";
		try {
			String[] inputParts = Utils.splitBinaryString(input, 8);
			for (int i = 0; i < inputParts.length; i++) {
				binary += decrypt(inputParts[i]);
				text += Utils.convertBinaryStringToText(binary);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}					
		return binary;
	}
	
	private void generateKeys(BitSet key) {				
		BitSet P10 = P10(key);
		BitSet[] P10Divided = Utils.divideBitSet(P10, 10);
		BitSet LS1_1 = Utils.leftShift(P10Divided[0], 1, 5);
		BitSet LS1_2 = Utils.leftShift(P10Divided[1], 1, 5);
		BitSet UnionOfLS1 = Utils.unionBitSet(LS1_1, LS1_2, 10);
		k1 = P8(UnionOfLS1);
		BitSet LS2_1 = Utils.leftShift(LS1_1, 2, 5);
		BitSet LS2_2 = Utils.leftShift(LS1_2, 2, 5);
		BitSet UnionOfLS2 = Utils.unionBitSet(LS2_1, LS2_2, 10);
		k2 = P8(UnionOfLS2);
	}
	
//	private void generateKeys(BitSet key) {				
//		BitSet P10 = P10(key);
//		System.out.println("P10: "+Utils.convertBitSetToString(P10,10));
//		BitSet[] P10Divided = Utils.divideBitSet(P10, 10);
//		System.out.println("P10 - 1: "+Utils.convertBitSetToString(P10Divided[0],5));
//		System.out.println("P10 - 2: "+Utils.convertBitSetToString(P10Divided[1],5));
//		BitSet LS1_1 = Utils.leftShift(P10Divided[0], 1, 5);
//		BitSet LS1_2 = Utils.leftShift(P10Divided[1], 1, 5);
//		System.out.println("LS1 - 1: "+Utils.convertBitSetToString(LS1_1,5));
//		System.out.println("LS1 - 2: "+Utils.convertBitSetToString(LS1_2,5));
//		BitSet UnionOfLS1 = Utils.unionBitSet(LS1_1, LS1_2, 10);
//		System.out.println("UnionOfLS1: "+Utils.convertBitSetToString(UnionOfLS1,10));
//		k1 = P8(UnionOfLS1);
//		System.out.println("k1: "+Utils.convertBitSetToString(k1,8));
//		BitSet LS2_1 = Utils.leftShift(LS1_1, 2, 5);
//		BitSet LS2_2 = Utils.leftShift(LS1_2, 2, 5);
//		System.out.println("LS2 - 1: "+Utils.convertBitSetToString(LS2_1,5));
//		System.out.println("LS2 - 2: "+Utils.convertBitSetToString(LS2_2,5));
//		BitSet UnionOfLS2 = Utils.unionBitSet(LS2_1, LS2_2, 10);
//		System.out.println("UnionOfLS2: "+Utils.convertBitSetToString(UnionOfLS2,10));
//		k2 = P8(UnionOfLS2);
//		System.out.println("k2: "+Utils.convertBitSetToString(k2,8));
//	}
	
	private String Fk(String binaryInput, String action) throws Exception {
		BitSet input = Utils.createBitSetFromString(binaryInput);
		BitSet[] keys;
		
		switch (action) {
		case "encrypt":
			keys = new BitSet[]{k1, k2}; 
			break;
		case "decrypt":
			keys = new BitSet[]{k2, k1};
			break;
		default:
			throw new Exception("Invalid Fk action.");			
		}
		
		//First box
		BitSet IP = IP(input);
		BitSet[] IPDivided = Utils.divideBitSet(IP, 8);
		BitSet EP = EP(IPDivided[1]);
		EP.xor(keys[0]);
		BitSet[] EPXorK1Divided = Utils.divideBitSet(EP, 8);
		BitSet S0 = S0(EPXorK1Divided[0]);
		BitSet S1 = S1(EPXorK1Divided[1]);
		BitSet S = Utils.unionBitSet(S0, S1, 4);
		BitSet P4 = P4(S);
		P4.xor(IPDivided[0]);
		BitSet[] SW = SW(P4, IPDivided[1]);
		
		//Second box
		BitSet EP_2 = EP(SW[1]);
		EP_2.xor(keys[1]);
		BitSet[] EP2XorK2Divided = Utils.divideBitSet(EP_2, 8);
		BitSet S0_2 = S0(EP2XorK2Divided[0]);
		BitSet S1_2 = S1(EP2XorK2Divided[1]);
		BitSet S_2 = Utils.unionBitSet(S0_2, S1_2, 4);
		BitSet P4_2 = P4(S_2);
		P4_2.xor(SW[0]);
		BitSet inputForIPInv = Utils.unionBitSet(P4_2, SW[1], 8);
		BitSet IPInv = IPInv(inputForIPInv);
		
		return Utils.convertBitSetToString(IPInv, 8);
	}
	
//	private String Fk(String binaryInput, String action) throws Exception {
//		BitSet input = Utils.createBitSetFromString(binaryInput);
//		BitSet[] keys;
//		
//		switch (action) {
//		case "encrypt":
//			keys = new BitSet[]{k1, k2}; 
//			break;
//		case "decrypt":
//			keys = new BitSet[]{k2, k1};
//			break;
//		default:
//			throw new Exception("Invalid Fk action.");			
//		}
//		
//		//First box
//		System.out.println("Starting Fk ("+action+")...");
//		System.out.println("input: "+Utils.convertBitSetToString(input, 8));
//		BitSet IP = IP(input);
//		System.out.println("IP: "+Utils.convertBitSetToString(IP, 8));
//		BitSet[] IPDivided = Utils.divideBitSet(IP, 8);
//		System.out.println("IPDivided 0: "+Utils.convertBitSetToString(IPDivided[0], 4));
//		System.out.println("IPDivided 1: "+Utils.convertBitSetToString(IPDivided[1], 4));
//		BitSet EP = EP(IPDivided[1]);
//		System.out.println("EP: "+Utils.convertBitSetToString(EP, 8));
//		EP.xor(keys[0]);
//		System.out.println("EP xor: "+Utils.convertBitSetToString(EP, 8));
//		BitSet[] EPXorK1Divided = Utils.divideBitSet(EP, 8);
//		System.out.println("EP xor Divided 0: "+Utils.convertBitSetToString(EPXorK1Divided[0], 4));
//		System.out.println("EP xor Divided 1: "+Utils.convertBitSetToString(EPXorK1Divided[1], 4));
//		BitSet S0 = S0(EPXorK1Divided[0]);
//		BitSet S1 = S1(EPXorK1Divided[1]);
//		System.out.println("S0: "+Utils.convertBitSetToString(S0, 2));
//		System.out.println("S1: "+Utils.convertBitSetToString(S1, 2));
//		BitSet S = Utils.unionBitSet(S0, S1, 4);
//		System.out.println("S: "+Utils.convertBitSetToString(S, 4));
//		BitSet P4 = P4(S);
//		System.out.println("P4: "+Utils.convertBitSetToString(P4, 4));
//		P4.xor(IPDivided[0]);
//		System.out.println("P4 xor IPDivided 0: "+Utils.convertBitSetToString(P4, 4));
//		BitSet[] SW = SW(P4, IPDivided[1]);
//		System.out.println("IPDivided 1: "+Utils.convertBitSetToString(IPDivided[1], 4));
//		System.out.println("SW 0: "+Utils.convertBitSetToString(SW[0], 4));
//		System.out.println("SW 1: "+Utils.convertBitSetToString(SW[1], 4));
//		
//		//Second box
//		BitSet EP_2 = EP(SW[1]);
//		System.out.println("EP_2: "+Utils.convertBitSetToString(EP_2, 8));
//		EP_2.xor(keys[1]);
//		System.out.println("EP_2 xor: "+Utils.convertBitSetToString(EP_2, 8));
//		BitSet[] EP2XorK2Divided = Utils.divideBitSet(EP_2, 8);
//		System.out.println("EP_2 xor Divided 0: "+Utils.convertBitSetToString(EP2XorK2Divided[0], 4));
//		System.out.println("EP_2 xor Divided 1: "+Utils.convertBitSetToString(EP2XorK2Divided[1], 4));
//		BitSet S0_2 = S0(EP2XorK2Divided[0]);
//		BitSet S1_2 = S1(EP2XorK2Divided[1]);
//		System.out.println("S0_2: "+Utils.convertBitSetToString(S0_2, 2));
//		System.out.println("S1_2: "+Utils.convertBitSetToString(S1_2, 2));
//		BitSet S_2 = Utils.unionBitSet(S0_2, S1_2, 4);
//		System.out.println("S_2: "+Utils.convertBitSetToString(S_2, 4));
//		BitSet P4_2 = P4(S_2);
//		System.out.println("P4_2: "+Utils.convertBitSetToString(P4_2, 4));
//		P4_2.xor(SW[0]);
//		System.out.println("P4_2 xor SW 0: "+Utils.convertBitSetToString(P4_2, 4));
//		BitSet inputForIPInv = Utils.unionBitSet(P4_2, SW[1], 8);
//		System.out.println("inputForIPInv: "+Utils.convertBitSetToString(inputForIPInv, 8));
//		BitSet IPInv = IPInv(inputForIPInv);
//		System.out.println("IPInv: "+Utils.convertBitSetToString(IPInv, 8));
//		
//		return Utils.convertBitSetToString(IPInv, 8);
//	}
	
	private BitSet IP(BitSet input) {
		return transformBasedOnMask(input, IPMask);		
	}
	
	private BitSet IPInv(BitSet input) {
		return transformBasedOnMask(input, IPInvMask);		
	}
	
	private BitSet EP(BitSet input) {
		return transformBasedOnMask(input, EPMask);		
	}
	
	private BitSet S0(BitSet input) {
		return S(input, S0Matrix);
	}
	
	private BitSet S1(BitSet input) {
		return S(input, S1Matrix);
	}
	
	private BitSet S(BitSet input, int[][] matrix) {
		String binaryLine = "";
		String binaryColumn = "";
		
		binaryLine += input.get(0) ? "1" : "0";
		binaryLine += input.get(3) ? "1" : "0";
		
		binaryColumn += input.get(1) ? "1" : "0";
		binaryColumn += input.get(2) ? "1" : "0";
		
		int line = Integer.parseInt(binaryLine, 2);
		int column = Integer.parseInt(binaryColumn, 2);
		
		String valueFromMatrix = Integer.toBinaryString(matrix[line][column]);
		valueFromMatrix = valueFromMatrix.length() != 2 ? "0" + valueFromMatrix : valueFromMatrix;	
		
		return Utils.createBitSetFromString(valueFromMatrix);
	}

	private BitSet P10(BitSet input) {		
		return transformBasedOnMask(input, P10Mask);
	}
	
	private BitSet P8(BitSet input) {		
		return transformBasedOnMask(input, P8Mask);
	}
	
	private BitSet P4(BitSet input) {		
		return transformBasedOnMask(input, P4Mask);
	}
	
	private BitSet transformBasedOnMask(BitSet input, int[] mask) {
		BitSet result = new BitSet(mask.length);
		
		for (int i = 0; i < mask.length; i++) {
			result.set(i, input.get(mask[i]-1));
		}
		
		return result;
	}	
	
	private BitSet[] SW(BitSet input1, BitSet input2) {
		return new BitSet[]{input2, input1};
	}	
}
