/*
 *  Copyright (C) 2009 Jack Park,
 * 	mail : jackpark@gmail.com
 *
 *  Part of IBIS Server, an open source project.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.nex.ibis.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.nex.util.TextFileHandler;
/**
 * <p>Title: IBIS Server Tester</p>
 * <p>Description: Test Webservices with the IBIS Server</p>
 * <p>Copyright: Copyright (c) 2009, Jack Park</p>
 * <p>Company: NexistGroup</p>
 * @author Park
 * @version 1.0
 *
 * <p>TODO: we must open with a feature that lets user
 * authenticate and set baseURL when calling helper.init()
 * </p>
 */
public class MainFrame
    extends JFrame {
  TestHelper helper;
  JPanel contentPane;
  BorderLayout borderLayout1 = new BorderLayout();
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuFileExit = new JMenuItem();
  JLabel statusBar = new JLabel();
//  JScrollPane jScrollPane1 = new JScrollPane();
//  JTextArea consoleArea = new JTextArea();
  JMenu testMenu = new JMenu();
  JMenuItem postMenuItem = new JMenuItem();
  JMenuItem removeMenuITem = new JMenuItem();
  JMenuItem listItem = new JMenuItem();
//  JTabbedPane jTabbedPane1 = new JTabbedPane();
//  JPanel jPanel1 = new JPanel();
//  BorderLayout borderLayout2 = new BorderLayout();
  JSplitPane jSplitPane1 = new JSplitPane();
  JPanel leftPanel = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel rightPanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  JScrollPane jScrollPane2 = new JScrollPane();
  JTextArea contentArea = new JTextArea();
  JScrollPane jScrollPane3 = new JScrollPane();
  JTable indexTable = new JTable();
  UploadFormDialog uploadDialog;
  ServerInitializeDialog serverDialog;
  FileRemoveDialog removeDialog;
  JMenuItem configItem = new JMenuItem();
  JMenuItem saveAsItem = new JMenuItem();

  public MainFrame() {
    try {
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      jbInit();
      helper = new TestHelper(this);
      uploadDialog = new UploadFormDialog();
      uploadDialog.setHost(this);
      serverDialog = new ServerInitializeDialog(this);
      //start by asking for server stuff.
      removeDialog = new FileRemoveDialog(this);
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Component initialization.
   * @throws java.lang.Exception
   */
  private void jbInit() throws Exception {
    contentPane = (JPanel) getContentPane();
    contentPane.setLayout(borderLayout1);
    setSize(new Dimension(800, 600));
    setTitle("IBIS Server Client");
    statusBar.setText(" ");
    jMenuFile.setText("File");
    jMenuFileExit.setText("Exit");
    jMenuFileExit.addActionListener(new MainFrame_jMenuFileExit_ActionAdapter(this));
//    consoleArea.setLineWrap(true);
    testMenu.setText("Server");
    postMenuItem.setText("Post...");
    postMenuItem.addActionListener(new MainFrame_postMenuItem_actionAdapter(this));
    removeMenuITem.setText("Remove...");
    removeMenuITem.addActionListener(new MainFrame_removeMenuITem_actionAdapter(this));
    listItem.setText("List...");
    listItem.addActionListener(new MainFrame_listItem_actionAdapter(this));
//    jPanel1.setLayout(borderLayout2);
    leftPanel.setLayout(borderLayout4);
    rightPanel.setLayout(borderLayout5);
    contentArea.setText("");
    contentArea.setLineWrap(true);
    indexTable.addMouseListener(new MainFrame_jTable1_mouseAdapter(this));
    configItem.setText("Config Server...");
    configItem.addActionListener(new MainFrame_configItem_actionAdapter(this));
    saveAsItem.setText("Save As...");
    saveAsItem.addActionListener(new MainFrame_saveAsItem_actionAdapter(this));
    testMenu.add(configItem);
    testMenu.add(listItem);
    testMenu.add(postMenuItem);
    testMenu.add(removeMenuITem);
    jMenuBar1.add(jMenuFile);
    jMenuFile.add(saveAsItem);
    jMenuFile.addSeparator();
    jMenuFile.add(jMenuFileExit);
    jMenuBar1.add(testMenu);
    setJMenuBar(jMenuBar1);
    contentPane.add(statusBar, BorderLayout.SOUTH);
    contentPane.add(jSplitPane1);
    //    jScrollPane1.getViewport().add(consoleArea);
    contentPane.add(/*jTabbedPane1*/jSplitPane1, java.awt.BorderLayout.CENTER);
//    jTabbedPane1.add(jPanel2, "Directory");
//    jTabbedPane1.add(jPanel1, "Console");
//    jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);
    jSplitPane1.add(leftPanel, JSplitPane.LEFT);
    leftPanel.add(jScrollPane3, java.awt.BorderLayout.CENTER);
    jSplitPane1.add(rightPanel, JSplitPane.RIGHT);
    rightPanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);
    jScrollPane2.getViewport().add(contentArea);
    jScrollPane3.getViewport().add(indexTable);
    tell("Ready");
    saveAsItem.setEnabled(false);
  }

  /**
   * Callback to set Status Bar
   * @param msg String
   */
  public void tell(String msg) {
    statusBar.setText(msg);
  }
  /**
   * File | Exit action performed.
   * @param actionEvent ActionEvent
   */
  void jMenuFileExit_actionPerformed(ActionEvent actionEvent) {
    System.exit(0);
  }
  /**
   * Fetch a list of all files from server
   * @param e ActionEvent
   */
  public void listItem_actionPerformed(ActionEvent e) {
      try {
        contentArea.setText("");
        acceptList(helper.listFiles());
      } catch (Exception x) {
        x.printStackTrace();
      }
  }
  /**
   * Paint files in the table
   * @param files List
   */
  private void acceptList(java.util.List<java.util.List> files) {
    int len = files.size();
    DefaultTableModel model = new DefaultTableModel(0,4);
    for (int i=0;i<files.size();i++) {
      java.util.List x = files.get(i);
      java.util.Vector v = new java.util.Vector(3);
      v.addElement(x.get(0));
      v.addElement(x.get(1));
      v.addElement(x.get(3));
      v.addElement(x.get(2));
      model.addRow(v);
    }
    indexTable.setModel(model);
  }
  /**
   * Select a file to fetch from the server
   * @param e MouseEvent
   */
  public void indexTable_mouseClicked(MouseEvent e) {
    int which = indexTable.getSelectedRow();
    contentArea.setText("");
    DefaultTableModel m = (DefaultTableModel)indexTable.getModel();
    //String filename =
    Object o = m.getValueAt(which,0);
    String filename = (String)o;
    o = m.getValueAt(which,1);
    String platform = (String)o;
    System.out.println(filename+" "+platform);
    try {
      //go get the file
      String doc = helper.getDocument(platform,filename);
      contentArea.setText(doc);
      saveAsItem.setEnabled(true);
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  public void postMenuItem_actionPerformed(ActionEvent e) {
    uploadDialog.setVisible(true);
  }

  public void handleUpload(String fileName, String platform,
                           String description, String cargo,
                           String login, String password) {
    System.out.println(fileName);
    try {
      helper.postDocument(platform,fileName, description,cargo,login,password);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void removeMenuITem_actionPerformed(ActionEvent e) {
    try {
      removeDialog.fillTable(helper.listFiles());
      removeDialog.setVisible(true);
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  public void removeDialogCallBack(String filename, String platform,
                                   String login, String password) {
    try {
      helper.removeDocument(platform,filename,login,password);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public void configItem_actionPerformed(ActionEvent e) {
    serverDialog.setVisible(true);
  }

  public void configMenuCallback(String server, String port) {
    helper.initServer(server, Integer.parseInt(port));
  }

  public void saveAsItem_actionPerformed(ActionEvent e) {
    TextFileHandler h = new TextFileHandler();
    h.saveAs(contentArea.getText());
    saveAsItem.setEnabled(false);
  }
}

class MainFrame_saveAsItem_actionAdapter
    implements ActionListener {
  private MainFrame adaptee;
  MainFrame_saveAsItem_actionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.saveAsItem_actionPerformed(e);
  }
}

class MainFrame_configItem_actionAdapter
    implements ActionListener {
  private MainFrame adaptee;
  MainFrame_configItem_actionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.configItem_actionPerformed(e);
  }
}

class MainFrame_removeMenuITem_actionAdapter
    implements ActionListener {
  private MainFrame adaptee;
  MainFrame_removeMenuITem_actionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.removeMenuITem_actionPerformed(e);
  }
}

class MainFrame_postMenuItem_actionAdapter
    implements ActionListener {
  private MainFrame adaptee;
  MainFrame_postMenuItem_actionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.postMenuItem_actionPerformed(e);
  }
}

class MainFrame_jTable1_mouseAdapter
    extends MouseAdapter {
  private MainFrame adaptee;
  MainFrame_jTable1_mouseAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e) {
    adaptee.indexTable_mouseClicked(e);
  }
}

class MainFrame_listItem_actionAdapter
    implements ActionListener {
  private MainFrame adaptee;
  MainFrame_listItem_actionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.listItem_actionPerformed(e);
  }
}

class MainFrame_jMenuFileExit_ActionAdapter
    implements ActionListener {
  MainFrame adaptee;

  MainFrame_jMenuFileExit_ActionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent actionEvent) {
    adaptee.jMenuFileExit_actionPerformed(actionEvent);
  }
}
