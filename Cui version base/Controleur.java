public class Controleur
{

	private IhmCui ihm;
	private Grille metier;

	public Controleur()
	{
		this.metier = new Grille ();
		this.ihm    = new IhmCui ( this );

		this.lancerJeu();
	}


	private void lancerJeu()
	{
		char action;

		this.ihm.afficher();

		while ( true )
		{
			action = this.ihm.getChoix();

			switch ( action )
			{
				case 'i' -> this.metier.deplacer     ( 'N' );
				case 'j' -> this.metier.deplacer     ( 'O' );
				case 'k' -> this.metier.deplacer     ( 'S' );
				case 'l' -> this.metier.deplacer     ( 'E' );
				case '+' -> this.metier.defiSuivant  ();
				case '-' -> this.metier.defiPrecedent();
				case 'R' -> this.metier.recommencer  ();
			}

			this.ihm.afficher();
		}
	}

	public String getMessage  (){ return this.metier.getMessage(); }
	public int    getNbPas    (){ return this.metier.getNbPas  (); }

	public int    getNbLigne  (){ return this.metier.getNbLigne  (); }
	public int    getNbColonne(){ return this.metier.getNbColonne(); }

	public char   getCase  (int lig, int col)
	{
		char symb, cRet;

		symb = this.metier.getSymbole (lig, col);

		switch ( symb )
		{
			case '#'      -> cRet = '#';
			case '.'      -> cRet = '.';
			case '$'      -> cRet = 'o';
			case '*'      -> cRet = 'O';
			case '@', '+' -> cRet = 'P';
			default       -> cRet = ' ';
		}

		return cRet;
	}


	public static void main(String[] a)
	{
		new Controleur();
	}

}