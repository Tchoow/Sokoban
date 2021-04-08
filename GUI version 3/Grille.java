import java.util.ArrayList;
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

	// Attribus relatifs au changements de thème
	private String[] tabNom = { "classique", "link", "geometrique", "nature", "espace", "geometrique2", "nature2" };
	private String   theme  ;
	private int      themeId;

	private char curDir;         // Direction du dernier déplacement fais par le personnage

	// Attributs utiles au CR-Z
	private boolean bCrz;                 // Permet de savoir si le déplacement actuel est en lien avec un CR-Z
	private ArrayList<Character> alMouv; // Liste dynamique qui enregistre les mouvements de déplacement

	// Constructeur de la grille
	public Grille () 
	{
		// Initialisation de variables qui ne changerons pas
		// même si la grille change ou est actualiser

		this.finis   = new boolean[21]; // Instanciation du tableau
		
		this.theme   = "";
		this.themeId = 0 ;

		this.nbPas   = 0;
		this.numDefi = 0;

		this.initGrille();  // Appelle la fonction qui permet d'initialiser la grille du défi correspondant
	}


	// Accesseurs
	public int     getNbPas    ()                 { return this.nbPas                    ; }
	public int     getNbLigne  ()                 { return this.nbLigne                  ; }
	public int     getNbColonne()                 { return this.nbColonne                ; }
	public String  getMessage  ()                 { return this.message                  ; }
	public char    getSymbole  (int lig, int col) { return this.grille[lig][col]         ; }
	public char    getCurDir   ()                 { return this.curDir                   ; }

	// Changer le thème actuel
	public String getTheme() 
	{
		
		if( this.themeId == 6) this.themeId = 0;      // On passe au thème suivant
		else                   this.themeId++  ;

		switch( this.themeId )
		{
			case 0 -> this.theme = this.tabNom[0]; 
			case 1 -> this.theme = this.tabNom[1];
			case 2 -> this.theme = this.tabNom[2];
			case 3 -> this.theme = this.tabNom[3];
			case 4 -> this.theme = this.tabNom[4];
			case 5 -> this.theme = this.tabNom[5];
			case 6 -> this.theme = this.tabNom[6];
		};

		return this.theme;
	}


	public void deplacer ( char dir ) 
	{
		int deltaX0, deltaY0;
		int deltaX1, deltaY1;

		deltaX1 = this.posX;
		deltaY1 = this.posY;

		deltaX0 = this.deltaX0(dir);
		deltaY0 = this.deltaY0(dir);

		if ( this.verifDep(dir) )
		{
			switch ( dir )
			{
				case 'N' -> deltaY1 -= 2;
				case 'S' ->	deltaY1 += 2;
				case 'E' -> deltaX1 += 2;
				case 'O' -> deltaX1 -= 2;
			};

			this.curDir = dir;

			// Ici on gère la situations du déplacement simple
			if ( this.grille[deltaY0][deltaX0] == ' ' ) this.grille[deltaY0][deltaX0] = '@';
			if ( this.grille[deltaY0][deltaX0] == '.' ) this.grille[deltaY0][deltaX0] = '+';


			// On gère la situation où le personnage se déplace sur une caisse ou une caisse sur un point
			if ( this.grille[deltaY0][deltaX0] == '$' ||
			     this.grille[deltaY0][deltaX0] == '*'   )
			{
				if ( this.grille[deltaY1][deltaX1] == '.' ) this.grille[deltaY1][deltaX1] = '*';
				if ( this.grille[deltaY1][deltaX1] == ' ' ) this.grille[deltaY1][deltaX1] = '$';

				if ( this.grille[deltaY0][deltaX0] == '$' ) this.grille[deltaY0][deltaX0] = '@';
				else                                        this.grille[deltaY0][deltaX0] = '+';
			}

			// On regarde si le personnage est sur une case de rangement alors il faut remettre le point 
			if ( this.grille[this.posY][this.posX] == '@' ) this.grille[this.posY][this.posX] = ' ';
			else                                            this.grille[this.posY][this.posX] = '.';

			// On effectue le déplacement du personnage 
			this.posX = deltaX0;
			this.posY = deltaY0;

			if ( !bCrz )
			{
				this.nbPas++;
				this.alMouv.add(dir);
			}

			estFini();
			this.setMessage();
		}
	}

	public void retourArriere ()
	{
		// L'objectif est de réinitialiser la grille 
		// et de refaire tous les mouvements sauf le dernier.

		// S'il y a eu au moins un mouvement alors on enclenche l'effet du CR-Z
		if ( this.alMouv.size()>0)
		{
			initGrille();
			this.nbPas++;
			this.bCrz = true;
			for (int i = 0; i<this.alMouv.size()-1; i++ ) this.deplacer(this.alMouv.get(i));

			this.alMouv.remove(this.alMouv.size()-1);

			this.setMessage();
			this.bCrz = false;
		}

	}

	// Passe au defi suivant
	public void defiSuivant ()
	{
		// Vérifie que le défi existe
		if ( this.finis[this.numDefi] && this.numDefi < 20  || this.numDefi == 0 )
		{
			this.numDefi++;
			this.flush();
			this.initGrille();
			this.nbPas = 0   ;
		}

		this.setMessage();
	}

	// Passer au defi precedent
	public void defiPrecedent ()
	{
		if ( this.numDefi > 1 ) // Evite de passer a des défis non existants
		{
			this.numDefi--;
			this.flush();
			this.initGrille();
			this.nbPas = 0   ;
		}

		this.setMessage();
	}

	// Recommence le défi à 0
	public void recommencer ()
	{
		this.flush();
		this.initGrille();
		this.nbPas = 0   ;

		this.setMessage();
	}

	// Actualise le message
	private void setMessage(){ if ( this.finis[this.numDefi] ) this.message = "Niveau : " + numDefi + " fini"; }

	// Verifier si le déplacement du pousseur est possible
	private boolean verifDep ( char dir )
	{
		int deltaX0, deltaY0; //La position suivante le pousseur
		int deltaX1, deltaY1; //La position suivante celle obtenue par deltaX0, deltaY0

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

	// Retourne l'abscisse suivante le pousseur
	private int deltaX0 ( char dir )
	{
		if (dir == 'N') return this.posX;
		if (dir == 'S') return this.posX;
		if (dir == 'E') return this.posX + 1;
		if (dir == 'O') return this.posX - 1;

		return 0;
	}

	// Retourne l'ordonnée suivante le pousseur
	private int deltaY0 ( char dir )
	{
		if (dir == 'N') return this.posY - 1;
		if (dir == 'S') return this.posY + 1;
		if (dir == 'E') return this.posY;
		if (dir == 'O') return this.posY;

		return 0;
	}

	// Reinitialise les attributs nécessaire à une manche
	private void flush ()
	{
		this.posX = 0;
		this.posY = 0;

		this.nbColonne = 0;
		this.nbLigne   = 0;

		this.nbPas = 0;

		this.curDir = 'S';

		this.alMouv = new ArrayList<>();
	}

	// Verifie si le niveau est ou a été finis
	private void estFini ()
	{
		if ( !this.finis[this.numDefi] )
		{
			for ( char[] i : grille )
				for ( char j : i )
					if ( j == '$' ) return;

			this.finis[this.numDefi] = true;
		}
	}

	// Initialise la grille en fonction de ce qui a été trouvé dans le fichier.
	private void initGrille ()
	{
		String line;
		line = "";
		
		this.message = "Niveau : " + numDefi;

		/*----------------------------------*/
		/* Attributs propre à chaque manche */
		/*----------------------------------*/
		this.grille = new char[30][30];

		this.nbColonne = 0;
		this.nbLigne   = 0;
		this.bCrz      = false;

		try
		{
			// Lit le fichier correspondant au numero du defi
			if ( this.numDefi >= 10 )
				this.defiPath = "../defis/defi" + this.numDefi + ".xsb";
			else
				this.defiPath = "../defis/defi0" + this.numDefi + ".xsb";

			Scanner sc = new Scanner ( new FileInputStream ( defiPath ), "UTF8"  );

			while ( sc.hasNextLine() )
			{
				line = sc.nextLine();
				this.nbColonne = 0;
				for ( int i = 0; i < line.length(); i++ ) 
				{

					this.grille[this.nbLigne][this.nbColonne] = line.charAt(i);

					if ( this.grille[this.nbLigne][this.nbColonne] == '@' ) 
					{
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