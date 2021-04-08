import ihmgui.*;

public class Controleur extends Controle 
{
	private FrameGrille frame                ;  // Classe Vue
	private Grille      metier               ;  // Classe Metier // inchangé par rapport au mode CUI
	private String      sTheme = "classique" ;  // Cet attribut permet de stocker le thème original


	/* --------------------------------------------------------------------- */
	// Constructeur


	public Controleur ()
	{
		this.metier  = new Grille ()           ;  // instanciation de la partie  Metier
		this.frame   = new FrameGrille ( this );  // instanciation de la fenêtre graphique

		this.metier.defiSuivant()              ;  // Permet de passer le niveau de paramétrage (defi00.xsb)


		// Paramétrage de la fenêtre graphique
		frame.setSize     ( 1430, 800        );
		frame.setLocation ( 1,  1            );
		frame.setTitle    ( "Sokoban"        );
		frame.setVisible  ( true             );
	}


	/* --------------------------------------------------------------------- */
	// Cette méthode crée les boutons


	public String setBouton ( int numBtn )
	{
		String sRet;

		switch ( numBtn )
		{
			case 0  -> sRet = "Defi Precedent";
			case 1  -> sRet = "Defi Suivant"  ;
			case 2  -> sRet = "Recommencer"   ;
			default -> sRet = null;
		}

		return sRet;
	}


	/* --------------------------------------------------------------------- */
	// Cette méthode créée les labels


	public String setLabel ( int numLbl )
	{
		String sRet;

		switch(numLbl)
		{
			case 0  -> sRet =   "";
			case 1  -> sRet =   "";
			case 2  -> sRet =   "";
			default -> sRet = null;
		}

		return sRet;
	}


	/* --------------------------------------------------------------------- */
	// Initialisation des différents valeurs nécéssaires au bon fonctionnement de la fenêtre.


	public int     setNbLigne         () { return this.metier.getNbLigne();   }
	public int     setNbColonne       () { return this.metier.getNbColonne(); }
	public boolean setNumLigneColonne () { return false;                      }
	public int     setLargeurImg      () { return  48;                        }
	public int     setLargeurLabel    () { return 260;                        }
	public int     setMargeHaute      () { return -20;                        }
	public int     setMargeGauche     () { return -20;                        }
	public int     setMargeImageBtn   () { return   2;                        }
	public int     setMargeBtnLabel   () { return   2;                        }


	/* --------------------------------------------------------------------- */
	// Cette méthode redistribue les touches a différentes actions


	public void jouer ( String touche )
	{

		if( touche.equals("I")    )   this.metier.deplacer('N');
		if( touche.equals("K")    )   this.metier.deplacer('S');
		if( touche.equals("J")    )   this.metier.deplacer('O');
		if( touche.equals("L")    )   this.metier.deplacer('E');

		this.frame.majIHM();
	}


	/* --------------------------------------------------------------------- */
	// Cette méthode permet d'effectuer certaines action selon le bouton
	// préssé


	public void bouton ( int action )
	{

		switch( action )
		{
			case 0 -> this.metier.defiPrecedent()         ;
			case 1 -> this.metier.defiSuivant()           ;
			case 2 -> this.metier.recommencer()           ;
		}

		this.frame.majIHM();
	}


	/* --------------------------------------------------------------------- */
	// Cette méthode est appellée en boucle pour réinitialiser les labels.


	public String setTextLabel ( int numLbl )
	{
		String sRet;

		switch ( numLbl )
		{
			case 0  -> sRet = "nb Pas:        " + this.metier.getNbPas()  ;
			case 1  -> sRet = "Message :      " + this.metier.getMessage();
			default -> sRet = null;
		}

		return sRet;
	}


	/* --------------------------------------------------------------------- */
	// Cette méthode permet de mettre en place toutes les imagettes qui
	// Constituent les éléments du décor


	public String setImage ( int ligne, int colonne, int couche )
	{
		String sPath   = "../images/" + this.sTheme;
		String sRetImg = ""                        ;

		char   cSymbole;

		cSymbole = this.metier.getSymbole(ligne, colonne);

		if( couche == 0 )
		{
			switch( cSymbole )
			{
				case '#' -> sRetImg = sPath + "/mur.png"      ;
				case '$' -> sRetImg = sPath + "/objet.png"     ;
				case '*' -> sRetImg = sPath + "/objet_rangement.png"     ;
				case '.' -> sRetImg = sPath + "/rangement.png";
				case ' ' -> sRetImg = sPath + "/vide.png"     ;
				case '+' -> sRetImg = sPath + "/pousseur.png";
				case '@' -> sRetImg = sPath + "/pousseur.png"     ;
			}
		}

		return sRetImg;
	}


	public static void main( String[] a )
	{
		new Controleur();
	}
}