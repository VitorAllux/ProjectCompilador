package Compilador;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class IDE extends JFrame {

	// PANES
	public ColorPane editor, console;
	private JScrollPane scrollPane1, scrollPane2, scrollPane3, scrollPane4;
	private JLayeredPane tablePane;
	// BUTTONS
	private JButton btnSearch, btnRun, btnSave, btnNew, btnBuild, btnDebug, btnStep;
	// TOKENS
	private Stack<Token> tokens = new Stack<Token>();
	private static Stack<Token> listA;
	private static ArrayList<TokenX> listX;
	// STRINGS
	public String aux = new String();
	private String text;
	// TABBLES AND MODELS
	private JTable tableL, tableS;
	private DefaultTableModel modelL, modelS;
	// AUTOMATO E IDE
	private Automatos automato;
	public IDE ide = this;
	private String dir = null;
	public int linerror;

	File arq;

	public IDE() {

		automato = new Automatos(ide);
		listX = new ArrayList<TokenX>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setBounds(400, 200, 1050, 680);
		GroupLayout layout = new GroupLayout(this.getContentPane());
		setLocationRelativeTo(null);
		this.setLayout(layout);

		// DEBUG TABLES
		// LEXIC TABLE
		String colunas[] = { "Codigo", "Linha", "Simbolo" };

		modelL = new DefaultTableModel(null, colunas) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableL = new JTable();
		tableL.setModel(modelL);
		tableL.setBorder(BorderFactory.createLineBorder(Color.black));
		tableL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableL.setBounds(800, 150, 235, 395);
		;

		// SINTATIC TABLE
		String colunas2[] = { "Codigo", "Simbolo" };

		modelS = new DefaultTableModel(null, colunas2) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableS = new JTable();
		tableS.setModel(modelS);
		tableS.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		tableS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableS.setBounds(800, 20, 235, 395);

		// SCROLL PANES
		tablePane = new JLayeredPane();

		scrollPane1 = new JScrollPane(tableL, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane1.setBounds(5, 150, 235, 330);
		scrollPane1.setBorder(BorderFactory.createLineBorder(Color.black));
		tablePane.add(scrollPane1);

		scrollPane2 = new JScrollPane(tableS, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane2.setBounds(5, 0, 235, 145);
		scrollPane2.setBorder(BorderFactory.createLineBorder(Color.black));

		tablePane.add(scrollPane2);
		tablePane.setVisible(false);

		// BUTTONS

		// BUTTON NEW
		btnNew = new JButton("Novo", new ImageIcon(System.getProperty("user.dir") + "\\images\\22x22\\novo.png"));
		btnNew.setBorder(BorderFactory.createLineBorder(Color.black));
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				editor.setText(null);
				console.setText(null);
				modelL.setRowCount(0);
				modelS.setRowCount(0);
				dir = null;
			}
		});

		// BUTTON SEARCH
		btnSearch = new JButton("buscar",
				new ImageIcon(System.getProperty("user.dir") + "\\images\\22x22\\localizar.png"));
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
						dir = chooser.getSelectedFile().getAbsolutePath();
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		// BUTTON EXECUTE

		btnRun = new JButton("Executar",
				new ImageIcon(System.getProperty("user.dir") + "\\images\\22x22\\avancar.png"));
		btnRun.setBorder(BorderFactory.createLineBorder(Color.black));
		btnRun.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				btnBuild.setEnabled(false);
				if (!editor.getText().isEmpty()) {
					console.setText(null);
					console.append(Color.BLUE, "Executando", true);
					String aux = editor.getText().replaceAll("\r\r", "");
					editor.setText(null);
					editor.append(Color.black, aux, false);
					if (!editor.getText().isEmpty()) {
						modelL.setRowCount(0);
						tokens = automato.splitSimbols(getEditorText());
						try {
							listA = (Stack<Token>) tokens.clone();
							listX.clear();
							atualizaTableS();
							btnBuild.setEnabled(true);
						} catch (Exception e2) {
							System.out.println("Erro no preenchimento das listas");
						}

						if (tokens != null) {
							for (Token token : tokens) {
								modelL.addRow(new String[] { Integer.toString(token.getCodigo()),
										Integer.toString(token.getLinha()), token.getSimbolo() });
							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "Campo de texto vazio");
					}
					console.append(Color.blue, "\nFinalizado", true);
					btnStep.setEnabled(true);
					btnBuild.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(null, "Editor vazio!");
				}
			}
		});

		// BUTTON SAVE

		btnSave = new JButton("Salvar", new ImageIcon(System.getProperty("user.dir") + "\\images\\22x22\\salvar.png"));
		btnSave.setBorder(BorderFactory.createLineBorder(Color.black));
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (dir!=null) {
					System.out.println(dir);
					arq = new File(dir);
					if (arq.exists()) {
						try {
							arq.delete();
							arq.createNewFile();
							FileManipulator.fileWrite(arq.getAbsolutePath(), editor.getText());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						try {
							arq.createNewFile();
							FileManipulator.fileWrite(arq.getAbsolutePath(), editor.getText());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} else {
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					if(arq != null) {
						chooser.setCurrentDirectory(arq);
					}else {
						chooser.setCurrentDirectory(new File("Documentos"));
					}
					if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						System.out.println(chooser.getName());	
						arq = chooser.getSelectedFile(); 
						dir = arq.getAbsolutePath();
						if (chooser.getSelectedFile().exists()) {
							JOptionPane.showMessageDialog(null, "Arquivo com o mesmo nome já existente");
						} else {
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

		// BUTTON BUILD
		btnBuild = new JButton("Build", new ImageIcon(System.getProperty("user.dir") + "\\images\\22x22\\build.png"));
		btnBuild.setBorder(BorderFactory.createLineBorder(Color.black));
		btnBuild.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (editor.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Editor Vazio!");
				} else {
					console.setText(null);
					while (!listA.isEmpty()) {
						if (automato.analiseSintatica(false, listX, listA)) {
							atualizaTableL();
						}
						atualizaTableS();
						if (!automato.erros.isEmpty()) {
							break;
						}
					}
					btnBuild.setEnabled(false);
				}
			}
		});
		btnBuild.setVisible(false);
		btnBuild.setEnabled(false);

		// BUTTON STEP
		btnStep = new JButton(">");
		btnStep.setBorder(BorderFactory.createLineBorder(Color.black));
		btnStep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				console.setText(null);
				if (automato.analiseSintatica(true, listX, listA)) {
					atualizaTableL();
				}
				atualizaTableS();
				if (!automato.erros.isEmpty()) {
					btnStep.setEnabled(false);
				} else {
					btnStep.setEnabled(!listA.isEmpty());
				}
			}
		});
		btnStep.setBounds(1006, 20, 25, 25);
		btnStep.setVisible(false);
		btnStep.setEnabled(false);
		this.getContentPane().add(btnStep);
		// BUTTON DEBBUG
		btnDebug = new JButton("Debug", new ImageIcon(System.getProperty("user.dir") + "\\images\\22x22\\debug.png"));
		btnDebug.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btnDebug.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (tablePane.isVisible()) {
					tablePane.setVisible(false);
					btnStep.setVisible(false);
				} else {
					tablePane.setVisible(true);
					btnStep.setVisible(true);
				}
				if (btnBuild.isVisible()) {
					btnBuild.setVisible(false);
				} else {
					btnBuild.setVisible(true);
				}

			}
		});

		// PANES
		// EDITOR PANE
		editor = new ColorPane(this);
		editor.setBorder(BorderFactory.createLineBorder(Color.black));

		TextLineNumber contadorLinhas = new TextLineNumber(editor);
		// CONSOLE PANE
		console = new ColorPane(this);
		console.setBorder(BorderFactory.createLineBorder(Color.black));
		// SCROLL PANE
		scrollPane3 = new JScrollPane(editor);
		scrollPane3.setRowHeaderView(contadorLinhas);
		scrollPane3.setSize(this.getWidth(), 335);
		scrollPane3.setBorder(BorderFactory.createLineBorder(Color.black));
		scrollPane4 = new JScrollPane(console);
		scrollPane4.setSize(790, 155);
		scrollPane4.setBorder(BorderFactory.createLineBorder(Color.black));

		// LAYOUT
		// ADD MAGINS
		layout.setAutoCreateContainerGaps(true);
		// HORIZONTAL GROUP
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				// BUTTONS GROUP
				.addGroup(layout.createSequentialGroup()
						// BUTTONS
						.addComponent(btnNew, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE).addGap(2)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE).addGap(2)
						.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE).addGap(2)
						.addComponent(btnRun, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE).addGap(2)
						.addComponent(btnDebug, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE).addGap(2)
						// .addComponent(btnStep, GroupLayout.PREFERRED_SIZE, 90,
						// GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBuild, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE).addGap(380))
				// EDITOR GROUP
				.addGroup(layout.createSequentialGroup()
						// EDITOR
						.addComponent(scrollPane3)
						// TABLE
						.addComponent(tablePane, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE))
				// CONSOLE GROUP
				.addGroup(layout.createSequentialGroup()
						// CONSOLE
						.addComponent(scrollPane4, GroupLayout.PREFERRED_SIZE, this.getWidth() - 30,
								GroupLayout.PREFERRED_SIZE))));

		// VERTICAL GROUP
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addGroup(layout
				.createSequentialGroup()
				// BUTTONS GROUP
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						// BUTTONS
						.addComponent(btnNew, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnRun, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDebug, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						// .addComponent(btnStep, GroupLayout.PREFERRED_SIZE, 25,
						// GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBuild, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
				// EDITOR GROUP
				.addGap(10).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						// EDITOR
						.addComponent(scrollPane3)
						// TABLE
						.addComponent(tablePane, GroupLayout.PREFERRED_SIZE, 480, GroupLayout.PREFERRED_SIZE))
				// CONSOLE GROUP
				.addGap(10).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						// CONSOLE
						.addComponent(scrollPane4, GroupLayout.PREFERRED_SIZE, 130 - 30, GroupLayout.PREFERRED_SIZE))));

	}

	private void atualizaTableL() {
		modelL.removeRow(0);
	}

	private void atualizaTableS() {
		modelS.setRowCount(0);

		for (TokenX token : listX) {
			modelS.addRow(new Object[] { token.getCodigo().toString(), token.getSimbolo() });
		}
	}

	// MAIN
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IDE frame = new IDE();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// FUNCTIONS
	// GET TEXT OF EDITOR
	public ArrayList<String> getEditorText() {
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

	public void rolarScroll(int linha) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JScrollBar temp = scrollPane3.getVerticalScrollBar();
				temp.setValue(linha * 10);
			}
		});
	}

	// PRINT ERROR CONSOLE
	public void printError(ArrayList<Erro> erros) {
		for (Erro bug : erros) {
			// console1.setText(console1.getText()+ "\n" + "Error: " + bug.getMsgError()+ "
			// line: " + bug.getLinha() + "\n");
			btnStep.setEnabled(false);
			btnBuild.setEnabled(false);
			console.append(Color.red,
					"Erro: " + bug.getMsgError()
					+ (bug.getTipo().equals("sintatico")
							? ("     Encontrado: [" + listA.get(0).getSimbolo() + "] Esperado: ["
									+ listX.get(0).getSimbolo() + "]     ")
									: "")
					+ " linha: " + bug.getLinha() + "\n",
					true);
			linerror = bug.getLinha();
		}
	}

	// SET NEW TEXT EDITOR
	public void newText(ArrayList<Erro> erros, boolean newe) {
		if(!newe) {
			ArrayList<String> textList = getEditorText();
			editor.setText(null);
			int i = 1;
			for (String text : textList) {
				if (i == erros.get(0).getLinha()) {
					editor.appendError(Color.lightGray, text + "\n");
				} else {
					editor.append(Color.white, text, true);

				}
				i++;
			}
		}

		else {
			String aux = editor.getText();
			editor.setText(null);
			editor.append(Color.white, aux, false);
		}
		ide.rolarScroll(linerror);
	}

}
