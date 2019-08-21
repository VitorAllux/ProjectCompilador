package AnalisadorLexico;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

	private JPanel contentPane;
	public JTextArea textArea = new JTextArea();
	private JInternalFrame internalFrame = new JInternalFrame("New JInternalFrame");
	private String text;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 581);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.addComponentListener(new ComponentAdapter() {
			   @Override
			    public void componentMoved(ComponentEvent e) {  
				   internalFrame.setMaximizable(false);  
				   internalFrame.setResizable(false);
				  try {
					internalFrame.setMaximum(true);
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    }
			});
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1100, 27);
		
		contentPane.add(menuBar);
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setCurrentDirectory(new File("Documentos"));
				chooser.showOpenDialog(null);

					try {
						text = new FileManipulator().fileRead(chooser.getSelectedFile().getPath());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				
					textArea.setText(text);
				
			}
		});
		menuBar.add(btnNewButton);
		
		//Layer
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 30, 789, 354);
		
		//editor de texto
        JScrollPane painelComBarraDeRolagem = new JScrollPane(textArea);
        TextLineNumber contadorLinhas = new TextLineNumber(textArea);
        painelComBarraDeRolagem.setRowHeaderView(contadorLinhas);
        
        internalFrame.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        internalFrame.add(BorderLayout.CENTER, painelComBarraDeRolagem);
		internalFrame.setBounds(0, 25, 789, 354);
		layeredPane.add(internalFrame);
		internalFrame.setVisible(true);
		contentPane.add(layeredPane);
	}
		
		//Get text area
		public ArrayList<String> getTextArea() {
			ArrayList<String> lista = new ArrayList<>();
			String texto = textArea.getText();
			String[] aux;
			aux = texto.split("\n");
			for(int i=0; i<aux.length; i++) {
				lista.add(aux[i]);
				System.out.println(lista.get(i));
			}
			return lista;
		}
}
