package org.infobeyondtech.unifieddl.guiservice;

import org.apache.log4j.Logger;
import org.infobeyondtech.unifieddl.gui.ProcessingDialog;

import javax.swing.*;
import java.awt.*;

public abstract class ParallelProcess extends SwingWorker<Void, Void> {

  public final static Logger logger = Logger.getLogger(ParallelProcess.class);

  protected JDialog loadPopUp;
  protected Window parent;
  // AppController controller;
  Boolean isShowLoadingScreen = true;


  public ParallelProcess(Window parent, Boolean isShowLoadingScreen) {
    this.parent = parent;
    this.isShowLoadingScreen = isShowLoadingScreen;
    init();
  }

  public ParallelProcess(String processingHead) {
    this.parent = null;
    this.isShowLoadingScreen = true;
    init(processingHead);
  }

  public void init() {
    if (isShowLoadingScreen) {
      loadPopUp = new ProcessingDialog(parent);
    }
  }

  public void init(String processHead) {
    if (isShowLoadingScreen) {
      loadPopUp = new ProcessingDialog(parent, processHead);
    }
  }

  public abstract void mainBackgroundProcess();

  public void doneProcess() {

  };

  @Override
  public Void doInBackground() {
    mainBackgroundProcess();
    return null;
  }


  @Override
  public void done() {
    destroyLoading();
    doneProcess();
  }


  public void hideLoading() {
    if (isShowLoadingScreen) {
      loadPopUp.setVisible(false);
    }
  }

  public void showLoading() {
    if (isShowLoadingScreen) {
      loadPopUp.setVisible(true);
    }
  }

  public void destroyLoading() {
    if (isShowLoadingScreen) {
      loadPopUp.dispose();
    }
  }

}
