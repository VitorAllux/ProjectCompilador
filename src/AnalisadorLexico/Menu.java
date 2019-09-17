package AnalisadorLexico;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ReplicateScaleFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.print.attribute.AttributeSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Menu extends JFrame {

	private JPanel contentPane;
	public ColorPane editor, console;

	public JScrollPane scrollPane1, scrollPane2, scrollPane3;
	private String text;
	private JButton btnSearch, btnRun, btnSave, btnNew, btnBuild;
	private Stack<Token> tokens = new Stack<Token>();
	private Menu menu = this;
	public String aux = new String();

	// tabela e modelo
	private JTable table;
	private DefaultTableModel model;
	private JTable tabela;

	
	private Automato automato;
	// main

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

	// Menu

	public Menu() {
		automato = new Automato(menu);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 581);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// debug table

		String colunas[] = { "Codigo", "Linha", "Simbolo" };

		model = new DefaultTableModel(null, colunas) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable();
		table.setModel(model);
		table.setBorder(BorderFactory.createLineBorder(Color.black));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setEnabled(true);
		table.setBounds(800, 150, 235, 395);
		;
		table.setVisible(true);
		contentPane.add(table);

		// Scroll Pane
		scrollPane1 = new JScrollPane(table);
		scrollPane1.setBounds(800, 150, 235, 395);
		scrollPane1.setBorder(BorderFactory.createLineBorder(Color.black));
		scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.getContentPane().add(scrollPane1);

		// Botões
		
		//botão new
		
		btnNew = new JButton("Novo",
				new ImageIcon(System.getProperty("user.dir") + "\\images\\22x22\\novo.png"));
		btnNew.setBounds(5, 3, 90, 25);
		btnNew.setBorder(BorderFactory.createLineBorder(Color.black));
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				editor.setText(null);
				console.setText(null);
				model.setRowCount(0);
			}
		});
		contentPane.add(btnNew);

		// botão Buscar
		
		btnSearch = new JButton("buscar",
				new ImageIcon(System.getProperty("user.dir") + "\\images\\22x22\\localizar.png"));
		btnSearch.setBounds(btnNew.getWidth()*2 + 15, 3, btnNew.getWidth(), 25);
		btnSearch.setBorder(BorderFactory.createLineBorder(Color.black));
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setCurrentDirectory(new File("Documentos"));

				try {
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						text = new FileManipulator().fileRead(chooser.getSelectedFile().getPath());
						editor.setText(text);
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}



			}
		});
		contentPane.add(btnSearch);

		// botão executar
		
		btnRun = new JButton("Executar",
				new ImageIcon(System.getProperty("user.dir") + "\\images\\22x22\\avancar.png"));
		btnRun.setBounds(btnNew.getWidth() + 10, 3, btnNew.getWidth(), 25);
		btnRun.setBorder(BorderFactory.createLineBorder(Color.black));
		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				console.setText(null);
				console.append(Color.BLUE, "Executando", true);
				String aux = editor.getText().replaceAll("\r\r", "");
				editor.setText(null);
				editor.append(Color.black, aux, false);
				if (!editor.getText().isEmpty()) {
					model.setRowCount(0);
					tokens = automato.splitSimbols(getTextArea());
					if (tokens != null) {
						for (Token token : tokens) {
							model.addRow(new String[] { Integer.toString(token.getCodigo()),
									Integer.toString(token.getLinha()), token.getSimbolo() });
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Campo de texto vazio");
				}
				console.append(Color.blue, "\nFinalizado", true);

			}
		});
		contentPane.add(btnRun);
		
		// botão salvar
		
		btnSave = new JButton("Salvar",
				new ImageIcon(System.getProperty("user.dir") + "\\images\\22x22\\salvar.png"));
		btnSave.setBounds(btnNew.getWidth()*3 + 20, 3, btnNew.getWidth(), 25);
		btnSave.setBorder(BorderFactory.createLineBorder(Color.black));
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setCurrentDirectory(new File("Documentos"));
				
				int op;
				String name;
				File arq;
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					op = JOptionPane.showConfirmDialog(null, "Deseja uma cópia salva neste diretório?", "criar diretório",  JOptionPane.YES_NO_OPTION);
					if(op == 0) {
						name = JOptionPane.showInputDialog("Informe o nome do arquivo");
						arq = new File(chooser.getSelectedFile().getPath() + "\\" + name);
						
						if(arq.exists()) {
							JOptionPane.showMessageDialog(null, "Arquivo com o mesmo nome já existente");
						}
						else {
							try {
								arq.createNewFile();
								FileManipulator.fileWrite(arq.getAbsolutePath(), editor.getText());
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
					}
				}
			}
		});
		contentPane.add(btnSave);
		
		btnBuild = new JButton("Build",
				new ImageIcon(System.getProperty("user.dir") + "\\images\\22x22\\subir.png"));
		btnBuild.setBounds(btnSave.getBounds().x + btnSave.getWidth() + 5, btnSave.getBounds().y, btnSave.getWidth(), 25);
		btnBuild.setBorder(BorderFactory.createLineBorder(Color.black));
		btnBuild.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				automato.analiseSintatica(automato.splitSimbols(getTextArea()));
			}
		});
		contentPane.add(btnBuild);		


		// editor

		editor = new ColorPane(this);
		editor.setBorder(BorderFactory.createLineBorder(Color.black));
		scrollPane3 = new JScrollPane(editor);
		TextLineNumber contadorLinhas = new TextLineNumber(editor);
		scrollPane3.setRowHeaderView(contadorLinhas);
		scrollPane3.setBounds(5, 30, 789, 355);
		scrollPane3.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(scrollPane3);
		// console

		console = new ColorPane(this);
		console.setBorder(BorderFactory.createLineBorder(Color.black));
		scrollPane2 = new JScrollPane(console);
		scrollPane2.setBounds(5, 390, 789, 155);
		scrollPane2.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(scrollPane2);

	}

	// pegar texto do editor

	public ArrayList<String> getTextArea() {
		ArrayList<String> lista = new ArrayList<>();
		String texto = editor.getText();
		String[] aux;
		aux = texto.split("\n");
		for (int i = 0; i < aux.length; i++) {
			lista.add(aux[i]);
			System.out.println(lista.get(i));
		}
		return lista;
	}

	// print console

	public void printError(ArrayList<Erro> erros) {
		for (Erro bug : erros) {
			// console1.setText(console1.getText()+ "\n" + "Error: " + bug.getMsgError()+ "
			// line: " + bug.getLinha() + "\n");
			console.append(Color.red, "Erro: " + bug.getMsgError() + " linha: " + bug.getLinha() + "\n", true);
		}
	}

	// setar novo texto

	public void newText(ArrayList<Erro> erros) {
		ArrayList<String> textList = getTextArea();
		editor.setText(null);
		int i = 1;
		for (String text : textList) {
			if (i == erros.get(0).getLinha()) {
				editor.appendError(Color.lightGray, text+"\n");
			} else {
				editor.append(Color.white, text, true);

			}
			i++;
		}
	}

}
