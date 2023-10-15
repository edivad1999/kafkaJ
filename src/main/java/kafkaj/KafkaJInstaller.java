package kafkaj;

import com.edivad1999.kafkaj.KafkaProgressBarUi;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

public class KafkaJInstaller {
    @SuppressWarnings({ "MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration" })
    public static ComponentUI createUI(JComponent c) {
        c.setBorder(JBUI.Borders.empty().asUIResource());
        return new KafkaProgressBarUi();
    }
}
