package klas_podst;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class PrezentacjaHill extends JFrame {

	private JPanel contentPane;
	private JTextField kluczTFiled;
	private JLabel lblPlik;
	private JButton btnOtworzPlik;
	private JFileChooser wybPlik;
	private File plik;
	private int dlugoscBl = 2;
	private JLabel wiadLabel;
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new PrezentacjaHill();
			}
		});
	}
	
	
	
	public PrezentacjaHill() {
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\Dokumenty\\Eclipse\\BiOD_Lab\\src\\icon.png"));
		setTitle("Hill");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 433, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnSzyfruj = new JButton("Szyfruj");
		btnSzyfruj.setBounds(100, 228, 89, 23);
		contentPane.add(btnSzyfruj);
		
		btnSzyfruj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
				LogikaHill.szyfruj(plik,dlugoscBl,kluczTFiled.getText());
				wiadLabel.setText("Pomyœlnie zaszyfrowano");
				}catch(IOException ex1){
					wiadLabel.setText(ex1.getMessage());
					ex1.printStackTrace();
				}catch(Exception ex2){
					wiadLabel.setText(ex2.getMessage());
					ex2.printStackTrace();
				}
			}
		});
		
		JButton btnDeszyfruj = new JButton("Deszyfruj");
		btnDeszyfruj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
				LogikaHill.deszyfruj(plik,2,kluczTFiled.getText());
				wiadLabel.setText("Pomyœlnie zdeszyfrowano");
				}catch(IOException ex1){
					wiadLabel.setText(ex1.getMessage());
					ex1.printStackTrace();
				}catch(Exception ex2){
					wiadLabel.setText(ex2.getMessage());
					ex2.printStackTrace();
				}
			}
		});
		btnDeszyfruj.setBounds(256, 228, 89, 23);
		contentPane.add(btnDeszyfruj);
		
		kluczTFiled = new JTextField();
		kluczTFiled.setBounds(256, 147, 86, 20);
		contentPane.add(kluczTFiled);
		kluczTFiled.setColumns(10);
		
		JLabel kluczLbl = new JLabel("Klucz");
		kluczLbl.setBounds(143, 150, 46, 14);
		contentPane.add(kluczLbl);
		
		JButton btnOtworzPlik = new JButton("Otw\u00F3rz plik");
		btnOtworzPlik.setBounds(256, 63, 89, 23);
		contentPane.add(btnOtworzPlik);
			
		btnOtworzPlik.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  wybPlik = new JFileChooser("D:\\Dokumenty\\studia\\VI semestr\\Bezpieczeñstwo i ochrona danych\\Laboratoria\\1 - Hill");
			        wybPlik.setAcceptAllFileFilterUsed(false);
			        wybPlik.setMultiSelectionEnabled(false);
			        FileNameExtensionFilter fnef = new FileNameExtensionFilter(".txt", "txt");
			        wybPlik.setFileFilter(fnef);
			        int result = wybPlik.showOpenDialog(null);
			       
			        if (result == JFileChooser.APPROVE_OPTION) 
			        {
			        	lblPlik.setText(wybPlik.getSelectedFile().getName());
			        	plik = wybPlik.getSelectedFile();
			        } 
			        else if (result == JFileChooser.CANCEL_OPTION) 
			        {
			        	lblPlik.setText("...");
			        }
			  }
			});
		
		lblPlik = new JLabel("...");
		lblPlik.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPlik.setBounds(34, 65, 155, 18);
		contentPane.add(lblPlik);
		
		wiadLabel = new JLabel("");
		wiadLabel.setBounds(34, 191, 373, 14);
		contentPane.add(wiadLabel);
		
		setVisible(true);
	}
}
