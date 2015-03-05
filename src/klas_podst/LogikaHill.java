package klas_podst;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


import Jama.Matrix;


public class LogikaHill{

	private static String znaki = "abcdefghijklmnopqrstuvwxyz .,:;!?()-+*/[]{}@_><#~=\\\"&%$§0123456789";
	
	public static void szyfruj(File plik,int dlBloku,String klucz)throws Exception{
		
		ArrayList<Character> alfabet = new ArrayList<Character>();       //aflabet w kolekcji
		for(char c: znaki.toCharArray()){
			alfabet.add(c);
		}																//
		
		int indKlucz = 0;												//tworzenie macierzy na podstawie klucza
		
		double[][] kTab = new double[dlBloku][dlBloku];
		
		for(int i=0;i<dlBloku;i++){
			for(int j=0;j<dlBloku;j++){
				kTab[i][j] = alfabet.indexOf(klucz.charAt(indKlucz));
				indKlucz++;
			}
		}
	
		Matrix kMac = new Matrix(kTab);									//
		
		Matrix kMacOdw = new Matrix(2,2);										//szukanie macierzy odwrotnej
		
		
		if(dlBloku==2){									
		
				Matrix kMacAdj = new Matrix(2,2);
				kMacAdj.set(0, 0, kTab[1][1]);
				kMacAdj.set(0, 1, -kTab[0][1]+znaki.length());
				kMacAdj.set(1, 0, -kTab[1][0]+znaki.length());
				kMacAdj.set(1, 1, kTab[0][0]);
				
				
				
				int d = new Double(kMac.det()%znaki.length()).intValue();
				int ind = 1;
				int d1 = 0;
				
				boolean znaleziono = false;
				while(ind<znaki.length() && !znaleziono){
					if((d*ind)%znaki.length()==1){
						d1=ind;
						znaleziono=true;
					}
					
					ind++;
				}
			
	
				if (!znaleziono){
					throw new Exception("Podany klucz jest nieodpowiedni. Macierz nie jest odwracalna.");
				}
				
				 kMacOdw = kMacAdj.times(d1);											//wyliczanie macierzy odwrotnej
				 																		//
				for(int i=0;i<dlBloku;i++){												//macierz odwrotna modulo 66
					for(int j=0;j<dlBloku;j++){
						kMacOdw.set(i, j, kMacOdw.get(i, j)%znaki.length());	
					}																	//
				}
				
		}else{
			throw new Exception("Macierz inna ni¿ 2x2 nie jest obs³ugiwana");
		}																		//
		
		
		
		BufferedReader br = null;
		PrintWriter pw = null;
		
		try 
		{
			br = new BufferedReader(new FileReader(plik));
			pw = new PrintWriter("D:\\Dokumenty\\studia\\VI semestr\\Bezpieczeñstwo i ochrona danych\\Laboratoria\\1 - Hill\\zaszyfrowanyHill.txt", "UTF-8");
			
			String aktLinia=br.readLine();
			
			
			while (aktLinia != null) 											//dopoki dojdziemy do konca pliku
			{
				int index=0;
				char[][] blok = new char[dlBloku][1];								
				double[][] blokLiczby = new double[dlBloku][1];
				char[][] blokWynik = new char[dlBloku][1];
				
				while(index+dlBloku-1<aktLinia.length()){								//dopoki ze znakow tekstu mozna utoworzyc bloki o podanej dlugosci
					
					for(int i=0;i<dlBloku;i++){												//wpisujemy blok do wektora
					blok[i][0]=aktLinia.charAt(index+i);
					System.out.println("znak"+i+": "+blok[i][0]);
					}																		//
					
					for(int i=0;i<dlBloku;i++){							//zamieniamy blok znakow na blok liczb
						if (alfabet.indexOf(blok[i][0])<0 || alfabet.indexOf(blok[i][0])>znaki.length()-1){
							throw new Exception("W tekscie wystepuja znaki nienalezace do alfbetu");
						}else{
							blokLiczby[i][0]=alfabet.indexOf(blok[i][0]);
						}
					}																		//
					
					Matrix bMac = new Matrix(blokLiczby);
					
					Matrix wynMac = kMac.times(bMac);										// wyznaczamy macierz wynikowa do zapisu w pliku
																							//
					for(int i=0;i<dlBloku;i++){
						wynMac.set(i, 0, wynMac.get(i, 0)%znaki.length());					// macierz modulo dlugosc alfabetu
						System.out.println("ind: "+(int)wynMac.get(i, 0));
						blokWynik[i][0]=znaki.charAt((int)wynMac.get(i, 0));				// zamiana macierzy liczbowej na znakowa
						pw.print(blokWynik[i][0]);											//zapis znakow z macierzy do pliku
					}		
				
					
					index=index+dlBloku;
				 }																		//
				
																								
					for(int i=0;i<dlBloku;i++){											//tworzymy ostatni blok ze znakow ktore pozostaly i dopelniamy spacjami.
						
						if(index+i<aktLinia.length())
						{
							blok[i][0]=aktLinia.charAt(index+i);
						}else
						{
							blok[i][0]=' ';
						}
		
					}																	//
					
					for(int i=0;i<dlBloku;i++){											//zamieniamy blok znakow na blok liczb
						blokLiczby[i][0]=alfabet.indexOf(blok[i][0]);
						}																//
					
					Matrix bMac = new Matrix(blokLiczby);
					
					Matrix wynMac = kMac.times(bMac);									// wyznaczamy macierz wynikowa do zapisu w pliku
																						//
					
					for(int i=0;i<dlBloku;i++){
						wynMac.set(i, 0, wynMac.get(i, 0)%znaki.length());				// macierz modulo dlugosc alfabetu
						blokWynik[i][0]=znaki.charAt((int)wynMac.get(i, 0));			// zamiana macierzy liczbowej na znakowa
						pw.print(blokWynik[i][0]);										//zapis znakow z macierzy do pliku
					}
					
				
				
				
				aktLinia=br.readLine();
				pw.println();
				
			}																	//
 
		} 
		catch (IOException e)
		{
			throw new IOException("B³¹d odczytu pliku");
		} 
		finally 
		{
			try 
			{
				if (br != null) br.close();
				if (pw != null) pw.close();
			} 
			catch (IOException ex) 
			{
				throw new IOException("B³¹d odczytu pliku");
			}
		}
	
	}
	
	
public static void deszyfruj(File plik,int dlBloku,String klucz) throws Exception{
		
		ArrayList<Character> alfabet = new ArrayList<Character>(); //aflabet w kolekcji
		for(char c: znaki.toCharArray()){
			alfabet.add(c);
		}														   //
		
		int indKlucz = 0;										//tworzenie macierzy na podstawie klucza
		
		double[][] kTab = new double[dlBloku][dlBloku];			
		
		for(int i=0;i<dlBloku;i++){
			for(int j=0;j<dlBloku;j++){
				kTab[i][j] = alfabet.indexOf(klucz.charAt(indKlucz));
				indKlucz++;
			}
		}
		
		Matrix kMac = new Matrix(kTab);							//
		Matrix kMacOdw = new Matrix(2,2);
		
		
		if(dlBloku==2){												//szukanie macierzy odwrotnej
		
				Matrix kMacAdj = new Matrix(2,2);
				kMacAdj.set(0, 0, kTab[1][1]);
				kMacAdj.set(0, 1, -kTab[0][1]+znaki.length());
				kMacAdj.set(1, 0, -kTab[1][0]+znaki.length());
				kMacAdj.set(1, 1, kTab[0][0]);
				
				
				int d = new Double(kMac.det()%znaki.length()).intValue();
				int ind = 1;
				int d1 = 0;
				
				boolean znaleziono = false;
				while(ind<znaki.length() && !znaleziono){
					if((d*ind)%znaki.length()==1){
						d1=ind;
						znaleziono=true;
					}
					
					ind++;
				}
			
	
				if (!znaleziono){
					throw new Exception("Podany klucz jest nieodpowiedni. Macierz nie jest odwracalna.");
				}
				
				 kMacOdw = kMacAdj.times(d1);								//wyliczanie macierzy odwrotnej
				
				for(int i=0;i<dlBloku;i++){									//macierz odwrotna modulo dlugosc alfabetu
					for(int j=0;j<dlBloku;j++){
						kMacOdw.set(i, j, kMacOdw.get(i, j)%znaki.length());
					}
				}
				
		}else{	
			throw new Exception("Macierz inna ni¿ 2x2 nie jest obs³ugiwana");
		}															//
		
		BufferedReader br = null;
		PrintWriter pw = null;
		
		try 
		{
			br = new BufferedReader(new FileReader(plik));
			
			pw = new PrintWriter("D:\\Dokumenty\\studia\\VI semestr\\Bezpieczeñstwo i ochrona danych\\Laboratoria\\1 - Hill\\zdeszyfrowanyHill.txt", "UTF-8");
			
			String aktLinia=br.readLine();
			
			
			while (aktLinia != null) 
			{
				int index=0;
				char[][] blok = new char[dlBloku][1];
				double[][] blokLiczby = new double[dlBloku][1];
				char[][] blokWynik = new char[dlBloku][1];
				
				while(index+dlBloku-1<aktLinia.length()){
					
					for(int i=0;i<dlBloku;i++){
					blok[i][0]=aktLinia.charAt(index+i);
					}
					
					for(int i=0;i<dlBloku;i++){
					blokLiczby[i][0]=alfabet.indexOf(blok[i][0]);
					}
					
					Matrix bMac = new Matrix(blokLiczby);
					
					Matrix wynMac = kMacOdw.times(bMac);
					
					
					for(int i=0;i<dlBloku;i++){
						wynMac.set(i, 0, wynMac.get(i, 0)%znaki.length());
						blokWynik[i][0]=znaki.charAt((int)wynMac.get(i, 0));
						pw.print(blokWynik[i][0]);
					}
				
					
					index=index+dlBloku;
				 }
				
				aktLinia=br.readLine();
				pw.println();
				
			}
 
		} 
		catch (IOException e)
		{
			throw new IOException("B³¹d odczytu pliku");
		} 
		finally 
		{
			try 
			{
				if (br != null) br.close();
				if (pw != null) pw.close();
			} 
			catch (IOException ex) 
			{
				throw new IOException("B³¹d odczytu pliku");
			}
		}
		
	}


}
