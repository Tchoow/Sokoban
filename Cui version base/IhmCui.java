import iut.algo.Console;
import iut.algo.CouleurConsole;


public class IhmCui
{
	private Controleur ctrl;

	public IhmCui(Controleur ctrl)
	{
		this.ctrl = ctrl;
	}

	public char getChoix()
	{
		char car;

		Console.println ();

		Console.print ( "  Action : " );

		car = Console.lireChar();

		return car;
	}


	public void afficher()
	{
		int  lig, col;
		char cara;

		Console.normal();
		//Console.effacerEcran();
		for ( int cpt=0;cpt<20;cpt++)
			Console.println ( "                                                " );
		//Console.effacerEcran();

		Console.println ( this.ctrl.getMessage() );
		Console.println ( "Pas : " + this.ctrl.getNbPas() );

		for ( lig=0; lig < this.ctrl.getNbLigne() ; lig++ )
		{
			for ( col=0; col < this.ctrl.getNbColonne() ; col++ )
			{
				cara = this.ctrl.getCase ( lig, col );

				switch ( cara )
				{
					case '#' ->
					{
						Console.couleurFond ( CouleurConsole.MAUVE );
						Console.print ( ' ' );
						Console.couleurFond ( CouleurConsole.NOIR  );
					}

					case ' ', 'x' ->
					{
						Console.print ( ' ' );
					}

					case 'o' ->
					{
						Console.couleurFond ( CouleurConsole.JAUNE );
						Console.print ( ' ' );
						Console.couleurFond ( CouleurConsole.NOIR  );
					}

					case 'O' ->
					{
						Console.couleurFond ( CouleurConsole.JAUNE );
						Console.print ( '.' );
						Console.couleurFond ( CouleurConsole.NOIR  );
					}

					case 'P' ->
					{
						Console.couleurFont ( CouleurConsole.JAUNE );
						Console.print ( 'P' );
						Console.couleurFont ( CouleurConsole.BLANC );
					}

					case '.' ->
					{
						Console.couleurFont ( CouleurConsole.VERT );
						Console.print ( '.' );
						Console.couleurFont ( CouleurConsole.BLANC );
					}
				}
			}

			Console.println();
		}

		Console.normal();

	}
}