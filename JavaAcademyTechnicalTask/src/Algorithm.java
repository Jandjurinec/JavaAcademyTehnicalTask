import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * Algoritam za odreðivanje pojavljivanja slova unutar zadanog teksta. <br>
 * Izveden kao klasa kako bi se mogao kasnije lakše implementirati u kodu.
 * 
 * @author Jan Ðurinec
 *
 */

public class Algorithm{
	/**
	 * Set u koji se spremaju slova nad kojima se vrši algoritam
	 * 
	 */
    private static Set<Character> setOfChars;
    /**
     * Ulazni string
     * 
     */
    private static String line;
    /**
     * Mapa koja sadrži Set slova koja se nalaze u pojedinoj rijeèi i u setOfChars setu
     * 
     */
    private static Map<Map<Set<Character>, Double>, Double> finalFinalMap;
    private static double totalLetterCount;
    private static double ourCharsNumTotal;
    static Scanner sc = new Scanner(System.in);

	@SuppressWarnings("static-access")
	public Algorithm(char[] chars, String line)
    {
    	this.setOfChars = new LinkedHashSet<Character>();
    	for(var c:chars) setOfChars.add(Character.toLowerCase(c)); //napuniti novonastali set sa charovima
    	this.line = line;
    	this.totalLetterCount=0;
    	this.ourCharsNumTotal=0;
    	this.finalFinalMap = getResult();
    	
    }
    
    //konstruktor bez parametara, kada korisnik želi sam unijeti slova i tekst nad kojim se vrši provjera
    @SuppressWarnings("static-access")
    public Algorithm()
    {
    	this.line= new String();
		this.setOfChars = inputChars();
		this.totalLetterCount=0;
    	this.ourCharsNumTotal=0;
    	this.finalFinalMap = getResult();

    }
    
    @SuppressWarnings("static-access")
    public Algorithm(String line)
    {
    	this.setOfChars = inputChars();
    	this.line=line;
    	this.totalLetterCount=0;
    	this.ourCharsNumTotal=0;
    	this.finalFinalMap = getResult();
    	
    }
    /**
     * funkcija za dobivanje setOfChars i line (ukoliko nisu prenesene u konstruktor)
     * 
     * @return LinkedHashSet(Character)
     */
    private static LinkedHashSet<Character> inputChars() 
    { 	
    	var setOfChars2 = new LinkedHashSet<Character>();
    	
        String line = new String();

        System.out.println("Enter different letters to examine or press enter to continue: ");

        while(sc.hasNextLine() && !(line=sc.nextLine()).isEmpty())
        {
            
                Character chr = line.toLowerCase().charAt(0);
                if(line.length()!=1 || !Character.isAlphabetic(chr))
                {
                    System.out.println("Please enter only one letter!");
                }
                else if(setOfChars2.contains(chr))
                {
                    System.out.println(chr + " is allready entered!");
                }
                else
                {
                	setOfChars2.add(chr);
                	System.out.println("Enter a letter or press enter to continue: ");
                }    
        }
        
        System.out.println("All letters:");
        setOfChars2.forEach((a)-> System.out.print(a + " "));
        System.out.println();
        if(getLine().isBlank())
        {
        	System.out.println("Enter the phrase or text you want to examine: ");
        	setLine(sc.nextLine());
        }
        sc.close();
        
        return setOfChars2;
    }

    /**
     * 
     * Funkcija za formatirano ispisivanje statistike koliko se puta neki niz.<br>
     * zadanih znakova pojavio u tekstu<br>
     * Po defaultu se izvodi nakon funkcije getResult()
     * 
     */
    public static void ispis()
    {
    	 for(var m : finalFinalMap.entrySet())
           {
           	for(var n : m.getKey().entrySet())
           	{
           		System.out.print("{" + n.getKey() + ", " + n.getValue().intValue() + "} = ");
           		System.out.printf("%.2f",(m.getValue()/getOurCharsNumTotal()));
           		System.out.println(" (" + m.getValue().intValue() + "/" + (int)getOurCharsNumTotal()+ ")");
           	}
           }
           System.out.print("TOTAL Frequency: ");
           System.out.printf("%.2f", (getOurCharsNumTotal())/(getTotalLetterCount()));
           System.out.println(" (" + (int)getOurCharsNumTotal()+"/"+ (int)getTotalLetterCount()+")");
    }
    
    
    /**
     * <b>Pravi 'algoritam'</b> koji zapravo pronalazi pojavljivanje znakova unutar teksta.<br>
     * 
     * <b>Set&ltCharacter></b> bilježi sva pojavljivanja setOfChars znakova u pojedinoj rijeèi<br>
     * <b>Double unutar prve Mape</b> bilježi velièinu rijeèi unutar koje se bilježio set<br>
     * <b>Double unutar druge mape</b> bilježi koliko se puta u cijelom tekstu pojavio toèno taj par
     * 
     * @return Map&ltMap&ltSet<Character>, Double>, Double>
     */
    private static Map<Map<Set<Character>, Double>, Double> getResult()
    {
        finalFinalMap = new LinkedHashMap<Map<Set<Character>,Double>, Double>();
        
        line.replaceAll("[^a-zA-Z0-9]"," ");
        
        for(var inptline : line.split("[^a-zA-Z0-9]")) //regex koji ce 'razbiti' string na sve što nije slovo ili broj
        {	
        	
        	var finalMap = new LinkedHashMap<Set<Character>, Double>();
        	var helper = new LinkedHashSet<Character>();
        	double lettersInLine=0;
        	double ourCharsNum=0;
        	
        	for(var ch : inptline.toCharArray())
        	{
        		
        		if(!Character.isLetter(ch)) continue;
        		totalLetterCount = getTotalLetterCount() + 1;
        		lettersInLine++;
        		ch=Character.toLowerCase(ch);
        		
        		if(setOfChars.contains(ch))
        		{
        			ourCharsNumTotal = getOurCharsNumTotal() + 1;
        			ourCharsNum++;
        			helper.add(ch);
        		}
        	}
        	
        	finalMap.put(helper, lettersInLine);
        	
        	if(finalFinalMap.containsKey(finalMap))
        	{
        		var previousNum = finalFinalMap.entrySet().stream()
        				.filter(n-> n.getKey().equals(finalMap))
        				.mapToDouble(Map.Entry::getValue)
        				.findFirst()
        				.getAsDouble();
        		finalFinalMap.put(finalMap, previousNum + ourCharsNum);
        	}
        	finalFinalMap.putIfAbsent(finalMap, ourCharsNum);
        }
        
        return sortByValue(finalFinalMap);
    }
    
    /**
     * Funkcija koja sortira mapu po value atributu.
     * 
     * 
     * @param <K> Key
     * @param <V> Value
     * 
     * @return &ltK, V> Map&ltK, V> 
     */
    private static <K, V> Map<K, V> sortByValue(Map<K, V> map)
    {
        List<Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Object>()
        {
            @SuppressWarnings("unchecked")
            public int compare(Object o1, Object o2)
            {
                return ((Comparable<V>) ((Map.Entry<K, V>) (o1)).getValue()).compareTo(((Map.Entry<K, V>) (o2)).getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Iterator<Entry<K, V>> it = list.iterator(); it.hasNext();)
        {
            Map.Entry<K, V> entry = (Map.Entry<K, V>) it.next();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

	public static double getTotalLetterCount() {
		return totalLetterCount;
	}

	public static double getOurCharsNumTotal() {
		return ourCharsNumTotal;
	}
    public static String getLine() {
		return line;
	}

	public static void setLine(String line) {
		Algorithm.line = line;
	}
}

