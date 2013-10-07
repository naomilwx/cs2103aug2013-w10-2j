package nailit.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class HomeWindow extends JFrame{
	protected static final int HOME_WINDOW_HEIGHT = MainWindow.WINDOW_HEIGHT;
	protected static final int HOME_WINDOW_WIDTH = 450;
	private static final int HOME_WINDOW_BORDER_WIDTH = 3;
	protected static final LineBorder HOME_WINDOW_BORDER = new LineBorder(GUIManager.BORDER_COLOR, HOME_WINDOW_BORDER_WIDTH);
	
	private GUIManager GUIBoss;
	private JPanel contentPane;
	
	public HomeWindow(final GUIManager GUIMain){
		GUIBoss = GUIMain;
		initialiseHomeWindow();
	}
	
	private void initialiseHomeWindow(){
		configureHomeWindowFrame();
		createAndInitialiseContentPane();
	}
	private void configureHomeWindowFrame(){
		setUndecorated(true);
		setLocation(GUIManager.HOME_WINDOW_X_POS, GUIManager.HOME_WINDOW_Y_POS);
		setSize(HOME_WINDOW_WIDTH, HOME_WINDOW_HEIGHT);
		setResizable(false);
	}
	private void createAndInitialiseContentPane(){
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBorder(HOME_WINDOW_BORDER);
		setContentPane(contentPane);
	}
}
