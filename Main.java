/*	Program name:	Lab 09 Tree
	Programmer:		Marcus Ross
	Date Due:		5 May, 2014
	Description:	This program loads a tree with some hundreds of thousands of longs from a file and prompts the user to search the tree. Before the program closes, it displays the average depth of levels accessed per search.	*/

package lab09;

import java.io.*;
import java.util.*;
import lab09.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		Frame frame = new Frame("Number Tree Search");
		LoadFrame(frame);
	}

	public static void LoadFrame(Frame frame) {
		Tree tree = new Tree();
		LoadTree(tree);
		Counter counter = new Counter();

		// initialize components
		SpringLayout layout = new SpringLayout();
		Panel panel = new Panel(layout);
		Label queryL = new Label("Search Query");
		Label resultL = new Label("Result");
		Label depthL = new Label("Depth");
		TextField queryF = new TextField();
		TextField resultF = new TextField();
		TextField depthF = new TextField();
		Button searchB = new Button("Search");

		// component methods
		resultF.setFocusable(false);
		depthF.setFocusable(false);
		searchB.addActionListener(new SearchLstn(queryF, resultF, depthF, tree, counter));

		// constraints
		layout.putConstraint("North", queryL, 10, "North", panel);
		layout.putConstraint("West", queryL, 10, "West", panel);
		layout.putConstraint("North", queryF, 10, "North", panel);
		layout.putConstraint("West", queryF, 10, "East", queryL);
		layout.putConstraint("East", queryF, -10, "East", panel);
		layout.putConstraint("North", resultL, 0, "North", resultF);
		layout.putConstraint("West", resultL, 10, "West", panel);
		layout.putConstraint("North", resultF, 10, "South", queryF);
		layout.putConstraint("West", resultF, 10, "East", resultL);
		layout.putConstraint("East", resultF, -10, "East", panel);
		layout.putConstraint("North", depthL, 0, "North", depthF);
		layout.putConstraint("West", depthL, 10, "West", panel);
		layout.putConstraint("North", depthF, 10, "South", resultF);
		layout.putConstraint("West", depthF, 10, "East", depthL);
		layout.putConstraint("East", depthF, -10, "East", panel);
		layout.putConstraint("HorizontalCenter", searchB, 0, "HorizontalCenter", panel);
		layout.putConstraint("South", searchB, -10, "South", panel);

		// add components
		panel.add(queryL);	panel.add(queryF);	panel.add(resultL);	panel.add(resultF);
		panel.add(depthL);	panel.add(depthF);	panel.add(searchB);	frame.add(panel);

		// frame methods
		frame.setResizable(false);
		frame.addWindowListener(new WinListen(frame, counter));
		frame.setSize(250, 170);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void LoadTree(Tree tree) {
		Element element;
		Scanner inFileStr, inFileNum;
		String key;
		long num;

		try {
			inFileStr = new Scanner(new BufferedReader(new FileReader(new File("string-numbers.txt"))));
			inFileNum = new Scanner(new BufferedReader(new FileReader(new File("numbers.txt"))));
		} catch(FileNotFoundException e) {
			System.out.println("Data files not found.");
			return;
		}

		try { // took from both files just to make use of all your hard work
			while(inFileStr.hasNext() && inFileNum.hasNext()) {
				key = inFileStr.nextLine();
				num = inFileNum.nextLong();
				element = new Element(key, num);
				tree.add(element);
			}
		} catch(InputMismatchException e) {
			System.out.println("Data file incorrectly formatted.");
		}

		inFileStr.close();
		inFileNum.close();
	}

	public static void UpdateResult(TextField queryF, TextField resultF, TextField depthF, Tree tree, Counter counter) {
		String query;
		Element data;
		int depth;

		query = queryF.getText();
		depth = tree.retrieve(query);

		if(depth == -1) {
			resultF.setText("Search failed");
			depthF.setText("");
		} else {
			resultF.setText("Search succeeded");
			depthF.setText(Integer.toString(depth));
			counter.inc(depth);
		}
	}

	public static void DisplayAvg(Frame frame, Counter counter) {
		String depthStr;
		double depthAvg;

		depthAvg = counter.getAvg();

		if(depthAvg == depthAvg) // check if NaN
			depthStr = String.format("%.1f steps",depthAvg);
		else
			depthStr = "No searches conducted";

		frame.removeAll();

		// initialize components
		SpringLayout layout = new SpringLayout();
		Panel panel = new Panel(layout);
		Label avgL = new Label("Average depth:", Label.CENTER);
		Label depthL = new Label(depthStr, Label.CENTER);

		// constraints
		layout.putConstraint("VerticalCenter", avgL, -10, "VerticalCenter", panel);
		layout.putConstraint("HorizontalCenter", avgL, 0, "HorizontalCenter", panel);
		layout.putConstraint("VerticalCenter", depthL, 10, "VerticalCenter", panel);
		layout.putConstraint("HorizontalCenter", depthL, 0, "HorizontalCenter", panel);

		// add components
		panel.add(avgL);	panel.add(depthL);	frame.add(panel);

		// frame methods
		frame.setTitle("Result");
		frame.setVisible(true);
	}
}

class Counter { // have a better solution? when I can't get primitive out of a function called from an ActionListener
	private int depthSum, qty;

	public Counter() {
		depthSum = 0;
		qty = 0;
	}

	public void inc(int n) {
		depthSum += n;
		qty++;
	}

	public double getAvg() {
		double avg;
		avg = (double)depthSum / qty;
		return avg;
	}
}

class SearchLstn implements ActionListener {
	private TextField queryF, resultF, depthF;
	private Tree tree;
	private Counter counter;

	public SearchLstn(TextField queryF, TextField resultF, TextField depthF, Tree tree, Counter counter) {
		this.queryF = queryF;
		this.resultF = resultF;
		this.depthF = depthF;
		this.tree = tree;
		this.counter = counter;
	}

	public void actionPerformed(ActionEvent e) {
		Main.UpdateResult(queryF, resultF, depthF, tree, counter);
	}
}

class WinListen implements WindowListener {
	private Frame frame;
	private Counter counter;

	public WinListen(Frame frame, Counter counter) {
		this.frame = frame;
		this.counter = counter;
	}

	public void windowClosing(WindowEvent e) {
		if(frame.getTitle().equals("Number Tree Search"))
			Main.DisplayAvg(frame, counter);
		else {
			frame.setVisible(false);
			frame.dispose();
			System.exit(0);
		}
	}

	public void windowOpened(WindowEvent e) { }
	public void windowClosed(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowActivated(WindowEvent e) { }
	public void windowDeactivated(WindowEvent e) { }
}