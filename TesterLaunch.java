import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

public class TesterLaunch extends JFrame {
	
	JTabbedPane tabs;
	
	Container tabDisplay;
	
	Container tabControl;
	
	JTextArea globalEntry;
	JTextArea controlEntry;
	
	static final String REGEX_COLOR = "\\[\\d+,\\d+,\\d+\\]";
	static final String REGEX_URL = "\\[https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*\\])";	//	https://stackoverflow.com/questions/3809401/what-is-a-good-regular-expression-to-match-a-url
	static final String REGEX_PATH = "\\[([a-zA-Z]:)?(\\\\[a-zA-Z0-9._-]+)+\\\\?\\]";
	
	public TesterLaunch() {
		tabs = new JTabbedPane();
		
		tabDisplay = new Container() {{
			setLayout(new MigLayout());
			tabs.addTab("display", this);
		}};
		
		tabControl = new Container() {{
			setLayout(new MigLayout("al center center, flowy"));
			
			add(new JLabel() {{
				setText("global : ");
			}}, "flowx, split 2");
			
			globalEntry = new JTextArea();
			add(globalEntry, "w 100%, h 10%, gap 5 5 5 5");
			
			controlEntry = new JTextArea();
			add(controlEntry, "w 100%, h 90%, gap 5 5 5 5");
			
			tabs.addTab("control", this);
		}};
		
		add(tabs);
		
		tabs.addChangeListener((e) -> {
			if(tabs.getSelectedIndex() == 0) {
				
				tabDisplay.removeAll();
				var global = globalEntry.getText();
				
				if(!global.isBlank())
					tabDisplay.setLayout(new MigLayout(global));
					
				try (var controls = controlEntry.getText().lines()) {
					controls.forEach((str) -> {
						var parts = str.split("\\|");
						
						var obj = parts[0].trim();
						
						Component objComp;
						if(Pattern.matches(REGEX_COLOR, obj)) {
							var rgbStr = obj.substring(1, parts[0].length() - 1).split(",");
							var rgb = new int[3];
							for(int i = 0; i < 3; i++) 
								rgb[i] = Integer.valueOf(rgbStr[i]);
							var color = new Color(rgb[0], rgb[1], rgb[2]);
							objComp = new JPanel() {{
								setBackground(color);
							}};
						}else if(Pattern.matches(REGEX_URL, obj)) {
							
							objComp = new JLabel() {{
								try {
									setIcon(new ImageIcon(new URL(obj.substring(1, parts[0].length() - 1))));
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}};
							
						}else if(Pattern.matches(REGEX_PATH, obj)) {
							
							objComp = new JLabel() {{
								setIcon(new ImageIcon(obj.substring(1, parts[0].length() - 1)));
							}};
							
						}else {
							objComp = new JLabel() {{
								setText(obj);
							}};
						}
						if(parts.length == 1) {
							tabDisplay.add(objComp);
						}else {
							tabDisplay.add(objComp, parts[1]);
						}
					});
				}
			}
		});
		
		setTitle("MiGLayout Tester");
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			var app = new TesterLaunch();
			app.setVisible(true);
		});
	}
}
