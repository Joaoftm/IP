import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Joao Miranda nº47143;
 *
 *
 */
public class JogarBolhao3 {

	static final int ALTURA_MINIMA = 2;

	/**
	 * Joga o jogo Watch out for the Bolhao!
	 *
	 * @param aviaoX
	 *            Coordenada X da posicao inicial do aviao
	 * @param aviaoY
	 *            Coordenada Y da posicao inicial do aviao
	 * @param iniMaus
	 *            Posicoes iniciais dos Maus
	 * @param altMax
	 *            Altura maxima a que o aviao pode subir
	 * @param dim
	 *            Dimensao do caminho
	 * @param maxVezes
	 *            Numero maximo de jogadas
	 * @param gerador
	 *            O gerador de numeros aleatorios que deve ser usado para a
	 *            geracao de todos os aleatorios
	 * @param leitor
	 *            O objeto Scanner que deve ser usado para ler os dados que o
	 *            Utlizador vai introduzir
	 * @requires aviaoX >= 1 && aviaoX <= dim && aviaoY >= ALTURA_MINIMA &&
	 *           aviaoY <= altMax && iniMaus != null && forall i, 1 <=
	 *           iniMaus[i] <= dim && dim > 0 && dim <= 80 && maxVezes > 0 &&
	 *           gerador != null && leitor != null
	 */
	public static void jogaJogoBolhao3(int aviaoX, int aviaoY, int[] iniMaus,
			int altMax, int dim, int maxVezes, Random gerador, Scanner leitor) {

		//Array para actualizar a posicao dos Maus
		int[] posMaus = iniMaus;

		//Array com tamanho do numero maximo maximo de jogadas
		int[] crateras = new int[maxVezes];

		// Inicializacao de uma variavel contadora das jogadas
		int jogadas = 0;

		//Impressao inicial do caminho
		System.out.println("Configuracao inicial do Caminho:");
		imprimeCaminho(iniMaus, dim, crateras);


		//Inicio do ciclo que ira correr enquanto houver algum mau vivo
		// se não tiver sido feito o numero maximo de jogadas 
		while (mausVivos(posMaus) != 0 && jogadas < maxVezes) {

			//Impressao do caminho e a posicao atual do aviao
			System.out.println(
					"Posicao atual do aviao: (" + aviaoX + "," + aviaoY + ")");

			//Pedir ao utilizador e validar os movimentos hor.e ver. para o aviao
			aviaoX += pedeMovHorizontal(aviaoX, dim, leitor);
			aviaoY += pedeMovVertical(altMax, aviaoY, dim, leitor);

			//Execuçao da fuga dos Maus
			fuga(gerador, aviaoY, dim, posMaus);
			mataMaus(posMaus, crateras);

			//Impressao doo caminho refletindo as novas posicoes dos Maus;
			imprimeCaminho(posMaus, dim, crateras);

			int forcaImpacto = aviaoY / 2;
			int posImpacto = aviaoX;

			//Escrever no ecra a força e a posicao de impacto do Bolhao;
			System.out.println("Apohs impacto do Bolhao com forca "
					+ forcaImpacto + " na posicao " + posImpacto + ":");

			//Se o Bolhao cai numa cratera : informa o utilizador
			if (bolhaoNaCratera(crateras, posImpacto) == true && jogadas != 0)
				System.out.println(
						"O Bolhao nao teve efeito pois caiu numa cratera!");

			//Senao: aplica o efeito do Bolhao,invocando o aplicaBolhao3,e mata os que
			//  cairam nas crateras
			else {
				FuncoesBolhao3.aplicaBolhao3(posImpacto, forcaImpacto, posMaus,
						dim);
				crateras[jogadas] = posImpacto;
			}
			if (mausVivos(posMaus) != 0 && jogadas != (maxVezes - 1))
				imprimeCaminho(posMaus, dim, crateras);

			jogadas++;
		}
		if (mausVivos(posMaus) == 0)

			//O utilizador ganhou
			System.out.println("Parabens! Afogou os Maus ao fim de " + jogadas
					+ (jogadas == 1 ? " jogada!" : " jogadas!"));
		else

			//O utilizador perdeu
			System.out.println("Temos pena! Better luck next time!");
	}

	/**
	 * Metodo de construcao do Caminho do Jogo
	 *
	 * @param aviaoX
	 * 			Coordenada X da posicao inicial do aviao
	 * @param aviaoY
	 * 			Coordenada Y da posicao inicial do aviao
	 * @param posMaus
	 * 			Posicoes dos Maus
	 * @param dim
	 * 			Dimensao do caminho
	 * @requires
	 * 			aviaoX >= 1 && aviaoX <= dim && aviaoY >= ALTURA_MINIMA &&
	 * 			 1 <=posMaus[i] <= dim && dim > 0 && dim <= 80
	 *
	 *
	 */
	public static void imprimeCaminho(int[] posMaus, int dim, int[] crateras) {

		StringBuilder caminho = new StringBuilder();
		StringBuilder numeros = new StringBuilder();
		StringBuilder mDez = new StringBuilder();

		//Acrescenta o numero de casas igual a dim ao caminho
		for (int i = 1; i <= dim; i++) {
			caminho.append(" ");
			numeros.append(i % 10);
			if (i % 10 != 0)
				mDez.append(" ");
			else
				mDez.append(i / 10);
		}

		//Acrescentar os Maus ao caminho
		for (int i = 0; i < posMaus.length; i++) {
			if (posMaus[i] != -1) {
				caminho.setCharAt(posMaus[i] - 1, 'M');
			}
		}

		//Acrescentar as Crateras ao caminho / retirar a posicao delas
		for (int i = 0; i < crateras.length; i++)
			if (crateras[i] != 0)
				numeros.setCharAt(crateras[i] - 1, ' ');

		System.out.println(caminho.toString() + "\n" + numeros.toString() + "\n"
				+ mDez.toString() + "\n");
	}

	/**
	 * Metodo que devolve o movimento Horizontal do aviao,dado pelo Utilizador
	 *
	 * @param aviaoX
	 * 			Coordenada X da posicao inicial do aviao
	 * @param dim
	 * 			Dimensao do caminho
	 * @param leitor
	 * 			O objeto Scanner que deve ser usado para ler os dados que o
	 *            Utlizador vai introduzir
	 * @requires
	 * 			aviaoX >= 1 && aviaoX <= dim && leitor != null &&
	 * 			dim > 0 && dim <= 80
	 * @return
	 * 			Movimento Horizontal do aviao,dado pelo Utilizador
	 */
	public static int pedeMovHorizontal(int aviaoX, int dim, Scanner leitor) {

		//Pede ao Utilizador um valor inteiro para o Mov.Horizontal do aviao
		System.out.print("Movimento horizontal? (Tem que ser inteiro entre "
				+ (1 - aviaoX) + " e " + (dim - aviaoX) + ")");

		return WatchTheBolhao3
				.lerValorNoIntervalo(1 - aviaoX,
						dim - aviaoX, "Tem que ser inteiro entre "
								+ (1 - aviaoX) + " e " + (dim - aviaoX),
						leitor);
	}

	/**
	 * Metodo que devolve o movimento Vertical do aviao,dado pelo utilizador
	 *
	 * @param aviaoY
	 * 			Coordenada Y da posicao inicial do aviao
	 * @param dim
	 * 			Dimensao do caminho
	 * @param leitor
	 * 			O objeto Scanner que deve ser usado para ler os dados que o
	 *            Utlizador vai introduzir
	 * @requires
	 * 			aviaoY >= ALTURA_MINIMA  && leitor != null &&
	 * 			dim > 0 && dim <= 80
	 *
	 * @return
	 * 			Movimento Vertical do aviao,dado pelo Utilizador
	 *
	 */
	public static int pedeMovVertical(int altMax, int aviaoY, int dim,
			Scanner leitor) {

		//Pede ao Utilizador um valor inteiro para o Mov.Vertical do aviao
		System.out.print("Movimento vertical? (Tem que ser inteiro entre "
				+ (ALTURA_MINIMA - aviaoY) + " e " + (altMax - aviaoY) + ")");

		return WatchTheBolhao3.lerValorNoIntervalo(ALTURA_MINIMA - aviaoY,
				altMax - aviaoY, "Tem que ser inteiro entre "
						+ (ALTURA_MINIMA - aviaoY) + " e " + (altMax - aviaoY),
				leitor);

	}

	/**
	 * Metodo que executa a fuga dos Maus,quando o Bolhao cai
	 *
	 * @param gerador
	 * 			O gerador de numeros aleatorios que deve ser usado para a
	 *            geracao de todos os aleatorios
	 * @param aviaoY
	 * 			Coordenada Y da posicao inicial do aviao
	 * @param dim
	 * 			Dimensao do caminho
	 * @requires
	 * 			aviaoY >= ALTURA_MINIMA && gerador != null &&
	 * 			dim > 0 && dim <= 80
	 *
	 */
	public static void fuga(Random gerador, int aviaoY, int dim,
			int posMaus[]) {

		for (int i = 0; i < posMaus.length; i++) {
			if (posMaus[i] != -1) {
				boolean fuga = gerador.nextBoolean();
				if (fuga == true) {
					posMaus[i] += aviaoY;
					if (posMaus[i] >= dim)
						posMaus[i] = dim;

				} else {
					posMaus[i] -= aviaoY;
					if (posMaus[i] <= 1)
						posMaus[i] = 1;
				}
			}
		}
		System.out.println(
				"Maus fogem " + aviaoY + " posicoes antes do Bolhao cair:");
	}

	/**
	 * Metodo que ve se verifica se a posicao de impacto corresponde a de alguma
	 *	cratera
	 *
	 * @param crateras
	 * 			Posicoes das crateras
	 * @param posImpacto
	 * 			Posicao de impacto do Bolhao
	 * @return
	 * 		Retorna se algum Mau caiu /esta em alguma cratera
	 */
	public static boolean bolhaoNaCratera(int[] crateras, int posImpacto) {

		//Criacao de uma Variavel boolean para ver se algum Mau caiu em alguma Cratera
		boolean caiuNaCratera = false;

		//verifica se a posicao de impacto do Bolhao corresponde a de alguma cratera
		for (int i = 0; i < crateras.length && !caiuNaCratera; i++)
			if (crateras[i] == posImpacto) {
				caiuNaCratera = true;
			}
		return caiuNaCratera;
	}


	/**
	 * Metodo que devolve o numero de Maus vivos
	 *
	 * @param posMaus
	 * 			Posicoes dos Maus
	 * @return
	 * 		Retorna o numero de Maus vivos
	 */
	public static int mausVivos(int[] posMaus) {

		//Variavel contadora do numero de Maus ainda vivos
		int count = 0;

		//Ver quandos Maus ainda estao Vivos
		for (int i = 0; i < posMaus.length; i++)
			if (posMaus[i] != -1)
				count++;
		return count;
	}



	/**
	 * Metodo que verifica se a posicao de algum Mau corresponde a de alguma
	 *  cratera,"matando" o Mau
	 *
	 * @param posMaus
	 * 			Posicoes dos Maus
	 * @param crateras
	 * 			Posicoes das crateras
	 */
	public static void mataMaus(int[] posMaus, int[] crateras) {


		for (int i = 0; i < posMaus.length; i++)
			for (int j = 0; j < crateras.length; j++)
				if (posMaus[i] == crateras[j])
					posMaus[i] = -1;
	}
}
