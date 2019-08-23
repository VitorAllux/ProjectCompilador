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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SingleSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
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
import java.util.Stack;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

	private JPanel contentPane;
	public JTextArea textArea = new JTextArea();
	private JInternalFrame internalFrame1;
	private String text;
	private JButton btnSearch, btnRun, btnDebug;
	private Stack<Token> tokens = new Stack<Token>();
	private JLayeredPane layer1;
	
	//tabela e modelo
	private JTable table;
	private DefaultTableModel model;
	private JTable tabela;




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
		setBounds(100, 100, 1050, 581);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);




		
		this.addComponentListener(new ComponentAdapter() {
			   @Override
			    public void componentMoved(ComponentEvent e) {  
				   internalFrame1.setMaximizable(false);  
				   internalFrame1.setResizable(false);
				  try {
					internalFrame1.setMaximum(true);
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    }
			});
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
		
				
			}
		});
		
		/**
		 * Create table;
		 */

		String colunas[] = { "Codigo" , "Linha", "Simbolo" };

		model = new DefaultTableModel(null, colunas) {
			public boolean isCellEditable(int row,int column) {
				return false;				
			}
		};
		table = new JTable();
		table.setModel(model);
 		table.setBorder(BorderFactory.createLineBorder(Color.black));
 		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
 		table.getTableHeader().setEnabled(true);
 		table.setBounds(800,150,235,395);;
		table.setVisible(true);
		contentPane.add(table);
		
		//Scroll Pane
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(800,150,235,395);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.getContentPane().add(scrollPane);
			
				
		/**
 		 * Create the buttons.
 		 */
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1100, 27);
		
		contentPane.add(menuBar);
		
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
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
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!textArea.getText().isEmpty()) {
					model.setRowCount(0);
				Automatos automato = new Automatos();
				tokens = automato.splitSimbols(getTextArea());
				for(Token token : tokens) {
						model.addRow(new String[] {Integer.toString(token.getCodigo()),Integer.toString(token.getLinha()),token.getSimbolo()});
					}
				}
				else
					JOptionPane.showMessageDialog(null, "Campo de texto vazio");
			}
			
		});
		
		menuBar.add(btnSearch);
		menuBar.add(btnRun);
		
		
		
		/**
		 * Create the layer.
		 */
		layer1 = new JLayeredPane();
		layer1.setBounds(0, 30, 789, 354);
		
		/**
		 * Create the editor de texto.
		 */
        JScrollPane painelComBarraDeRolagem = new JScrollPane(textArea);
        TextLineNumber contadorLinhas = new TextLineNumber(textArea);
        painelComBarraDeRolagem.setRowHeaderView(contadorLinhas);
        
    	internalFrame1 = new JInternalFrame("New JInternalFrame");
        internalFrame1.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        internalFrame1.add(BorderLayout.CENTER, painelComBarraDeRolagem);
		internalFrame1.setBounds(0, 25, 789, 354);
		layer1.add(internalFrame1);
		internalFrame1.setVisible(true);
		contentPane.add(layer1);
	}
		
		/**
		 * Create the gettextarea.
		 */
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
