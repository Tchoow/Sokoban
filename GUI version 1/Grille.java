import java.util.Scanner;
import java.io.FileInputStream;

public class Grille
{
	private char[][] grille;   // Grille contenant l'ensemble du plateau 

	private int nbLigne  ;     // Nombre de lignes   de la grille à chaque changement de défis
	private int nbColonne;     // Nombre de colonnes de la grille à chaque changement de défis
	private int nbPas    ;     // Nombre de pas réalisé par le personnage

	private int    numDefi ;   // Numéro du défi actuelle
	private String defiPath;   // Chemin d'accès au fichier du défi

	private int posX;          // Position en abcisse  du personnage
	private int posY;          // Position en ordonnée du personnage

	private boolean[] finis;   // Tableau qui gardera en mémoire si le niveau est finis ou pas
	private String message ;   // Chaine contenant le niveau avec s'il est fini ou non

	// Constructeur de la grille
	public Grille() 
	{
		this.numDefi = 0;

		finis = new boolean[21]; //Initialisation du tableau

		initGrille(); // Appelle la fonction qui permet d'initialiser la grille à chaque changement de niveau
	}

	// Accesseurs
	public int    getNbPas    () { return this.nbPas                    ; }
	public int    getNbLigne  () { return this.nbLigne                  ; }
	public int    getNbColonne() { return this.nbColonne                ; }
	public String getMessage  () { return this.message                  ; }
	public char   getSymbole(int lig, int col) { return grille[lig][col]; }

	public void deplacer(char dir) {
		int deltaX0, deltaY0;
		int deltaX1, deltaY1;

		deltaX1 = this.posX;
		deltaY1 = this.posY;

		deltaX0 = this.deltaX0(dir);
		deltaY0 = this.deltaY0(dir);

		if (this.verifDep(dir))
		{

			if (dir == 'N') deltaY1 -= 2;
			if (dir == 'S') deltaY1 += 2;
			if (dir == 'E') deltaX1 += 2;
			if (dir == 'O') deltaX1 -= 2;

			if (this.grille[deltaY0][deltaX0] == ' ') this.grille[deltaY0][deltaX0] = '@';
			if (this.grille[deltaY0][deltaX0] == '.') this.grille[deltaY0][deltaX0] = '+';

			if (this.grille[deltaY0][deltaX0] == '$' ||
			    this.grille[deltaY0][deltaX0] == '*'  )
			{
				if (this.grille[deltaY1][deltaX1] == '.') this.grille[deltaY1][deltaX1] = '*';
				if (this.grille[deltaY1][deltaX1] == ' ') this.grille[deltaY1][deltaX1] = '$';

				if (this.grille[deltaY0][deltaX0] == '$') this.grille[deltaY0][deltaX0] = '@';
				else                                      this.grille[deltaY0][deltaX0] = '+';
			}

			if (this.grille[this.posY][this.posX] == '@')this.grille[this.posY][this.posX] = ' ';
			else                                         this.grille[this.posY][this.posX] = '.';

			this.posX = deltaX0;
			this.posY = deltaY0;

			estFini();
			this.nbPas++;
			this.setMessage();
		}
	}

	// Passer au defi suivant en vidant tous les attributs et en reloadant la grille
	public void defiSuivant()
	{
		if (this.finis[this.numDefi] && this.numDefi < 20 || this.numDefi == 0)
		{
			this.numDefi++;
			this.flush();
			this.initGrille();
		}
		this.setMessage();
	}

	// Passer au defi precedent en vidant tous les attributs et en reloadant la
	// grille
	public void defiPrecedent()
	{
		if (this.numDefi > 1)
		{
			this.numDefi--;
			this.flush();
			this.initGrille();
		}

		this.setMessage();
	}

	// Passer au defi suivant en vidant tous les attributs et en reloadant la grille
	public void recommencer()
	{
		this.flush();
		this.initGrille();
		this.setMessage();
	}

	// Actualise le message
	private void setMessage(){ if ( this.finis[this.numDefi] ) this.message = "Niveau : " + numDefi + " fini"; }

	// Verifier si le déplacement du pousseur est possible
	private boolean verifDep(char dir)
	{
		int deltaX0, deltaY0;
		int deltaX1, deltaY1;

		deltaX0 = this.deltaX0(dir);
		deltaY0 = this.deltaY0(dir);
		deltaX1 = this.posX;
		deltaY1 = this.posY;

		if (dir == 'N') deltaY1 -= 2;
		if (dir == 'S') deltaY1 += 2;
		if (dir == 'E') deltaX1 += 2;
		if (dir == 'O') deltaX1 -= 2;

		if (this.grille[deltaY0][deltaX0] == '#'  ||
		   (this.grille[deltaY0][deltaX0] == '$'  || 
		    this.grille[deltaY0][deltaX0] == '*') && 
		   (this.grille[deltaY1][deltaX1] == '#'  ||
		    this.grille[deltaY1][deltaX1] == '$'  ||
			this.grille[deltaY1][deltaX1] == '*'  ))
			return false;

		return true;
	}

	// Recuperer le poteniel abssice de la position après le pousseur;
	private int deltaX0(char dir)
	{
		if (dir == 'N') return this.posX;
		if (dir == 'S') return this.posX;
		if (dir == 'E') return this.posX + 1;
		if (dir == 'O') return this.posX - 1;
		return 0;
	}

	// Recuperer la potenielle ordonnée de la position après le pousseur;
	private int deltaY0(char dir)
	{
		if (dir == 'N') return this.posY - 1;
		if (dir == 'S') return this.posY + 1;
		if (dir == 'E') return this.posY;
		if (dir == 'O') return this.posY;
		return 0;
	}

	// Remet tous les attributs a 0;
	private void flush()
	{
		this.posX = 0;
		this.posY = 0;

		this.nbColonne = 0;
		this.nbLigne   = 0;

		this.nbPas = 0;
	}

	// Verifie si le niveau est finis
	private void estFini ()
	{
		if ( !this.finis[this.numDefi] )
		{
			for ( char[] i : this.grille )
				for ( char j : i )
					if ( j == '$' ) return;

			this.finis[this.numDefi] = true;
		}
	}

	// Initialise la grille en fonction de ce qui a été trouvé dans le fichier.
	private void initGrille()
	{
		String line;
		line = "";

		this.message = "Niveau : " + numDefi;

		this.grille = new char[30][30];

		this.nbColonne = 0;
		this.nbLigne = 0;
		this.nbPas = 0;

		try
		{
			if (this.numDefi >= 10)
				this.defiPath = "../defis/defi" + this.numDefi + ".xsb";
			else
				this.defiPath = "../defis/defi0" + this.numDefi + ".xsb";

			Scanner sc = new Scanner(new FileInputStream(defiPath));

			while (sc.hasNextLine())
			{
				line = sc.nextLine();
				this.nbColonne = 0;
				for (int i = 0; i < line.length(); i++) {
					this.grille[this.nbLigne][this.nbColonne] = line.charAt(i);
					if (this.grille[this.nbLigne][this.nbColonne] == '@') {
						this.posX = this.nbColonne;
						this.posY = this.nbLigne;
					}
					this.nbColonne++;
				}
				this.nbLigne++;
			}

			sc.close();
		} 
		catch (Exception e) { e.printStackTrace(); }
	}
}