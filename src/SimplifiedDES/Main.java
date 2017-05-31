package SimplifiedDES;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("--- Simplified DES iniciado ---");
		System.out.println();
		
		boolean continuaExecucao = true;		
		while (continuaExecucao) {
			continuaExecucao = exibeOpcoes();
		}
		
		System.out.println("--- Simplified DES encerrado ---");
		System.out.println();
	}
	
	public static boolean exibeOpcoes() throws Exception {
		System.out.println("Selecione a opção desejada:");
		System.out.println("1 - Criptografar e descriptografar texto.");
		System.out.println("2 - Criptografar e descriptografar binário.");
		System.out.println("3 - Executar teste com o caso resolvido em sala.");
		System.out.println("4 - Executar testes utilizados durante o desenvolvimento.");
		System.out.println();
		
		int opcao = new Scanner(System.in).nextInt();
		System.out.println();
		
		switch (opcao) {
		case 1:
			System.out.print("Digite o valor binário (com 10 bits) da chave a ser utilizada: ");
			String chave = new Scanner(System.in).nextLine();
			System.out.println();
			
			System.out.print("Digite o texto a ser criptografado: ");
			String texto = new Scanner(System.in).nextLine();
			System.out.println();
			
			executarOpcao1(chave, texto);
			
			break;
		case 2:
			System.out.print("Digite o valor binário (com 10 bits) da chave a ser utilizada: ");
			String chave2 = new Scanner(System.in).nextLine();
			System.out.println();
			
			System.out.print("Digite o valor binário (com 8 bits) a ser criptografado: ");
			String binario = new Scanner(System.in).nextLine();
			System.out.println();
			
			executarOpcao2(chave2, binario);
			
			break;			
		case 3:
			testarCasoResolvidoEmSala();
			break;			
		case 4:
			executarTestesUtilizadosDuranteDesenvolvimento();
			break;
		default:
			System.out.println("Opção inválida.");
			break;
		}
		
		System.out.println("Deseja realizar outra operação?");
		System.out.print("Digite 1 para sim ou 2 para não: ");
		String opcaoContinuacao = new Scanner(System.in).nextLine();
		System.out.println();
		return opcaoContinuacao.equals("1");
	}
	
	public static void executarOpcao1(String chave, String texto) throws Exception {
		SimplifiedDES des = new SimplifiedDES(chave);
		
		String encrypted = des.encryptText(texto);
		String decrypted = des.decryptText(encrypted);
		
		System.out.println("--- Resultado do encriptação/descriptação de texto ---");
		System.out.println("Texto em binário     : "+Utils.convertAsciiToBinaryString(texto));
		System.out.println("Binário encriptado   : "+encrypted);
		System.out.println("Binário decriptado   : "+decrypted);
		System.out.println("Decriptação em texto : "+Utils.convertBinaryStringToText(decrypted));
		System.out.println("Texto original       : "+texto);
		System.out.println();
	}
	
	public static void executarOpcao2(String chave, String binario) throws Exception {
		SimplifiedDES des = new SimplifiedDES(chave);
		String encrypted = des.encrypt(binario);
		String decrypted = des.decrypt(encrypted);
		String valorOriginal = (binario.length() > 8 ? binario.substring(0, 8) : binario);
		
		System.out.println("--- Resultado do encriptação/descriptação de binário ---");
		System.out.println("Valor encriptado : "+encrypted);
		System.out.println("Valor decriptado : "+decrypted);
		System.out.println("Valor original   : "+valorOriginal);
		System.out.println();
	}
	
	public static void testarCasoResolvidoEmSala() throws Exception {
		String key = "1100100111";
		String originalValue = "00110011";
		
		System.out.println("Key                      : "+key);
		System.out.println("Value to encript/decript : "+originalValue);
		System.out.println();
		
		SimplifiedDES des = new SimplifiedDES(key);
		String encrypted = des.encrypt(originalValue);
		String decrypted = des.decrypt(encrypted);
		
		System.out.println("--- Resultado do encriptação/descriptação de binário ---");
		System.out.println("encrypted      : "+encrypted);
		System.out.println("decrypted      : "+decrypted);
		System.out.println("original value : "+originalValue);
		System.out.println();
	}
	
	public static void executarTestesUtilizadosDuranteDesenvolvimento() throws Exception {
		String key = "1100100111";
		String originalValue = "00110011";

		System.out.println("Key                      : "+key);
		System.out.println("Value to encript/decript : "+originalValue);
		System.out.println();
		
		SimplifiedDES des = new SimplifiedDES(key);
		String encrypted = des.encrypt(originalValue);
		String decrypted = des.decrypt(encrypted);
		
		System.out.println("--- Resultado do encriptação/descriptação de binário ---");
		System.out.println("encrypted      : "+encrypted);
		System.out.println("decrypted      : "+decrypted);
		System.out.println("original value : "+originalValue);
		System.out.println();
		
		originalValue = "he";
		
		System.out.println("Key                      : "+key);
		System.out.println("Value to encript/decript : "+originalValue);
		System.out.println();
		
		encrypted = des.encryptText(originalValue);
		decrypted = des.decryptText(encrypted);
		
		System.out.println("--- Resultado do encriptação/descriptação de texto ---");		
		System.out.println("encrypted          : "+encrypted);
		System.out.println("decrypted          : "+decrypted);
		System.out.println("original binary    : "+Utils.convertAsciiToBinaryString(originalValue));
		System.out.println("decrypted in ascii : "+Utils.convertBinaryStringToText(decrypted));
		System.out.println("original text      : "+originalValue);
		System.out.println();
	}
}
