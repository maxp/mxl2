/*
 * Rus.java
 */

package mxl2;

import java.util.Date;

public final class Rus
{

	/** month names [0-11]: {abb,normal,date} */
	public static final String[][] MONTH = new String[][]
	{
		{ "янв", "январь",	"января"  },
		{ "фев", "февраль",	"февраля" },
		{ "мар", "март",	"марта"   },
		{ "апр", "апрель",	"апреля"  },
		{ "май", "май",		"мая"     },
		{ "июн", "июнь",	"июня"    },
		{ "июл", "июль",	"июля"    },
		{ "авг", "август",	"августа" },
		{ "сен", "сентябрь","сентября"},
		{ "окт", "октябрь",	"октября" },
		{ "ноя", "ноябрь",	"ноября"  },
		{ "дек", "декабрь",	"декабря" },
	};
	
	
	/** weekdays [0-8]: {short2,short3,short4,normal} */
	public static final String[][] WEEKDAY = new String[][]
	{
		{ "вс", "вск", "воскр", "воскресенье" },
		{ "пн", "пнд", "понед", "понедельник" },
		{ "вт", "втр", "вторн", "вторник"     },
		{ "ср", "срд", "среда", "среда"       },
		{ "чт", "чтв", "четв" , "четверг"     },
		{ "пт", "птн", "пятн" , "пятница"     },
		{ "сб", "сбт", "субб" , "суббота"     },
		{ "вс", "вск", "воскр", "воскресенье" },
	};
	
	
	private static final String[] num20 = new String[] 
	{
		"", "один ", "два ", "три ", "четыре ", "пять ", "шесть ", "семь ", "восемь ", "девять ", 
		"десять ", "одиннадцать ", "двенадцать ", "тринадцать ", "четырнадцать ", "пятнадцать ", 
		"шестнадцать ", "семнадцать ", "восемнадцать ", "девятнадцать ",
		"", "одна ", "две "
	};
	
	private static final String[] decs = new String[] 
	{
		"", "", "двадцать ", "тридцать ", "сорок ", "пятьдесят ",
		"шестьдесят ", "семьдесят ", "восемьдесят ", "девяносто "
	};

	private static final String[] hund = new String[] 
	{
		"", "сто ", "двести ", "триста ", "четыреста ", "пятьсот ", 
		"шестьсот ", "семьсот ", "восемьсот ", "девятьсот "
	};

	private static final String[][] mill = new String[][] 
	{
		{ "триллион ", "триллиона ", "триллионов " },
		{ "миллиард ", "миллиарда ", "миллиардов " },
		{ "миллион ", "миллиона ", "миллионов " },
		{ "тысяча ", "тысячи ", "тысяч " },
		{ "рубль", "рубля", "рублей" }
	};
	
	
	/** write rubles in russian */
	public static String writeRub( long sum ) 
	{
		if( sum <= 0 ) return "Ноль рублей";
		
		long div = 1000000000000L;
		if( sum >= div*1000 ) return Long.toString( sum )+" рублей";
			
		StringBuffer res = new StringBuffer();
		for( int i = 0; i < 5; i++ ) {
			int tri = (int)( (sum / div) % 1000 );

			if( tri > 0 ) {
				res.append( hund[tri / 100] );
			
				int d = tri % 100;
				if( d >= 20 ) {	res.append( decs[ d / 10 ] ); d %= 10; }
			
				int ofs = ( i == 3 && ( d == 1 || d == 2 ) )? 20: 0;
				res.append( num20[ d+ofs ] );
			
				int v = ( d == 1 )? 0 : ( d >= 2 && d <= 4 )? 1 : 2;
				res.append( mill[i][v] );
			}
			else if( i == 4 ) {
				res.append( mill[4][2] );
			}
		
			div /= 1000;
		}
		
		res.setCharAt( 0, Character.toUpperCase( res.charAt( 0 ) ) );
		return res.toString();
	}
	

	/** format date russian Mon. D */
	public static String date_md( long dt )
	{
		if( dt <= 0 ) return "???. ??";
		
		int[] dm = Lib.int_dmy( dt );
		return Lib.firstCap( MONTH[dm[1]][0] )+". "+dm[0];
	}
	
	/** format date russian Mon. D */
	public static String date_md( Date dt )	{
		return date_md( (dt == null)? 0 : dt.getTime() );
	}

	/** format date russian d_Mmm */
	public static String date_dMmm( long dt )
	{
		if( dt <= 0 ) return "??_???";
		
		int[] dm = Lib.int_dmy( dt );
		return dm[0]+" "+Lib.firstCap( MONTH[dm[1]][0] );
	}
	
	public static String date_dMmm( Date dt ) {
		return date_dMmm( (dt == null)? 0 : dt.getTime() );
	}

	/** format date russian d_mmm */
	public static String date_dmmm( long dt )
	{
		if( dt <= 0 ) return "??_???";
		
		int[] dm = Lib.int_dmy( dt );
		return dm[0]+" "+MONTH[dm[1]][0];
	}
	
	public static String date_dmmm( Date dt ) {
		return date_dmmm( (dt == null)? 0 : dt.getTime() );
	}
	
	

	/** format date russian D Month Year */
	public static String date_dmy( long dt )
	{
		if( dt <= 0 ) return "?? ?????? ????";
		
		int[] dm = Lib.int_dmy( dt );
		return dm[0]+" "+MONTH[dm[1]][2]+" "+dm[2];
	}
	
	/** format date russian D Month Year */
	public static String date_dmy( Date dt )
	{
		return date_dmy( (dt == null)? 0 : dt.getTime() );
	}
	
	/**
	 * @param n - number
	 * @param triplet: [1-час, 234-часа, 5-часов]
	 * @return correct plural form for n
	 */
	public static String plural( int n, String[] triplet )
	{
	    if( n < 0 ) n = -n;
	    if( 11 <= n && n <= 14 ) return triplet[2];
	    n = n % 10;
	    if( 2 <= n && n <= 4 ) return triplet[1];
	    if( n == 1 ) return triplet[0];
	    return triplet[2];
	}
	
}

//.
