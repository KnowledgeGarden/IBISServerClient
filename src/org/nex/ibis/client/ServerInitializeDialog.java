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

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

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
public class ServerInitializeDialog
    extends JDialog {
  MainFrame host;
  GridLayout gridLayout1 = new GridLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  JPanel jPanel3 = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton cancelButton = new JButton();
  JButton submitButton = new JButton();
  FlowLayout flowLayout2 = new FlowLayout();
  JTextField urlField = new JTextField();
  JLabel jLabel1 = new JLabel();
  FlowLayout flowLayout3 = new FlowLayout();
  JTextField portField = new JTextField();
  JLabel jLabel2 = new JLabel();

  public ServerInitializeDialog(MainFrame h) {
    host = h;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.getContentPane().setLayout(gridLayout1);
    this.setTitle("Server Config");
    this.setSize(200,120);
    gridLayout1.setRows(3);
    cancelButton.addActionListener(new
        ServerInitializeDialog_cancelButton_actionAdapter(this));
    submitButton.addActionListener(new
        ServerInitializeDialog_submitButton_actionAdapter(this));
    jPanel1.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    urlField.setPreferredSize(new Dimension(120, 20));
    urlField.setText("");
    jLabel1.setText("Server URL");
    jPanel3.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    portField.setPreferredSize(new Dimension(40, 20));
    portField.setText("80");
    jLabel2.setText("Port (80=default)");
    this.getContentPane().add(jPanel1);
    jPanel1.add(jLabel1);
    jPanel1.add(urlField);
    cancelButton.setText("Cancel");
    jPanel2.setLayout(flowLayout1);
    this.getContentPane().add(jPanel3);
    jPanel3.add(jLabel2);
    jPanel3.add(portField);
    this.getContentPane().add(jPanel2);
    submitButton.setText("Submit");
    jPanel2.add(submitButton);
    jPanel2.add(cancelButton);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = this.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    this.setLocation( (screenSize.width - frameSize.width) / 2,
                     (screenSize.height - frameSize.height) / 2);

  }

  public void cancelButton_actionPerformed(ActionEvent e) {
    this.setVisible(false);
  }

  public void submitButton_actionPerformed(ActionEvent e) {
    host.configMenuCallback(urlField.getText(), portField.getText());
    this.setVisible(false);
  }
}

class ServerInitializeDialog_cancelButton_actionAdapter
    implements ActionListener {
  private ServerInitializeDialog adaptee;
  ServerInitializeDialog_cancelButton_actionAdapter(ServerInitializeDialog
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}

class ServerInitializeDialog_submitButton_actionAdapter
    implements ActionListener {
  private ServerInitializeDialog adaptee;
  ServerInitializeDialog_submitButton_actionAdapter(ServerInitializeDialog
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.submitButton_actionPerformed(e);
  }
}
