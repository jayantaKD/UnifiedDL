package org.infobeyondtech.unifieddl.gui.base;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class InfobeyondTitleBorder extends TitledBorder {



  private static final long serialVersionUID = 1L;
  private static Border defaultBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);// new
                                                                                               // LineBorder
                                                                                               // (Color.BLUE,
                                                                                               // 1,
                                                                                               // true
                                                                                               // );
  private static Color defaultborderTitleColor = Color.BLACK;
  private static int defaultborderTitleFontSize = 11;



  public InfobeyondTitleBorder(String borderHeading) {



    super(
        BorderFactory.createCompoundBorder(defaultBorder,
            BorderFactory.createEmptyBorder(10, 10, 10, 10)),
        borderHeading, TitledBorder.CENTER, TitledBorder.TOP, null, defaultborderTitleColor);
    this.setTitleFont(
        new Font(this.getTitleFont().getFontName(), Font.PLAIN, defaultborderTitleFontSize));

  }


  public InfobeyondTitleBorder(String borderHeading, int fontSize) {

    super(
        BorderFactory.createCompoundBorder(defaultBorder,
            BorderFactory.createEmptyBorder(10, 10, 10, 10)),
        borderHeading, TitledBorder.CENTER, TitledBorder.TOP, null, defaultborderTitleColor);
    this.setTitleFont(new Font(this.getTitleFont().getFontName(), Font.PLAIN, fontSize));

  }



  public void setBorderColor(Color X) {
    this.setBorder(new LineBorder(X, 1, true));
    this.setTitleColor(X);
  }



  public void deleteBorderLine() {
    this.setBorder(new LineBorder(Color.BLACK, 0, true));
  }

}
