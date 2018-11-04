package de.cawolf.quickmock;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import de.cawolf.quickmock.intention.ConstantsKt;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.util.ResourceBundle;

public class Configuration implements Configurable {
    private JPanel panel;
    private JCheckBox addDocBlockForMembers;

    @Nls
    @Override
    public String getDisplayName() {
        return ResourceBundle.getBundle(ConstantsKt.RESOURCE_BUNDLE).getString("configuration.display");
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return panel;
    }

    @Override
    public boolean isModified() {
        return !getSettings().addDocBlockForMembers.equals(addDocBlockForMembers.isSelected());
    }

    @Override
    public void apply() {
        getSettings().addDocBlockForMembers = addDocBlockForMembers.isSelected();
    }

    @Override
    public void reset() {
        addDocBlockForMembers.setSelected(getSettings().addDocBlockForMembers);
    }

    private Settings getSettings() {
        return ServiceManager.getService(Settings.class).getState();
    }
}
